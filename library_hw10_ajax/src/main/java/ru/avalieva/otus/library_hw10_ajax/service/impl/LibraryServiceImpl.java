package ru.avalieva.otus.library_hw10_ajax.service.impl;


import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Service;
import ru.avalieva.otus.library_hw10_ajax.domain.Author;
import ru.avalieva.otus.library_hw10_ajax.domain.Book;
import ru.avalieva.otus.library_hw10_ajax.domain.Comment;
import ru.avalieva.otus.library_hw10_ajax.domain.Genre;
import ru.avalieva.otus.library_hw10_ajax.repository.AuthorRepository;
import ru.avalieva.otus.library_hw10_ajax.repository.BookRepository;
import ru.avalieva.otus.library_hw10_ajax.repository.CommentRepository;
import ru.avalieva.otus.library_hw10_ajax.repository.GenreRepository;
import ru.avalieva.otus.library_hw10_ajax.service.LibraryService;
import ru.avalieva.otus.library_hw10_ajax.service.MessageService;

import java.util.List;

@Service
public class LibraryServiceImpl implements LibraryService {

    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;
    private final MessageService messageService;

    public LibraryServiceImpl(AuthorRepository authorRepository,
                              GenreRepository genreRepository,
                              BookRepository bookRepository,
                              CommentRepository commentRepository,
                              MessageService messageService) {
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
        this.messageService = messageService;
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Genre> allGenres() {
        try {
            return genreRepository.findAll();
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("genre.info.error",  e.getMessage()), e);
        }
    }

    @Override
    public List<Book> allBooks() {
        try {
            return bookRepository.findAll();
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("book.info.error", e.getMessage()), e);
        }
    }

    @Override
    public List<Author> allAuthors() {
        try {
            return authorRepository.findAll();
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("author.info.error", e.getMessage()), e);
        }
    }

    @Override
    public Book findBookByISBN(long isbn) {
        try {
            return bookRepository.findById(isbn)
                    .orElseThrow(()-> new LibraryException(messageService.getMessage("book.id.not.found", isbn)));

        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("book.id.not.found.error.cause",
                    isbn, e.getMessage()), e);
        }
    }

    @Override
    public Author findAuthorByID(long id) {
        try {
            return authorRepository.findById(id)
                    .orElseThrow(()-> new LibraryException(messageService.getMessage("author.id.not.found", id)));

        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("author.id.not.found.error.cause",
                    id, e.getMessage()), e);
        }
    }

    @Override
    public Genre findGenreByID(long id) {
        try {
            return genreRepository.findById(id)
                    .orElseThrow(()-> new LibraryException(messageService.getMessage("genre.id.not.found", id)));

        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("genre.id.not.found.error.cause",
                    id, e.getMessage()), e);
        }
    }

    @Override
    public List<Book> findBookByName(String name) {
        try {
            return bookRepository.findByNameLike(name);
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("book.name.not.found.error.reason",
                    name, e.getMessage()), e);
        }
    }


    @Override
    public void addNewBook(Book book) {
        try {
            bookRepository.save(book);
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("book.add.error",
                    book.getName(), e.getMessage()), e);
        }
    }

    @Override
    public void deleteBook(long isbn) {
        try {
            bookRepository.deleteById(isbn);
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("book.deleted.error",
                    isbn, e.getMessage()), e);
        }
    }

    @Override
    public void addBookComment(long isbn, String comment) {
        try {
            Book book = bookRepository.findById(isbn)
                    .orElseThrow(()-> new LibraryException(messageService.getMessage("book.id.not.found", isbn)));
            Comment newComment = new Comment(0, comment, book);
            commentRepository.save(newComment);
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("add.comment.error",
                    isbn, e.getMessage()), e);
        }
    }

    @Override
    public void deleteComment(long commentId) {
        try {
            commentRepository.deleteById(commentId);
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("drop.comment.error",
                    commentId, e.getMessage()), e);
        }
    }

    @Override
    public List<Comment> findCommentByBookId(long isbn) {
        try {
            return commentRepository.getCommentsOfBook(isbn);
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("book.comment.not.found.error.reason",
                    isbn, e.getMessage()), e);
        }
    }
}

