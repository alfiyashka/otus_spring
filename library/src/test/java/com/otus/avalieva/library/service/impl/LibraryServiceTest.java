package com.otus.avalieva.library.service.impl;

import com.otus.avalieva.library.LibraryApplication;
import com.otus.avalieva.library.dao.AuthorDao;
import com.otus.avalieva.library.dao.BookDao;
import com.otus.avalieva.library.dao.GenreDao;
import com.otus.avalieva.library.domain.Author;
import com.otus.avalieva.library.domain.Book;
import com.otus.avalieva.library.domain.Genre;
import com.otus.avalieva.library.service.IOService;
import com.otus.avalieva.library.service.LibraryService;
import com.otus.avalieva.library.service.MessageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.test.context.ContextConfiguration;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ContextConfiguration(classes = LibraryApplication.class)
@SpringBootTest
public class LibraryServiceTest {

    @MockBean
    private AuthorDao authorDao;

    @MockBean
    private GenreDao genreDao;

    @MockBean
    private BookDao bookDao;

    @MockBean
    private IOService ioService;

    @MockBean
    private MessageService messageService;

    @Autowired
    private LibraryService libraryService;

    @DisplayName("print all genres test")
    @Test
    public void printAllGenresTest() {
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1L, "GENRE"));
        genres.add(new Genre(2L, "GENRE2"));
        when(genreDao.allGenres()).thenReturn(genres);

        for (Genre genre: genres) {
            when(messageService.getMessage("genre.info", genre.getId(), genre.getGenreName()))
                    .thenReturn("Genre Info");
            doNothing().when(ioService).outputData("Genre Info");
        }
        libraryService.printAllGenres();

        verify(genreDao, times(1)).allGenres();
        for (Genre genre: genres) {
            verify(messageService, times(1))
                    .getMessage("genre.info", genre.getId(), genre.getGenreName());
        }
        verify(ioService, times(genres.size())).outputData("Genre Info");
    }

    @DisplayName("print all genres test failed")
    @Test
    public void printAllGenresTestFailed() {
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("error"));
        when(genreDao.allGenres()).thenThrow(exception);
        when(messageService.getMessage("genre.info.error", exception.getMessage())).thenReturn("Error");

        var error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.printAllGenres(); });
        Assertions.assertEquals("Error", error.getMessage());

        verify(genreDao, times(1)).allGenres();
        verify(messageService, times(1)).getMessage("genre.info.error", exception.getMessage());
    }

    @DisplayName("print all books test")
    @Test
    public void printAllBooksTest() {
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "Book 1", 2019, new Author(), new Genre()));
        books.add(new Book(2L, "Book 2", 2019, new Author(), new Genre()));
        when(bookDao.allBooks()).thenReturn(books);

        for (Book book: books) {
            when(messageService.getMessage("book.info",
                    book.getIsbn(), book.getName(), book.authorFullName(), book.genreName()))
                    .thenReturn("Book Info");
            doNothing().when(ioService).outputData("Book Info");
        }
        libraryService.printAllBooks();

        verify(bookDao, times(1)).allBooks();
        for (Book book: books) {
            verify(messageService, times(1)).getMessage("book.info",
                    book.getIsbn(), book.getName(), book.authorFullName(), book.genreName());
        }
        verify(ioService, times(books.size())).outputData("Book Info");
    }

    @DisplayName("print all books test failed")
    @Test
    public void printAllBooksTestFailed() {
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("error"));
        when(bookDao.allBooks()).thenThrow(exception);
        when(messageService.getMessage("book.info.error", exception.getMessage())).thenReturn("Error");

        var error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.printAllBooks(); });
        Assertions.assertEquals("Error", error.getMessage());

        verify(bookDao, times(1)).allBooks();
        verify(messageService, times(1)).getMessage("book.info.error", exception.getMessage());
    }

    @DisplayName("print all authors test")
    @Test
    public void printAllAuthorTest() {
        List<Author> authors = new ArrayList<>();
        authors.add(new Author(1L, "Mary", "Panak", "23432435", "mary@mail.com"));
        authors.add(new Author(1L, "Mark", "Smith", "11111", "mark@mail.com"));
        when(authorDao.allAuthors()).thenReturn(authors);

        for (Author author: authors) {
            when(messageService.getMessage("author.info",
                    author.getId(), author.fullName(), author.getEmail(), author.getPhoneNumber()))
                    .thenReturn("Author Info");
            doNothing().when(ioService).outputData("Author Info");
        }
        libraryService.printAllAuthors();

        verify(authorDao, times(1)).allAuthors();
        for (Author author: authors) {
            verify(messageService, times(1)).getMessage("author.info",
                    author.getId(), author.fullName(), author.getEmail(), author.getPhoneNumber());
        }
        verify(ioService, times(authors.size())).outputData("Author Info");
    }

    @DisplayName("print all authors test failed")
    @Test
    public void printAllAuthorsTestFailed() {
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("error"));
        when(authorDao.allAuthors()).thenThrow(exception);
        when(messageService.getMessage("author.info.error", exception.getMessage())).thenReturn("Error");

        var error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.printAllAuthors(); });
        Assertions.assertEquals("Error", error.getMessage());

        verify(authorDao, times(1)).allAuthors();
        verify(messageService, times(1)).getMessage("author.info.error", exception.getMessage());
    }


    @DisplayName("find book by isbn test")
    @Test
    public void findBookByISBNAndPrintTest() {
        Book book = new Book(1L, "Book 1", 2019, new Author(), new Genre());
        when(bookDao.findBookByIsbn(1L)).thenReturn(book);
        when(messageService.getMessage("book.info",
                book.getIsbn(), book.getName(), book.authorFullName(), book.genreName())).thenReturn("Book Info");
        doNothing().when(ioService).outputData("Book Info");

        libraryService.findBookByISBNAndPrint(1);

        verify(bookDao, times(1)).findBookByIsbn(1L);
        verify(messageService, times(1)).getMessage("book.info",
                book.getIsbn(), book.getName(), book.authorFullName(), book.genreName());
        verify(ioService, times(1)).outputData("Book Info");
    }


    @DisplayName("find book by isbn test failed")
    @Test
    public void findBookByISBNAndPrintTestFailed() {
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("error"));
        when(bookDao.findBookByIsbn(1L)).thenThrow(exception);
        when(messageService.getMessage("book.id.not.found.error",
                1L, exception.getMessage())).thenReturn("Error");

        var error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.findBookByISBNAndPrint(1L); });
        Assertions.assertEquals("Error", error.getMessage());

        verify(bookDao, times(1)).findBookByIsbn(1L);
        verify(messageService, times(1))
                .getMessage("book.id.not.found.error",1L, exception.getMessage());
    }

    @DisplayName("find book by name test")
    @Test
    public void findBookByNameAndPrintTest() {
        String bookName = "BOOK 1";
        Book book = new Book(1L, bookName, 2019, new Author(), new Genre());
        List<Book> books = new ArrayList<>();
        books.add(book);
        when(bookDao.findBookByName(bookName)).thenReturn(books);
        when(messageService.getMessage("book.info",
                book.getIsbn(), book.getName(), book.authorFullName(), book.genreName())).thenReturn("Book Info");
        doNothing().when(ioService).outputData("Book Info");

        libraryService.findBookByNameAndPrint(bookName);

        verify(bookDao, times(1)).findBookByName(bookName);
        verify(messageService, times(1)).getMessage("book.info",
                book.getIsbn(), book.getName(), book.authorFullName(), book.genreName());
        verify(ioService, times(1)).outputData("Book Info");
    }

    @DisplayName("not find any book by name test")
    @Test
    public void findBookByNameAndPrintTestResultEmpty() {
        String bookName = "BOOK 1";
        List<Book> books = new ArrayList<>();
        when(bookDao.findBookByName(bookName)).thenReturn(books);
        when(messageService.getMessage("book.name.not.found", bookName)).thenReturn("Not found");
        doNothing().when(ioService).outputData("Not found");

        libraryService.findBookByNameAndPrint(bookName);

        verify(bookDao, times(1)).findBookByName(bookName);
        verify(messageService, times(1)).getMessage("book.name.not.found", bookName);
        verify(ioService, times(1)).outputData("Not found");
    }

    @DisplayName("find book by name test failed")
    @Test
    public void findBookByNameAndPrintTestFailed() {
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("error"));
        String bookName = "BOOK 1";
        when(bookDao.findBookByName(bookName)).thenThrow(exception);
        when(messageService.getMessage("book.name.not.found.error.reason",
                bookName, exception.getMessage())).thenReturn("Error");

        var error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.findBookByNameAndPrint(bookName); });
        Assertions.assertEquals("Error", error.getMessage());

        verify(bookDao, times(1)).findBookByName(bookName);
        verify(messageService, times(1))
                .getMessage("book.name.not.found.error.reason",bookName, exception.getMessage());
    }

    @DisplayName("find book by author test")
    @Test
    public void findBookByAuthorAndPrintTest() {
        String authorFirstName = "Mary";
        String authorLastName = "Fox";
        Author author = new Author(1L, authorFirstName, authorLastName, "234355", "mail");
        Book book = new Book(1L, "BOOK 1", 2019, author, new Genre());
        List<Book> books = new ArrayList<>();
        books.add(book);
        when(bookDao.findBookByAuthor(authorFirstName, authorLastName)).thenReturn(books);
        when(messageService.getMessage("book.info",
                book.getIsbn(), book.getName(), book.authorFullName(), book.genreName())).thenReturn("Book Info");
        doNothing().when(ioService).outputData("Book Info");

        libraryService.findBookByAuthorAndPrint(authorFirstName, authorLastName);

        verify(bookDao, times(1)).findBookByAuthor(authorFirstName, authorLastName);
        verify(messageService, times(1)).getMessage("book.info",
                book.getIsbn(), book.getName(), book.authorFullName(), book.genreName());
        verify(ioService, times(1)).outputData("Book Info");
    }

    @DisplayName("not find any book by author test")
    @Test
    public void findBookByAuthorAndPrintTestResultEmpty() {
        String authorFirstName = "Mary";
        String authorLastName = "Fox";
        List<Book> books = new ArrayList<>();
        when(bookDao.findBookByAuthor(authorFirstName, authorLastName)).thenReturn(books);
        when(messageService.getMessage("book.author.not.found", authorFirstName, authorLastName)).thenReturn("Not found");
        doNothing().when(ioService).outputData("Not found");

        libraryService.findBookByAuthorAndPrint(authorFirstName, authorLastName);

        verify(bookDao, times(1)).findBookByAuthor(authorFirstName, authorLastName);
        verify(messageService, times(1))
                .getMessage("book.author.not.found", authorFirstName, authorLastName);
        verify(ioService, times(1)).outputData("Not found");
    }

    @DisplayName("find book by author test failed")
    @Test
    public void findBookByAuthorAndPrintTestFailed() {
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("error"));
        String authorFirstName = "Mary";
        String authorLastName = "Fox";
        when(bookDao.findBookByAuthor(authorFirstName, authorLastName)).thenThrow(exception);
        when(messageService.getMessage("book.author.not.found.error.reason",
                authorFirstName, authorLastName, exception.getMessage())).thenReturn("Error");

        var error = Assertions.assertThrows(LibraryException.class, () -> {
            libraryService.findBookByAuthorAndPrint(authorFirstName, authorLastName); });
        Assertions.assertEquals("Error", error.getMessage());

        verify(bookDao, times(1)).findBookByAuthor(authorFirstName, authorLastName);
        verify(messageService, times(1))
                .getMessage("book.author.not.found.error.reason",
                        authorFirstName, authorLastName, exception.getMessage());
    }

    @DisplayName("add book test")
    @Test
    public void addNewBookTest() {
        Author author = new Author(1L, "Mary", "Fox", "234355", "mail");
        Book book = new Book(1L, "BOOK 1", 2019, author, new Genre(1L, "Genre"));
        doNothing().when(bookDao).addBook(book);
        when(genreDao.findGenreById(book.getGenre().getId())).thenReturn(book.getGenre());
        when(authorDao.findAuthorById(book.getAuthor().getId())).thenReturn(book.getAuthor());
        when(messageService.getMessage("book.added", book.getIsbn())).thenReturn("Book was added");
        doNothing().when(ioService).outputData("Book was added");

        libraryService.addNewBook(book);

        verify(bookDao, times(1)).addBook(book);
        verify(genreDao, times(1)).findGenreById(book.getGenre().getId());
        verify(authorDao, times(1)).findAuthorById(book.getAuthor().getId());
        verify(messageService, times(1)).getMessage("book.added", book.getIsbn());
        verify(ioService, times(1)).outputData("Book was added");
    }

    @DisplayName("add book test no author")
    @Test
    public void addNewBookTestNoAuthor() {
        Author author = new Author(1L, "Mary", "Fox", "234355", "mail");
        Book book = new Book(1L, "BOOK 1", 2019, author, new Genre(1L, "Genre"));
        doNothing().when(bookDao).addBook(book);
        when(genreDao.findGenreById(book.getGenre().getId())).thenReturn(book.getGenre());
        when(authorDao.findAuthorById(book.getAuthor().getId())).thenThrow(EmptyResultDataAccessException.class);
        when(messageService.getMessage("author.not.exist", book.getAuthor().getId())).thenReturn("Author doe not exist");
        when(messageService.getMessage("book.add.error", book.getIsbn(), "Author doe not exist"))
                .thenReturn("Book cannot be added");

        var error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.addNewBook(book); });
        Assertions.assertEquals("Book cannot be added", error.getMessage());

        verify(bookDao, never()).addBook(book);
        verify(genreDao, times(1)).findGenreById(book.getGenre().getId());
        verify(authorDao, times(1)).findAuthorById(book.getAuthor().getId());
        verify(messageService, times(1))
                .getMessage("author.not.exist", book.getAuthor().getId());
        verify(messageService, times(1))
                .getMessage("book.add.error", book.getIsbn(), "Author doe not exist");
    }

    @DisplayName("add book test no genre")
    @Test
    public void addNewBookTestNoGenre() {
        Author author = new Author(1L, "Mary", "Fox", "234355", "mail");
        Book book = new Book(1L, "BOOK 1", 2019, author, new Genre(1L, "Genre"));
        doNothing().when(bookDao).addBook(book);
        when(genreDao.findGenreById(book.getGenre().getId())).thenThrow(EmptyResultDataAccessException.class);
        when(authorDao.findAuthorById(book.getAuthor().getId())).thenReturn(book.getAuthor());
        when(messageService.getMessage("genre.not.exist", book.getAuthor().getId())).thenReturn("Genre doe not exist");
        when(messageService.getMessage("book.add.error", book.getIsbn(), "Genre doe not exist"))
                .thenReturn("Book cannot be added");

        var error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.addNewBook(book); });
        Assertions.assertEquals("Book cannot be added", error.getMessage());

        verify(bookDao, never()).addBook(book);
        verify(genreDao, times(1)).findGenreById(book.getGenre().getId());
        verify(authorDao, never()).findAuthorById(book.getAuthor().getId());
        verify(messageService, times(1))
                .getMessage("genre.not.exist", book.getAuthor().getId());
        verify(messageService, times(1))
                .getMessage("book.add.error", book.getIsbn(), "Genre doe not exist");
    }

    @DisplayName("add book test failed")
    @Test
    public void addNewBookTestFailed() {
        Author author = new Author(1L, "Mary", "Fox", "234355", "mail");
        Book book = new Book(1L, "BOOK 1", 2019, author, new Genre(1L, "Genre"));
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("error"));
        doThrow(exception).when(bookDao).addBook(book);
        when(messageService.getMessage("book.add.error", book.getIsbn(), exception.getMessage()))
                .thenReturn("Book cannot be added");

        var error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.addNewBook(book); });
        Assertions.assertEquals("Book cannot be added", error.getMessage());


        verify(bookDao, times(1)).addBook(book);
        verify(genreDao, times(1)).findGenreById(book.getGenre().getId());
        verify(authorDao, times(1)).findAuthorById(book.getAuthor().getId());
        verify(messageService, times(1))
                .getMessage("book.add.error", book.getIsbn(), exception.getMessage());
    }

    @DisplayName("add author test")
    @Test
    public void addNewAuthorTest() {
        Author author = new Author(1L, "Mary", "Fox", "234355", "mail");
        doNothing().when(authorDao).addAuthor(author);
        when(messageService.getMessage("author.added", author.fullName())).thenReturn("Author was added");
        doNothing().when(ioService).outputData("Author was added");

        libraryService.addNewAuthor(author);

        verify(authorDao, times(1)).addAuthor(author);
        verify(messageService, times(1))
                .getMessage("author.added", author.fullName());
        verify(ioService, times(1)).outputData("Author was added");
    }

    @DisplayName("add author test failed")
    @Test
    public void addNewAuthorTestFailed() {
        Author author = new Author(1L, "Mary", "Fox", "234355", "mail");
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("error"));
        doThrow(exception).when(authorDao).addAuthor(author);
        when(messageService.getMessage("author.added.error", author.fullName(), exception.getMessage()))
                .thenReturn("Author cannot be added");

        var error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.addNewAuthor(author); });
        Assertions.assertEquals("Author cannot be added", error.getMessage());

        verify(authorDao, times(1)).addAuthor(author);
        verify(messageService, times(1))
                .getMessage("author.added.error", author.fullName(), exception.getMessage());
    }

    @DisplayName("add genre test")
    @Test
    public void addNewGenreTest() {
        Genre genre = new Genre(1L, "Genre");
        doNothing().when(genreDao).addGenre(genre);
        when(messageService.getMessage("genre.added", genre.getGenreName())).thenReturn("Genre was added");
        doNothing().when(ioService).outputData("Genre was added");

        libraryService.addNewGenre(genre);

        verify(genreDao, times(1)).addGenre(genre);
        verify(messageService, times(1))
                .getMessage("genre.added", genre.getGenreName());
        verify(ioService, times(1)).outputData("Genre was added");
    }

    @DisplayName("add genre test failed")
    @Test
    public void addNewGenreTestFailed() {
        Genre genre = new Genre(1L, "Genre");
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("error"));
        doThrow(exception).when(genreDao).addGenre(genre);
        when(messageService.getMessage("genre.added.error", genre.getGenreName(), exception.getMessage()))
                .thenReturn("Genre cannot be added");

        var error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.addNewGenre(genre); });
        Assertions.assertEquals("Genre cannot be added", error.getMessage());

        verify(genreDao, times(1)).addGenre(genre);
        verify(messageService, times(1))
                .getMessage("genre.added.error", genre.getGenreName(), exception.getMessage());
    }

    @DisplayName("delete book test")
    @Test
    public void deleteBookTest() {
        long isbn = 1L;
        doNothing().when(bookDao).deleteBookById(isbn);
        when(messageService.getMessage("book.deleted", isbn)).thenReturn("Book was deleted");
        doNothing().when(ioService).outputData("Book was deleted");

        libraryService.deleteBook(isbn);

        verify(bookDao, times(1)).deleteBookById(isbn);
        verify(messageService, times(1))
                .getMessage("book.deleted", isbn);
        verify(ioService, times(1)).outputData("Book was deleted");
    }

    @DisplayName("delete book failed")
    @Test
    public void deleteBookTestFailed() {
        long isbn = 1L;
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("error"));
        doThrow(exception).when(bookDao).deleteBookById(isbn);
        when(messageService.getMessage("book.deleted.error", isbn, exception.getMessage()))
                .thenReturn("Book cannot be deleted");

        var error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.deleteBook(isbn); });
        Assertions.assertEquals("Book cannot be deleted", error.getMessage());

        verify(bookDao, times(1)).deleteBookById(isbn);
        verify(messageService, times(1))
                .getMessage("book.deleted.error", isbn, exception.getMessage());
    }

    @DisplayName("delete author test")
    @Test
    public void deleteAuthorTest() {
        long authorId = 1L;
        doNothing().when(authorDao).deleteAuthorById(authorId);
        Author author = new Author(authorId, "Mary", "Fox", "234355", "mail");
        when(authorDao.findAuthorById(authorId)).thenReturn(author);
        when(bookDao.findBookByAuthor(author.getFirstName(), author.getLastName())).thenReturn(new ArrayList<>());
        when(messageService.getMessage("author.deleted", authorId)).thenReturn("Author was deleted");
        doNothing().when(ioService).outputData("Author was deleted");

        libraryService.deleteAuthor(authorId);

        verify(authorDao, times(1)).findAuthorById(authorId);
        verify(authorDao, times(1)).deleteAuthorById(authorId);
        verify(bookDao, times(1)).findBookByAuthor(author.getFirstName(), author.getLastName());
        verify(messageService, times(1)).getMessage("author.deleted", authorId);
        verify(ioService, times(1)).outputData("Author was deleted");
    }

    @DisplayName("cannot delete author test, because book exists")
    @Test
    public void deleteAuthorTestBookExists() {
        long authorId = 1L;
        Author author = new Author(authorId, "Mary", "Fox", "234355", "mail");
        when(authorDao.findAuthorById(authorId)).thenReturn(author);
        Book book = new Book(1L, "BOOK 1", 2019, author, new Genre());
        List<Book> books = new ArrayList<>();
        books.add(book);
        when(bookDao.findBookByAuthor(author.getFirstName(), author.getLastName())).thenReturn(books);
        when(messageService.getMessage("book.for.author.exist", author.fullName(),
                books.stream().map(it -> it.getName()).collect(Collectors.joining(", ")))).thenReturn("Book exist");
        when(messageService.getMessage("author.deleted.error", authorId, "Book exist"))
                .thenReturn("Author cannot be deleted");

        var error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.deleteAuthor(authorId); });
        Assertions.assertEquals("Author cannot be deleted", error.getMessage());

        verify(authorDao, never()).deleteAuthorById(anyLong());
        verify(authorDao, times(1)).findAuthorById(authorId);
        verify(bookDao, times(1)).findBookByAuthor(author.getFirstName(), author.getLastName());
        verify(messageService, times(1)).getMessage("book.for.author.exist", author.fullName(),
                books.stream().map(it -> it.getName()).collect(Collectors.joining(", ")));
        verify(messageService, times(1)).getMessage("author.deleted.error", authorId, "Book exist");
    }

    @DisplayName("cannot delete author test, because author does not exist")
    @Test
    public void deleteAuthorTestAuthorNotExist() {
        long authorId = 1L;
        when(authorDao.findAuthorById(authorId)).thenThrow(EmptyResultDataAccessException.class);
        when(messageService.getMessage("author.not.exist", authorId))
                .thenReturn("Author not exist");
        when(messageService.getMessage("author.deleted.error", authorId, "Author not exist"))
                .thenReturn("Author cannot be deleted");

        var error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.deleteAuthor(authorId); });
        Assertions.assertEquals("Author cannot be deleted", error.getMessage());

        verify(authorDao, never()).deleteAuthorById(anyLong());
        verify(authorDao, times(1)).findAuthorById(authorId);
        verify(bookDao, never()).findBookByAuthor(anyString(), anyString());
        verify(messageService, times(1)).getMessage("author.not.exist", authorId);
        verify(messageService, times(1))
                .getMessage("author.deleted.error", authorId, "Author not exist");
    }

    @DisplayName("delete author test failed")
    @Test
    public void deleteAuthorTestFailed() {
        long authorId = 1L;
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("error"));
        doThrow(exception).when(authorDao).deleteAuthorById(authorId);
        Author author = new Author(authorId, "Mary", "Fox", "234355", "mail");
        when(authorDao.findAuthorById(authorId)).thenReturn(author);
        when(bookDao.findBookByAuthor(author.getFirstName(), author.getLastName())).thenReturn(new ArrayList<>());
        when(messageService.getMessage("author.deleted.error", authorId, exception.getMessage()))
                .thenReturn("Error");

        var error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.deleteAuthor(authorId); });
        Assertions.assertEquals("Error", error.getMessage());

        verify(authorDao, times(1)).deleteAuthorById(authorId);
        verify(authorDao, times(1)).findAuthorById(authorId);
        verify(bookDao, times(1)).findBookByAuthor(author.getFirstName(), author.getLastName());
        verify(messageService, times(1)).
                getMessage("author.deleted.error", authorId, exception.getMessage());
    }

    @DisplayName("delete genre test")
    @Test
    public void deleteGenreTest() {
        long genreId = 1L;
        doNothing().when(genreDao).deleteGenre(genreId);
        Genre genre = new Genre(genreId, "Genre");
        when(genreDao.findGenreById(genreId)).thenReturn(genre);
        when(bookDao.findBookByGenre(genre.getGenreName())).thenReturn(new ArrayList<>());
        when(messageService.getMessage("genre.deleted", genreId)).thenReturn("Genre was deleted");
        doNothing().when(ioService).outputData("Genre was deleted");

        libraryService.deleteGenre(genreId);

        verify(genreDao, times(1)).findGenreById(genreId);
        verify(genreDao, times(1)).deleteGenre(genreId);
        verify(bookDao, times(1)).findBookByGenre(genre.getGenreName());
        verify(messageService, times(1)).getMessage("genre.deleted", genreId);
        verify(ioService, times(1)).outputData("Genre was deleted");
    }

    @DisplayName("cannot delete genre test, because book exists")
    @Test
    public void deleteGenreTestBookExists() {
        long genreId = 1L;
        Genre genre = new Genre(genreId, "Genre");
        when(genreDao.findGenreById(genreId)).thenReturn(genre);
        Book book = new Book(1L, "BOOK 1", 2019, new Author(), genre);
        List<Book> books = new ArrayList<>();
        books.add(book);
        when(bookDao.findBookByGenre(genre.getGenreName())).thenReturn(books);
        when(messageService.getMessage("book.for.genre.exist", genre.getGenreName(),
                books.stream().map(it -> it.getName()).collect(Collectors.joining(", ")))).thenReturn("Book exist");
        when(messageService.getMessage("genre.deleted.error", genreId, "Book exist"))
                .thenReturn("Genre cannot be deleted");

        var error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.deleteGenre(genreId); });
        Assertions.assertEquals("Genre cannot be deleted", error.getMessage());

        verify(genreDao, never()).deleteGenre(anyLong());
        verify(genreDao, times(1)).findGenreById(genreId);
        verify(bookDao, times(1)).findBookByGenre(genre.getGenreName());
        verify(messageService, times(1)).getMessage("book.for.genre.exist", genre.getGenreName(),
                books.stream().map(it -> it.getName()).collect(Collectors.joining(", ")));
        verify(messageService, times(1)).getMessage("genre.deleted.error", genreId, "Book exist");
    }

    @DisplayName("cannot delete genre test, because genre does not exist")
    @Test
    public void deleteGenreTestGenreNotExist() {
        long genreId = 1L;
        when(genreDao.findGenreById(genreId)).thenThrow(EmptyResultDataAccessException.class);
        when(messageService.getMessage("genre.not.exist", genreId))
                .thenReturn("Genre not exist");
        when(messageService.getMessage("genre.deleted.error", genreId, "Genre not exist"))
                .thenReturn("Genre cannot be deleted");

        var error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.deleteGenre(genreId); });
        Assertions.assertEquals("Genre cannot be deleted", error.getMessage());

        verify(genreDao, never()).deleteGenre(anyLong());
        verify(genreDao, times(1)).findGenreById(genreId);
        verify(bookDao, never()).findBookByGenre(anyString());
        verify(messageService, times(1)).getMessage("genre.not.exist", genreId);
        verify(messageService, times(1))
                .getMessage("genre.deleted.error", genreId, "Genre not exist");
    }

    @DisplayName("delete genre test failed")
    @Test
    public void deleteGenreTestFailed() {
        long genreId = 1L;
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("error"));
        doThrow(exception).when(genreDao).deleteGenre(genreId);
        Genre genre = new Genre(genreId, "Genre");
        when(genreDao.findGenreById(genreId)).thenReturn(genre);
        when(bookDao.findBookByGenre(genre.getGenreName())).thenReturn(new ArrayList<>());
        when(messageService.getMessage("genre.deleted.error", genreId, exception.getMessage()))
                .thenReturn("Error");

        var error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.deleteGenre(genreId); });
        Assertions.assertEquals("Error", error.getMessage());

        verify(genreDao, times(1)).deleteGenre(genreId);
        verify(genreDao, times(1)).findGenreById(genreId);
        verify(bookDao, times(1)).findBookByGenre(genre.getGenreName());
        verify(messageService, times(1)).
                getMessage("genre.deleted.error", genreId, exception.getMessage());
    }
}
