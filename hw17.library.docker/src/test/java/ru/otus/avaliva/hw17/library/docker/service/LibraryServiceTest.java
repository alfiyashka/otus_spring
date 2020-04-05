package ru.otus.avaliva.hw17.library.docker.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.avaliva.hw17.library.docker.domain.Author;
import ru.otus.avaliva.hw17.library.docker.domain.Book;
import ru.otus.avaliva.hw17.library.docker.domain.Comment;
import ru.otus.avaliva.hw17.library.docker.domain.Genre;
import ru.otus.avaliva.hw17.library.docker.repository.AuthorRepository;
import ru.otus.avaliva.hw17.library.docker.repository.BookRepository;
import ru.otus.avaliva.hw17.library.docker.repository.CommentRepository;
import ru.otus.avaliva.hw17.library.docker.repository.GenreRepository;
import ru.otus.avaliva.hw17.library.docker.service.impl.LibraryException;
import ru.otus.avaliva.hw17.library.docker.service.impl.LibraryServiceImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ContextConfiguration(classes = LibraryServiceImpl.class)
@SpringBootTest
public class LibraryServiceTest {

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private MessageService messageService;

    @Autowired
    private LibraryService libraryService;

    @Test
    public void allGenresTest() {
        Genre genre = new Genre(0,  "GENRE");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);
        when(genreRepository.findAll()).thenReturn(genres);

        List<Genre> result = libraryService.allGenres();
        Assertions.assertEquals(genres, result);

        verify(genreRepository, times(1)).findAll();
    }

    @Test
    public void allGenresTestFailed() {
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("error"));
        when(genreRepository.findAll()).thenThrow(exception);
        when(messageService.getMessage("genre.info.error", exception.getMessage())).thenReturn("Error");

        LibraryException error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.allGenres(); });
        Assertions.assertEquals("Error", error.getMessage());


        verify(genreRepository, times(1)).findAll();
        verify(messageService, times(1)).getMessage("genre.info.error", exception.getMessage());
    }

    @Test
    public void allBooksTest() {
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "Book 1", 2019, new Author(), new Genre()));

        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = libraryService.allBooks();
        Assertions.assertEquals(books, result);

        verify(bookRepository, times(1)).findAll();
    }

    @Test
    public void allBooksTestFailed() {
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("error"));
        when(bookRepository.findAll()).thenThrow(exception);
        when(messageService.getMessage("book.info.error", exception.getMessage())).thenReturn("Error");

        LibraryException error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.allBooks(); });
        Assertions.assertEquals("Error", error.getMessage());

        verify(bookRepository, times(1)).findAll();
        verify(messageService, times(1)).getMessage("book.info.error", exception.getMessage());
    }

    @Test
    public void allAuthorsTest() {
        List<Author> authors = new ArrayList<>();
        authors.add(new Author(1L, "Mary", "Panak", "23432435", "mary@mail.com"));

        when(authorRepository.findAll()).thenReturn(authors);

        List<Author> result = libraryService.allAuthors();
        Assertions.assertEquals(authors, result);

        verify(authorRepository, times(1)).findAll();
    }

    @Test
    public void allAuthorsTestFailed() {
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("error"));
        when(authorRepository.findAll()).thenThrow(exception);
        when(messageService.getMessage("author.info.error", exception.getMessage())).thenReturn("Error");

        LibraryException error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.allAuthors(); });
        Assertions.assertEquals("Error", error.getMessage());


        verify(authorRepository, times(1)).findAll();
        verify(messageService, times(1)).getMessage("author.info.error", exception.getMessage());
    }

    @Test
    public void findBookByISBNTest() {
        Optional<Book> book = Optional.of(new Book(1L, "Book 1", 2019, new Author(), new Genre()));
        when(bookRepository.findById(1L)).thenReturn(book);

        libraryService.findBookByISBN(1);

        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    public void findBookByISBNTestFailed() {
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("error"));
        when(bookRepository.findById(1L)).thenThrow(exception);
        when(messageService.getMessage("book.id.not.found.error.cause",
                1L, exception.getMessage())).thenReturn("Error");

        LibraryException error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.findBookByISBN(1L); });
        Assertions.assertEquals("Error", error.getMessage());

        verify(bookRepository, times(1)).findById(1L);
        verify(messageService, times(1))
                .getMessage("book.id.not.found.error.cause",1L, exception.getMessage());
    }


    @Test
    public void findBookByISBNTestNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.ofNullable(null));
        when(messageService.getMessage("book.id.not.found",1L)).thenReturn("Not Found");

        when(messageService.getMessage("book.id.not.found.error.cause",
                1L, "Not Found")).thenReturn("Error");

        LibraryException error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.findBookByISBN(1L); });
        Assertions.assertEquals("Error", error.getMessage());

        verify(bookRepository, times(1)).findById(1L);
        verify(messageService, times(1)).getMessage("book.id.not.found",1L);
        verify(messageService, times(1))
                .getMessage("book.id.not.found.error.cause",1L, "Not Found");
    }

    @Test
    public void findBookByNameTest() {
        String bookName = "BOOK 1";
        Book book = new Book(1L, bookName, 2019, new Author(), new Genre());
        List<Book> books = new ArrayList<>();
        books.add(book);
        when(bookRepository.findByNameLike(bookName)).thenReturn(books);

        List<Book> result = libraryService.findBookByName(bookName);
        Assertions.assertEquals(books, result);

        verify(bookRepository, times(1)).findByNameLike(bookName);
    }


    @Test
    public void findBookByNameTestFailed() {
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("error"));
        String bookName = "BOOK 1";
        when(bookRepository.findByNameLike(bookName)).thenThrow(exception);
        when(messageService.getMessage("book.name.not.found.error.reason",
                bookName, exception.getMessage())).thenReturn("Error");

        LibraryException error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.findBookByName(bookName); });
        Assertions.assertEquals("Error", error.getMessage());

        verify(bookRepository, times(1)).findByNameLike(bookName);
        verify(messageService, times(1))
                .getMessage("book.name.not.found.error.reason",bookName, exception.getMessage());
    }

    @Test
    public void findGenreByIdTest() {
        Genre genre = new Genre(1L, "Genre");
        when(genreRepository.findById(genre.getId())).thenReturn(Optional.of(genre));

        Genre result = libraryService.findGenreByID(genre.getId());
        Assertions.assertEquals(genre, result);

        verify(genreRepository, times(1)).findById(genre.getId());
    }

    @Test
    public void findGenreByIdTestFailed() {
        long id = 1;
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("error"));
        when(genreRepository.findById(id)).thenThrow(exception);
        when(messageService.getMessage("genre.id.not.found.error.cause",
                id, exception.getMessage())).thenReturn("Error");

        LibraryException error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.findGenreByID(id); });
        Assertions.assertEquals("Error", error.getMessage());

        verify(genreRepository, times(1)).findById(id);
        verify(messageService, times(1))
                .getMessage("genre.id.not.found.error.cause", id, exception.getMessage());
    }


    @Test
    public void findGenreByIdTestFailedNoFound() {
        long id = 1;
        when(genreRepository.findById(id)).thenReturn(Optional.ofNullable(null));
        when(messageService.getMessage("genre.id.not.found",
                id)).thenReturn("No found");
        when(messageService.getMessage("genre.id.not.found.error.cause",
                id, "No found")).thenReturn("Error");

        LibraryException error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.findGenreByID(id); });
        Assertions.assertEquals("Error", error.getMessage());

        verify(genreRepository, times(1)).findById(id);
        verify(messageService, times(1)).getMessage("genre.id.not.found", id);
        verify(messageService, times(1))
                .getMessage("genre.id.not.found.error.cause", id, "No found");
    }


    @Test
    public void findAuthorByIdTest() {
        Author author = new Author(1L, "Mary", "Panak", "23432435", "mary@mail.com");
        when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));

        Author result = libraryService.findAuthorByID(author.getId());
        Assertions.assertEquals(author, result);

        verify(authorRepository, times(1)).findById(author.getId());
    }

    @Test
    public void findAuthorByIdTestFailed() {
        long id = 1;
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("error"));
        when(authorRepository.findById(id)).thenThrow(exception);
        when(messageService.getMessage("author.id.not.found.error.cause",
                id, exception.getMessage())).thenReturn("Error");

        LibraryException error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.findAuthorByID(id); });
        Assertions.assertEquals("Error", error.getMessage());

        verify(authorRepository, times(1)).findById(id);
        verify(messageService, times(1))
                .getMessage("author.id.not.found.error.cause", id, exception.getMessage());
    }


    @Test
    public void findAuthorByIdTestFailedNoFound() {
        long id = 1;
        when(authorRepository.findById(id)).thenReturn(Optional.ofNullable(null));
        when(messageService.getMessage("author.id.not.found",
                id)).thenReturn("No found");
        when(messageService.getMessage("author.id.not.found.error.cause",
                id, "No found")).thenReturn("Error");

        LibraryException error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.findAuthorByID(id); });
        Assertions.assertEquals("Error", error.getMessage());

        verify(authorRepository, times(1)).findById(id);
        verify(messageService, times(1)).getMessage("author.id.not.found", id);
        verify(messageService, times(1))
                .getMessage("author.id.not.found.error.cause", id, "No found");
    }


    @Test
    public void addNewBookTest() {
        Book book = new Book(1L, "BOOK 1", 2019, new Author(), new Genre());
        when(bookRepository.save(book)).thenReturn(book);

        libraryService.addNewBook(book);

        verify(bookRepository, times(1)).save(book);

    }

    @Test
    public void addNewBookTestFailed() {
        Book book = new Book(1L, "BOOK 1", 2019, new Author(), new Genre());
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("error"));
        doThrow(exception).when(bookRepository).save(book);
        when(messageService.getMessage("book.add.error", book.getName(), exception.getMessage()))
                .thenReturn("Book cannot be added");

        LibraryException error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.addNewBook(book); });
        Assertions.assertEquals("Book cannot be added", error.getMessage());


        verify(bookRepository, times(1)).save(book);
        verify(messageService, times(1))
                .getMessage("book.add.error", book.getName(), exception.getMessage());
    }

    @Test
    public void deleteBookTest() {
        long isbn = 1L;
        doNothing().when(bookRepository).deleteById(isbn);

        libraryService.deleteBook(isbn);

        verify(bookRepository, times(1)).deleteById(isbn);
    }

    @Test
    public void deleteBookTestFailed() {
        long isbn = 1L;
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("error"));
        doThrow(exception).when(bookRepository).deleteById(isbn);
        when(messageService.getMessage("book.deleted.error", isbn, exception.getMessage()))
                .thenReturn("Book cannot be deleted");

        LibraryException error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.deleteBook(isbn); });
        Assertions.assertEquals("Book cannot be deleted", error.getMessage());

        verify(bookRepository, times(1)).deleteById(isbn);
        verify(messageService, times(1))
                .getMessage("book.deleted.error", isbn, exception.getMessage());
    }

    @Test
    public void deleteCommentTest() {
        long commentId = 1L;
        doNothing().when(commentRepository).deleteById(commentId);

        libraryService.deleteComment(commentId);

        verify(commentRepository, times(1)).deleteById(commentId);
    }


    @Test
    public void deleteCommentTestFailed() {
        long commentId = 1L;
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("error"));
        doThrow(exception).when(commentRepository).deleteById(commentId);
        when(messageService.getMessage("drop.comment.error", commentId, exception.getMessage()))
                .thenReturn("Book cannot be deleted");

        LibraryException error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.deleteComment(commentId); });
        Assertions.assertEquals("Book cannot be deleted", error.getMessage());

        verify(commentRepository, times(1)).deleteById(commentId);
        verify(messageService, times(1))
                .getMessage("drop.comment.error", commentId, exception.getMessage());
    }


    @Test
    public void addCommentTest() {
        final long ISBN = 1L;
        final String COMMENT = "Comment";
        Optional<Book> book = Optional.of(new Book(ISBN, "Book 1", 2019, new Author(), new Genre()));
        when(bookRepository.findById(ISBN)).thenReturn(book);
        when(commentRepository.save(any(Comment.class))).thenReturn(new Comment(1L, COMMENT, book.get()));

        libraryService.addBookComment(ISBN, COMMENT);

        verify(bookRepository, times(1)).findById(ISBN);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    public void addCommentTestFailedNoBook() {
        final long ISBN = 1L;
        final String COMMENT = "Comment";
        when(bookRepository.findById(ISBN)).thenReturn(Optional.ofNullable(null));
        when(messageService.getMessage("book.id.not.found", ISBN)).thenReturn("Not found");
        when(messageService.getMessage("add.comment.error", ISBN, "Not found")).thenReturn("Error");

        LibraryException error = Assertions.assertThrows(LibraryException.class, () -> {
            libraryService.addBookComment(ISBN, COMMENT);
        });
        Assertions.assertEquals("Error", error.getMessage());

        verify(bookRepository, times(1)).findById(ISBN);
        verify(commentRepository, never()).save(any(Comment.class));
        verify(messageService, times(1)).getMessage("book.id.not.found", ISBN);
        verify(messageService, times(1)).getMessage("add.comment.error", ISBN, "Not found");
    }

    @Test
    public void addCommentTestFailedSomeError() {
        final long ISBN = 1L;
        final String COMMENT = "Comment";
        Optional<Book> book = Optional.of(new Book(ISBN, "Book 1", 2019, new Author(), new Genre()));
        when(bookRepository.findById(ISBN)).thenReturn(book);
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("sql error"));
        doThrow(exception).when(commentRepository).save(any(Comment.class));
        when(messageService.getMessage("add.comment.error", ISBN,
                exception.getMessage())).thenReturn("Error");

        LibraryException error = Assertions.assertThrows(LibraryException.class, () -> {
            libraryService.addBookComment(ISBN, COMMENT);
        });
        Assertions.assertEquals("Error", error.getMessage());

        verify(bookRepository, times(1)).findById(ISBN);
        verify(commentRepository, times(1)).save(any(Comment.class));
        verify(messageService, times(1)).getMessage("add.comment.error",
                ISBN, exception.getMessage());
    }

    @Test
    public void findCommentByBookTest() {
        final long ISBN = 1L;
        Book book = new Book(ISBN, "Book 1", 2019, new Author(), new Genre());
        Comment comment = new Comment(1, "Comment", book);
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);

        when(commentRepository.getCommentsOfBook(ISBN)).thenReturn(comments);

        List<Comment> result = libraryService.findCommentByBookId(ISBN);
        Assertions.assertEquals(comments, result);

        verify(commentRepository, times(1)).getCommentsOfBook(ISBN);
    }

    @DisplayName("find comment by book isbn test some error")
    @Test
    public void findCommentByBookTestSomeError() {
        final long ISBN = 1L;
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("sql error"));
        doThrow(exception).when(commentRepository).getCommentsOfBook(ISBN);
        when(messageService.getMessage("book.comment.not.found.error.reason", ISBN, exception.getMessage()))
                .thenReturn("Error");
        LibraryException error = Assertions.assertThrows(LibraryException.class, () -> {
            libraryService.findCommentByBookId(ISBN);
        });
        Assertions.assertEquals("Error", error.getMessage());

        verify(commentRepository, times(1)).getCommentsOfBook(ISBN);
        verify(messageService, times(1))
                .getMessage("book.comment.not.found.error.reason", ISBN, exception.getMessage());
    }

}



