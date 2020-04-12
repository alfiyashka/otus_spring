package ru.avalieva.otus.hw18.library.hystrix.service.impl;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import ru.avalieva.otus.hw18.library.hystrix.domain.Author;
import ru.avalieva.otus.hw18.library.hystrix.domain.Book;
import ru.avalieva.otus.hw18.library.hystrix.domain.Comment;
import ru.avalieva.otus.hw18.library.hystrix.domain.Genre;
import ru.avalieva.otus.hw18.library.hystrix.repository.AuthorRepository;
import ru.avalieva.otus.hw18.library.hystrix.repository.BookRepository;
import ru.avalieva.otus.hw18.library.hystrix.repository.CommentRepository;
import ru.avalieva.otus.hw18.library.hystrix.repository.GenreRepository;
import ru.avalieva.otus.hw18.library.hystrix.service.LibraryService;
import ru.avalieva.otus.hw18.library.hystrix.service.MessageService;

import java.util.ArrayList;
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

    @HystrixCommand(
            commandKey = "getGenresFromDB",
            fallbackMethod = "getEmptyGenreList",
            ignoreExceptions = { LibraryException.class },
            commandProperties = { @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000") }
    )
    @Override
    public List<Genre> allGenres() {
        try {
            return genreRepository.findAll();
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("genre.info.error",  e.getMessage()), e);
        }
    }

    public List<Genre> getEmptyGenreList() {
        return new ArrayList<>();
    }

    @HystrixCommand(
            commandKey = "getBooksFromDB",
            fallbackMethod = "getEmptyBookList",
            ignoreExceptions = { LibraryException.class },
            commandProperties = { @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000") })
    @Override
    public List<Book> allBooks() {
        try {
            return bookRepository.findAll();
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("book.info.error", e.getMessage()), e);
        }
    }

    public List<Book> getEmptyBookList() {
        return new ArrayList<>();
    }

    @HystrixCommand(
            commandKey = "getAuthorsFromDB",
            fallbackMethod = "getEmptyAuthorList",
            ignoreExceptions = { LibraryException.class },
            commandProperties = { @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000") })
    @Override
    public List<Author> allAuthors() {
        try {
            return authorRepository.findAll();
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("author.info.error", e.getMessage()), e);
        }
    }

    public List<Author> getEmptyAuthorList() {
        return new ArrayList<>();
    }

    @HystrixCommand(
            commandKey = "findBookByISBN",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
            }
    )
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

    @HystrixCommand(
            commandKey = "findAuthorByID",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
            }
    )
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

    @HystrixCommand(
            commandKey = "findGenreByID",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
            }
    )
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


    @HystrixCommand(
            commandKey = "getBookByNameFromDB",
            fallbackMethod = "getEmptyBookByNameList",
            ignoreExceptions = { LibraryException.class },
            commandProperties = { @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000") })
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

    public List<Book> getEmptyBookByNameList(String name) {
       return new ArrayList<>();
    }

    @HystrixCommand(
            commandKey = "addBook",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
            }
    )
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

    @HystrixCommand(
            commandKey = "deleteBook",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
            }
    )
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


    @HystrixCommand(
            commandKey = "addComment",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
            }
    )
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


    @HystrixCommand(
            commandKey = "deleteComment",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
            }
    )
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


    @HystrixCommand(
            commandKey = "getCommentsFromDB",
            fallbackMethod = "getEmptyCommentList",
            ignoreExceptions = { LibraryException.class },
            commandProperties = { @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000") })
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

    public List<Comment> getEmptyCommentList(long isbn) {
        return new ArrayList<>();
    }
}




