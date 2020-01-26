package ru.avalieva.otus.libraryMongoDB_hw08.service.impl;

import org.springframework.stereotype.Service;
import ru.avalieva.otus.libraryMongoDB_hw08.domain.Author;
import ru.avalieva.otus.libraryMongoDB_hw08.domain.Book;
import ru.avalieva.otus.libraryMongoDB_hw08.domain.Comment;
import ru.avalieva.otus.libraryMongoDB_hw08.domain.Genre;
import ru.avalieva.otus.libraryMongoDB_hw08.repository.AuthorRepository;
import ru.avalieva.otus.libraryMongoDB_hw08.repository.BookRepository;
import ru.avalieva.otus.libraryMongoDB_hw08.repository.CommentRepository;
import ru.avalieva.otus.libraryMongoDB_hw08.repository.GenreRepository;
import ru.avalieva.otus.libraryMongoDB_hw08.service.IOService;
import ru.avalieva.otus.libraryMongoDB_hw08.service.LibraryService;
import ru.avalieva.otus.libraryMongoDB_hw08.service.MessageService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibraryServiceImpl implements LibraryService {

    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;
    private final IOService ioService;
    private final MessageService messageService;

    public LibraryServiceImpl(AuthorRepository authorRepository,
                              GenreRepository genreRepository,
                              BookRepository bookRepository,
                              CommentRepository commentRepository,
                              IOService ioService,
                              MessageService messageService) {
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
        this.ioService = ioService;
        this.messageService = messageService;
        this.commentRepository = commentRepository;
    }

    @Override
    public void printAllGenres() {
        try {
            genreRepository.findAll().forEach(it -> ioService.outputData(
                    messageService.getMessage("genre.info", it.getId(), it.getGenreName())
            ));
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("genre.info.error",  e.getMessage()), e);
        }
    }

    private void printBooks(List<Book> books) {
        books.forEach(it -> ioService.outputData(
                messageService.getMessage("book.info",
                        it.getIsbn(), it.getName(), it.authorFullName(), it.genreName())));
    }

    @Override
    public void printAllBooks() {
        try {
            printBooks(bookRepository.findAll());
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("book.info.error", e.getMessage()), e);
        }
    }

    @Override
    public void printAllAuthors() {
        try {
            authorRepository.findAll().forEach(it -> ioService.outputData(
                    messageService.getMessage("author.info",
                            it.getId(), it.fullName(), it.getEmail(), it.getPhoneNumber())));
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("author.info.error", e.getMessage()), e);
        }
    }

    @Override
    public void findBookByISBNAndPrint(String isbn) {
        try {
            Book book = bookRepository.findById(isbn)
                    .orElseThrow(()-> new LibraryException(messageService.getMessage("book.id.not.found", isbn)));
            ioService.outputData(
                    messageService.getMessage("book.info",
                            book.getIsbn(), book.getName(), book.authorFullName(), book.genreName())
            );
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("book.id.not.found.error.cause",
                    isbn, e.getMessage()), e);
        }
    }

    @Override
    public void findBookByNameAndPrint(String name) {
        try {
            List<Book> books = bookRepository.findByNameLike(name);
            if (books.isEmpty()) {
                ioService.outputData(messageService.getMessage("book.name.not.found",  name));
            }
            else {
                printBooks(books);
            }
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("book.name.not.found.error.reason",
                    name, e.getMessage()), e);
        }
    }

    @Override
    public void findBookByAuthorAndPrint(String firstName, String lastName) {
        try {
            Author author = authorRepository.findByFirstNameAndLastName(firstName, lastName)
                    .orElseThrow(() -> new LibraryException(messageService.getMessage("author.absent",
                            firstName, lastName)));
            List<Book> books = bookRepository.findByAuthor(author);
            if (books.isEmpty()) {
                ioService.outputData(messageService.getMessage("book.author.not.found",
                        firstName, lastName));
            }
            else {
                printBooks(books);
            }
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("book.author.not.found.error.reason",
                    firstName, lastName, e.getMessage()), e);
        }
    }

    @Override
    public void findBookByGenreAndPrint(String genreName) {
        try {
            Genre genre = genreRepository.findByGenreName(genreName)
                    .orElseThrow(() -> new LibraryException(messageService.getMessage("genre.absent",
                            genreName)));
            List<Book> books = bookRepository.findByGenre(genre);
            if (books.isEmpty()) {
                ioService.outputData(messageService.getMessage("book.genre.not.found", genreName));
            }
            else {
                printBooks(books);
            }
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("book.genre.not.found.error.reason",
                    genreName, e.getMessage()), e);
        }
    }

    @Override
    public void addNewBook(Book book) {
        try {
            bookRepository.save(book);
            ioService.outputData(messageService.getMessage("book.added", book.getIsbn()));
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("book.add.error",
                    book.getName(), e.getMessage()), e);
        }
    }

    @Override
    public void addNewAuthor(Author author) {
        try {
            authorRepository.save(author);
            ioService.outputData(messageService.getMessage("author.added", author.fullName()));
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("author.added.error",
                    author.fullName(), e.getMessage()), e);
        }
    }

    @Override
    public void addNewGenre(Genre genre) {
        try {
            genreRepository.save(genre);
            ioService.outputData(messageService.getMessage("genre.added", genre.getGenreName()));
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("genre.added.error",
                    genre.getGenreName(), e.getMessage()), e);
        }
    }

    @Override
    public void deleteBook(String isbn) {
        try {
            bookRepository.deleteById(isbn);
            ioService.outputData(messageService.getMessage("book.deleted", isbn));
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("book.deleted.error",
                    isbn, e.getMessage()), e);
        }
    }

    @Override
    public void deleteAuthor(String id) {
        try {
            authorRepository.deleteById(id);
            ioService.outputData(messageService.getMessage("author.deleted", id));
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("author.deleted.error",
                    id, e.getMessage()), e);
        }
    }

    @Override
    public void deleteGenre(String id) {
        try {
            genreRepository.deleteById(id);
            ioService.outputData(messageService.getMessage("genre.deleted", id));
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("genre.deleted.error",
                    id, e.getMessage()), e);
        }
    }

    @Override
    public void addBookComment(String isbn, String comment) {
        try {
            Book book = bookRepository.findById(isbn)
                    .orElseThrow(()-> new LibraryException(messageService.getMessage("book.id.not.found", isbn)));
            Comment newComment = new Comment("", comment, book);
            commentRepository.save(newComment);
            ioService.outputData(messageService.getMessage("add.comment", isbn));
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("add.comment.error",
                    isbn, e.getMessage()), e);
        }
    }

    @Override
    public void findCommentByBookIdAndPrint(String isbn) {
        try {
            List<Comment> comments = commentRepository.getCommentsOfBook(isbn);
            if (comments.isEmpty()) {
                ioService.outputData(messageService.getMessage("book.comment.not.found", isbn));
            }
            else {
                Book book = comments.get(0).getBook();
                String commentsStr = comments.stream().map(Comment::getComment).collect(Collectors.joining("\n"));
                ioService.outputData(messageService.getMessage("comment.info",
                        book.getIsbn(), book.getName(), commentsStr));
            }
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("book.comment.not.found.error.reason",
                    isbn, e.getMessage()), e);
        }
    }

    @Override
    public Author findAuthorById(String id){
        return authorRepository.findById(id)
                .orElseThrow(() -> new LibraryException(messageService.getMessage("author.not.exist", id)));
    }

    @Override
    public Genre findGenreById(String id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new LibraryException(messageService.getMessage("genre.not.exist", id)));
    }
}


