package ru.otus.avalieva.library.orm.jpa.service.impl;


import org.springframework.stereotype.Service;
import ru.otus.avalieva.library.orm.jpa.domain.Author;
import ru.otus.avalieva.library.orm.jpa.domain.Book;
import ru.otus.avalieva.library.orm.jpa.domain.Comment;
import ru.otus.avalieva.library.orm.jpa.domain.Genre;
import ru.otus.avalieva.library.orm.jpa.repository.AuthorRepository;
import ru.otus.avalieva.library.orm.jpa.repository.BookRepository;
import ru.otus.avalieva.library.orm.jpa.repository.CommentRepository;
import ru.otus.avalieva.library.orm.jpa.repository.GenreRepository;
import ru.otus.avalieva.library.orm.jpa.service.IOService;
import ru.otus.avalieva.library.orm.jpa.service.LibraryService;
import ru.otus.avalieva.library.orm.jpa.service.MessageService;

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
            genreRepository.getAll().forEach(it -> ioService.outputData(
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
            printBooks(bookRepository.getAll());
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("book.info.error", e.getMessage()), e);
        }
    }

    @Override
    public void printAllAuthors() {
        try {
            authorRepository.getAll().forEach(it -> ioService.outputData(
                    messageService.getMessage("author.info",
                            it.getId(), it.fullName(), it.getEmail(), it.getPhoneNumber())));
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("author.info.error", e.getMessage()), e);
        }
    }

    @Override
    public void findBookByISBNAndPrint(long isbn) {
        try {
            Book book = bookRepository.findByIsbn(isbn)
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
            List<Book> books = bookRepository.findByName(name);
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
            List<Book> books = bookRepository.findByAuthor(firstName, lastName);
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
    public void findBookByGenreAndPrint(String genre) {
        try {
            List<Book> books = bookRepository.findByGenre(genre);
            if (books.isEmpty()) {
                ioService.outputData(messageService.getMessage("book.genre.not.found", genre));
            }
            else {
                printBooks(books);
            }
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("book.genre.not.found.error.reason",
                    genre, e.getMessage()), e);
        }
    }

    private void checkGenreExistence(long id) {
        if(!genreRepository.findById(id).isPresent()) {
            throw new LibraryException(messageService.getMessage("genre.not.exist", id));
        }
    }

    private void checkAuthorExistence(long id) {
        if(!authorRepository.findById(id).isPresent()) {
            throw new LibraryException(messageService.getMessage("author.not.exist",id));
        }
    }

    @Override
    public void addNewBook(Book book) {
        try {
            checkGenreExistence(book.getGenre().getId());
            checkAuthorExistence(book.getAuthor().getId());
            bookRepository.add(book);
            ioService.outputData(messageService.getMessage("book.added",book.getIsbn()));
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("book.add.error",
                    book.getName(), e.getMessage()), e);
        }
    }

    @Override
    public void addNewAuthor(Author author) {
        try {
            authorRepository.add(author);
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
            genreRepository.add(genre);
            ioService.outputData(messageService.getMessage("genre.added", genre.getGenreName()));
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("genre.added.error",
                    genre.getGenreName(), e.getMessage()), e);
        }
    }

    @Override
    public void deleteBook(long isbn) {
        try {
            bookRepository.deleteById(isbn);
            ioService.outputData(messageService.getMessage("book.deleted", isbn));
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("book.deleted.error",
                    isbn, e.getMessage()), e);
        }
    }

    private Author getAuthor(long authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new LibraryException(messageService.getMessage("author.not.exist", authorId)));

    }

    private void checkBookAbsenceForAuthor(long authorId) {
        Author author = getAuthor(authorId);
        List<Book> books = bookRepository.findByAuthor(author.getFirstName(), author.getLastName());
        if (!books.isEmpty()) {
            throw new LibraryException(messageService.getMessage("book.for.author.exist",
                    author.fullName(), books.stream().map(Book::getName).collect(Collectors.joining(", "))));
        }
    }

    @Override
    public void deleteAuthor(long id) {
        try {
            checkBookAbsenceForAuthor(id);
            authorRepository.deleteById(id);
            ioService.outputData(messageService.getMessage("author.deleted", id));
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("author.deleted.error",
                    id, e.getMessage()), e);
        }
    }

    private Genre getGenre(long genreId) {
        return genreRepository.findById(genreId)
                .orElseThrow(() -> new LibraryException(messageService.getMessage("genre.not.exist", genreId)));
    }

    private void checkBookAbsenceForGenre(long genreId) {
        Genre genre = getGenre(genreId);
        List<Book> books = bookRepository.findByGenre(genre.getGenreName());
        if (!books.isEmpty()) {
            throw new LibraryException(messageService.getMessage("book.for.genre.exist",
                    genre.getGenreName(), books.stream().map(Book::getName).collect(Collectors.joining(", "))));
        }
    }

    @Override
    public void deleteGenre(long id) {
        try {
            checkBookAbsenceForGenre(id);
            genreRepository.delete(id);
            ioService.outputData(messageService.getMessage("genre.deleted", id));
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("genre.deleted.error",
                    id, e.getMessage()), e);
        }
    }

    @Override
    public void addBookComment(long isbn, String comment) {
        try {
            Book book = bookRepository.findByIsbn(isbn)
                    .orElseThrow(()-> new LibraryException(messageService.getMessage("book.id.not.found", isbn)));
            Comment newComment = new Comment(0, comment, book);
            commentRepository.add(newComment);
            ioService.outputData(messageService.getMessage("add.comment", isbn));
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("add.comment.error",
                    isbn, e.getMessage()), e);
        }
    }

    @Override
    public void findCommentByBookIdAndPrint(long isbn) {
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
}
