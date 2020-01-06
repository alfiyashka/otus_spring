package ru.otus.avalieva.library.orm.jpa.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.avalieva.library.orm.jpa.Application;
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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ContextConfiguration(classes = Application.class)
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
        when(genreRepository.getAll()).thenReturn(genres);

        for (Genre genre: genres) {
            when(messageService.getMessage("genre.info", genre.getId(), genre.getGenreName()))
                    .thenReturn("Genre Info");
            doNothing().when(ioService).outputData("Genre Info");
        }
        libraryService.printAllGenres();

        verify(genreRepository, times(1)).getAll();
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
        when(genreRepository.getAll()).thenThrow(exception);
        when(messageService.getMessage("genre.info.error", exception.getMessage())).thenReturn("Error");

        var error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.printAllGenres(); });
        Assertions.assertEquals("Error", error.getMessage());

        verify(genreRepository, times(1)).getAll();
        verify(messageService, times(1)).getMessage("genre.info.error", exception.getMessage());
    }

    @DisplayName("print all books test")
    @Test
    public void printAllBooksTest() {
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "Book 1", 2019, new Author(), new Genre()));
        books.add(new Book(2L, "Book 2", 2019, new Author(), new Genre()));
        when(bookRepository.getAll()).thenReturn(books);

        for (Book book: books) {
            when(messageService.getMessage("book.info",
                    book.getIsbn(), book.getName(), book.authorFullName(), book.genreName()))
                    .thenReturn("Book Info");
            doNothing().when(ioService).outputData("Book Info");
        }
        libraryService.printAllBooks();

        verify(bookRepository, times(1)).getAll();
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
        when(bookRepository.getAll()).thenThrow(exception);
        when(messageService.getMessage("book.info.error", exception.getMessage())).thenReturn("Error");

        var error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.printAllBooks(); });
        Assertions.assertEquals("Error", error.getMessage());

        verify(bookRepository, times(1)).getAll();
        verify(messageService, times(1)).getMessage("book.info.error", exception.getMessage());
    }

    @DisplayName("print all authors test")
    @Test
    public void printAllAuthorTest() {
        List<Author> authors = new ArrayList<>();
        authors.add(new Author(1L, "Mary", "Panak", "23432435", "mary@mail.com"));
        authors.add(new Author(1L, "Mark", "Smith", "11111", "mark@mail.com"));
        when(authorRepository.getAll()).thenReturn(authors);

        for (Author author: authors) {
            when(messageService.getMessage("author.info",
                    author.getId(), author.fullName(), author.getEmail(), author.getPhoneNumber()))
                    .thenReturn("Author Info");
            doNothing().when(ioService).outputData("Author Info");
        }
        libraryService.printAllAuthors();

        verify(authorRepository, times(1)).getAll();
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
        when(authorRepository.getAll()).thenThrow(exception);
        when(messageService.getMessage("author.info.error", exception.getMessage())).thenReturn("Error");

        var error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.printAllAuthors(); });
        Assertions.assertEquals("Error", error.getMessage());

        verify(authorRepository, times(1)).getAll();
        verify(messageService, times(1)).getMessage("author.info.error", exception.getMessage());
    }


    @DisplayName("find book by isbn test")
    @Test
    public void findBookByISBNAndPrintTest() {
        Optional<Book> book = Optional.of(new Book(1L, "Book 1", 2019, new Author(), new Genre()));
        when(bookRepository.findByIsbn(1L)).thenReturn(book);
        when(messageService.getMessage("book.info", book.get().getIsbn(), book.get().getName(),
                book.get().authorFullName(), book.get().genreName())).thenReturn("Book Info");
        doNothing().when(ioService).outputData("Book Info");

        libraryService.findBookByISBNAndPrint(1);

        verify(bookRepository, times(1)).findByIsbn(1L);
        verify(messageService, times(1)).getMessage("book.info", book.get().getIsbn(),
                book.get().getName(), book.get().authorFullName(), book.get().genreName());
        verify(ioService, times(1)).outputData("Book Info");
    }


    @DisplayName("find book by isbn test failed")
    @Test
    public void findBookByISBNAndPrintTestFailed() {
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("error"));
        when(bookRepository.findByIsbn(1L)).thenThrow(exception);
        when(messageService.getMessage("book.id.not.found.error.cause",
                1L, exception.getMessage())).thenReturn("Error");

        var error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.findBookByISBNAndPrint(1L); });
        Assertions.assertEquals("Error", error.getMessage());

        verify(bookRepository, times(1)).findByIsbn(1L);
        verify(messageService, times(1))
                .getMessage("book.id.not.found.error.cause",1L, exception.getMessage());
    }

    @DisplayName("find book by isbn test not found")
    @Test
    public void findBookByISBNAndPrintTestNotFound() {
        when(bookRepository.findByIsbn(1L)).thenReturn(Optional.ofNullable(null));
        when(messageService.getMessage("book.id.not.found",1L)).thenReturn("Not Found");

        when(messageService.getMessage("book.id.not.found.error.cause",
                1L, "Not Found")).thenReturn("Error");

        var error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.findBookByISBNAndPrint(1L); });
        Assertions.assertEquals("Error", error.getMessage());

        verify(bookRepository, times(1)).findByIsbn(1L);
        verify(messageService, times(1)).getMessage("book.id.not.found",1L);
        verify(messageService, times(1))
               .getMessage("book.id.not.found.error.cause",1L, "Not Found");
    }

    @DisplayName("find book by name test")
    @Test
    public void findBookByNameAndPrintTest() {
        String bookName = "BOOK 1";
        Book book = new Book(1L, bookName, 2019, new Author(), new Genre());
        List<Book> books = new ArrayList<>();
        books.add(book);
        when(bookRepository.findByName(bookName)).thenReturn(books);
        when(messageService.getMessage("book.info", book.getIsbn(), book.getName(),
                book.authorFullName(), book.genreName())).thenReturn("Book Info");
        doNothing().when(ioService).outputData("Book Info");

        libraryService.findBookByNameAndPrint(bookName);

        verify(bookRepository, times(1)).findByName(bookName);
        verify(messageService, times(1)).getMessage("book.info",
                book.getIsbn(), book.getName(), book.authorFullName(), book.genreName());
        verify(ioService, times(1)).outputData("Book Info");
    }

    @DisplayName("not find any book by name test")
    @Test
    public void findBookByNameAndPrintTestResultEmpty() {
        String bookName = "BOOK 1";
        List<Book> books = new ArrayList<>();
        when(bookRepository.findByName(bookName)).thenReturn(books);
        when(messageService.getMessage("book.name.not.found", bookName)).thenReturn("Not found");
        doNothing().when(ioService).outputData("Not found");

        libraryService.findBookByNameAndPrint(bookName);

        verify(bookRepository, times(1)).findByName(bookName);
        verify(messageService, times(1)).getMessage("book.name.not.found", bookName);
        verify(ioService, times(1)).outputData("Not found");
    }

    @DisplayName("find book by name test failed")
    @Test
    public void findBookByNameAndPrintTestFailed() {
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("error"));
        String bookName = "BOOK 1";
        when(bookRepository.findByName(bookName)).thenThrow(exception);
        when(messageService.getMessage("book.name.not.found.error.reason",
                bookName, exception.getMessage())).thenReturn("Error");

        var error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.findBookByNameAndPrint(bookName); });
        Assertions.assertEquals("Error", error.getMessage());

        verify(bookRepository, times(1)).findByName(bookName);
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
        when(bookRepository.findByAuthor(authorFirstName, authorLastName)).thenReturn(books);
        when(messageService.getMessage("book.info", book.getIsbn(), book.getName(),
                book.authorFullName(), book.genreName())).thenReturn("Book Info");
        doNothing().when(ioService).outputData("Book Info");

        libraryService.findBookByAuthorAndPrint(authorFirstName, authorLastName);

        verify(bookRepository, times(1)).findByAuthor(authorFirstName, authorLastName);
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
        when(bookRepository.findByAuthor(authorFirstName, authorLastName)).thenReturn(books);
        when(messageService.getMessage("book.author.not.found", authorFirstName, authorLastName)).thenReturn("Not found");
        doNothing().when(ioService).outputData("Not found");

        libraryService.findBookByAuthorAndPrint(authorFirstName, authorLastName);

        verify(bookRepository, times(1)).findByAuthor(authorFirstName, authorLastName);
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
        when(bookRepository.findByAuthor(authorFirstName, authorLastName)).thenThrow(exception);
        when(messageService.getMessage("book.author.not.found.error.reason",
                authorFirstName, authorLastName, exception.getMessage())).thenReturn("Error");

        var error = Assertions.assertThrows(LibraryException.class, () -> {
            libraryService.findBookByAuthorAndPrint(authorFirstName, authorLastName); });
        Assertions.assertEquals("Error", error.getMessage());

        verify(bookRepository, times(1)).findByAuthor(authorFirstName, authorLastName);
        verify(messageService, times(1))
                .getMessage("book.author.not.found.error.reason",
                        authorFirstName, authorLastName, exception.getMessage());
    }

    @DisplayName("find book by genre test")
    @Test
    public void findBookByGenreAndPrintTest() {
        String genreName = "GENRE";
        Genre genre = new Genre(1L, genreName);
        Book book = new Book(1L, "BOOK 1", 2019, new Author(), genre);
        List<Book> books = new ArrayList<>();
        books.add(book);
        when(bookRepository.findByGenre(genreName)).thenReturn(books);
        when(messageService.getMessage("book.info", book.getIsbn(), book.getName(),
                book.authorFullName(), book.genreName())).thenReturn("Book Info");
        doNothing().when(ioService).outputData("Book Info");

        libraryService.findBookByGenreAndPrint(genreName);

        verify(bookRepository, times(1)).findByGenre(genreName);
        verify(messageService, times(1)).getMessage("book.info",
                book.getIsbn(), book.getName(), book.authorFullName(), book.genreName());
        verify(ioService, times(1)).outputData("Book Info");
    }

    @DisplayName("not find any book by genre test")
    @Test
    public void findBookByGenreAndPrintTestResultEmpty() {
        String genreName = "GENRE";
        List<Book> books = new ArrayList<>();
        when(bookRepository.findByGenre(genreName)).thenReturn(books);
        when(messageService.getMessage("book.genre.not.found", genreName)).thenReturn("Not found");
        doNothing().when(ioService).outputData("Not found");

        libraryService.findBookByGenreAndPrint(genreName);

        verify(bookRepository, times(1)).findByGenre(genreName);
        verify(messageService, times(1))
                .getMessage("book.genre.not.found", genreName);
        verify(ioService, times(1)).outputData("Not found");
    }

    @DisplayName("find book by genre test failed")
    @Test
    public void findBookByGenreAndPrintTestFailed() {
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("error"));
        String genreName = "GENRE";
        when(bookRepository.findByGenre(genreName)).thenThrow(exception);
        when(messageService.getMessage("book.genre.not.found.error.reason",
                genreName, exception.getMessage())).thenReturn("Error");

        var error = Assertions.assertThrows(LibraryException.class, () -> {
            libraryService.findBookByGenreAndPrint(genreName); });
        Assertions.assertEquals("Error", error.getMessage());

        verify(bookRepository, times(1)).findByGenre(genreName);
        verify(messageService, times(1))
                .getMessage("book.genre.not.found.error.reason", genreName, exception.getMessage());
    }

    @DisplayName("add book test")
    @Test
    public void addNewBookTest() {
        Author author = new Author(1L, "Mary", "Fox", "234355", "mail");
        Book book = new Book(1L, "BOOK 1", 2019, author, new Genre(1L, "Genre"));
        doNothing().when(bookRepository).add(book);
        when(genreRepository.findById(book.getGenre().getId())).thenReturn(Optional.of(book.getGenre()));
        when(authorRepository.findById(book.getAuthor().getId())).thenReturn(Optional.of(book.getAuthor()));
        when(messageService.getMessage("book.added", book.getIsbn())).thenReturn("Book was added");
        doNothing().when(ioService).outputData("Book was added");

        libraryService.addNewBook(book);

        verify(bookRepository, times(1)).add(book);
        verify(genreRepository, times(1)).findById(book.getGenre().getId());
        verify(authorRepository, times(1)).findById(book.getAuthor().getId());
        verify(messageService, times(1)).getMessage("book.added", book.getIsbn());
        verify(ioService, times(1)).outputData("Book was added");
    }

    @DisplayName("add book test no author")
    @Test
    public void addNewBookTestNoAuthor() {
        Author author = new Author(1L, "Mary", "Fox", "234355", "mail");
        Book book = new Book(1L, "BOOK 1", 2019, author, new Genre(1L, "Genre"));
        doNothing().when(bookRepository).add(book);
        when(genreRepository.findById(book.getGenre().getId())).thenReturn(Optional.of(book.getGenre()));
        when(authorRepository.findById(book.getAuthor().getId())).thenReturn(Optional.ofNullable(null));
        when(messageService.getMessage("author.not.exist", book.getAuthor().getId())).thenReturn("Author does not exist");
        when(messageService.getMessage("book.add.error", book.getName(), "Author does not exist"))
                .thenReturn("Book cannot be added");

        var error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.addNewBook(book); });
        Assertions.assertEquals("Book cannot be added", error.getMessage());

        verify(bookRepository, never()).add(book);
        verify(genreRepository, times(1)).findById(book.getGenre().getId());
        verify(authorRepository, times(1)).findById(book.getAuthor().getId());
        verify(messageService, times(1))
                .getMessage("author.not.exist", book.getAuthor().getId());
        verify(messageService, times(1))
                .getMessage("book.add.error", book.getName(), "Author does not exist");
    }

    @DisplayName("add book test no genre")
    @Test
    public void addNewBookTestNoGenre() {
        Author author = new Author(1L, "Mary", "Fox", "234355", "mail");
        Book book = new Book(1L, "BOOK 1", 2019, author, new Genre(1L, "Genre"));
        doNothing().when(bookRepository).add(book);
        when(genreRepository.findById(book.getGenre().getId())).thenReturn(Optional.ofNullable(null));
        when(authorRepository.findById(book.getAuthor().getId())).thenReturn(Optional.of(book.getAuthor()));
        when(messageService.getMessage("genre.not.exist", book.getAuthor().getId())).thenReturn("Genre does not exist");
        when(messageService.getMessage("book.add.error", book.getName(), "Genre does not exist"))
                .thenReturn("Book cannot be added");

        var error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.addNewBook(book); });
        Assertions.assertEquals("Book cannot be added", error.getMessage());

        verify(bookRepository, never()).add(book);
        verify(genreRepository, times(1)).findById(book.getGenre().getId());
        verify(authorRepository, never()).findById(book.getAuthor().getId());
        verify(messageService, times(1))
                .getMessage("genre.not.exist", book.getAuthor().getId());
        verify(messageService, times(1))
                .getMessage("book.add.error", book.getName(), "Genre does not exist");
    }

    @DisplayName("add book test failed")
    @Test
    public void addNewBookTestFailed() {
        Author author = new Author(1L, "Mary", "Fox", "234355", "mail");
        Book book = new Book(1L, "BOOK 1", 2019, author, new Genre(1L, "Genre"));
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("error"));
        doThrow(exception).when(bookRepository).add(book);
        when(genreRepository.findById(book.getGenre().getId())).thenReturn(Optional.of(book.getGenre()));
        when(authorRepository.findById(book.getAuthor().getId())).thenReturn(Optional.of(book.getAuthor()));
        when(messageService.getMessage("book.add.error", book.getName(), exception.getMessage()))
                .thenReturn("Book cannot be added");

        var error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.addNewBook(book); });
        Assertions.assertEquals("Book cannot be added", error.getMessage());


        verify(bookRepository, times(1)).add(book);
        verify(genreRepository, times(1)).findById(book.getGenre().getId());
        verify(authorRepository, times(1)).findById(book.getAuthor().getId());
        verify(messageService, times(1))
                .getMessage("book.add.error", book.getName(), exception.getMessage());
    }

    @DisplayName("add author test")
    @Test
    public void addNewAuthorTest() {
        Author author = new Author(1L, "Mary", "Fox", "234355", "mail");
        doNothing().when(authorRepository).add(author);
        when(messageService.getMessage("author.added", author.fullName())).thenReturn("Author was added");
        doNothing().when(ioService).outputData("Author was added");

        libraryService.addNewAuthor(author);

        verify(authorRepository, times(1)).add(author);
        verify(messageService, times(1))
                .getMessage("author.added", author.fullName());
        verify(ioService, times(1)).outputData("Author was added");
    }

    @DisplayName("add author test failed")
    @Test
    public void addNewAuthorTestFailed() {
        Author author = new Author(1L, "Mary", "Fox", "234355", "mail");
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("error"));
        doThrow(exception).when(authorRepository).add(author);
        when(messageService.getMessage("author.added.error", author.fullName(), exception.getMessage()))
                .thenReturn("Author cannot be added");

        var error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.addNewAuthor(author); });
        Assertions.assertEquals("Author cannot be added", error.getMessage());

        verify(authorRepository, times(1)).add(author);
        verify(messageService, times(1))
                .getMessage("author.added.error", author.fullName(), exception.getMessage());
    }

    @DisplayName("add genre test")
    @Test
    public void addNewGenreTest() {
        Genre genre = new Genre(1L, "Genre");
        doNothing().when(genreRepository).add(genre);
        when(messageService.getMessage("genre.added", genre.getGenreName())).thenReturn("Genre was added");
        doNothing().when(ioService).outputData("Genre was added");

        libraryService.addNewGenre(genre);

        verify(genreRepository, times(1)).add(genre);
        verify(messageService, times(1))
                .getMessage("genre.added", genre.getGenreName());
        verify(ioService, times(1)).outputData("Genre was added");
    }

    @DisplayName("add genre test failed")
    @Test
    public void addNewGenreTestFailed() {
        Genre genre = new Genre(1L, "Genre");
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("error"));
        doThrow(exception).when(genreRepository).add(genre);
        when(messageService.getMessage("genre.added.error", genre.getGenreName(), exception.getMessage()))
                .thenReturn("Genre cannot be added");

        var error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.addNewGenre(genre); });
        Assertions.assertEquals("Genre cannot be added", error.getMessage());

        verify(genreRepository, times(1)).add(genre);
        verify(messageService, times(1))
                .getMessage("genre.added.error", genre.getGenreName(), exception.getMessage());
    }

    @DisplayName("delete book test")
    @Test
    public void deleteBookTest() {
        long isbn = 1L;
        doNothing().when(bookRepository).deleteById(isbn);
        when(messageService.getMessage("book.deleted", isbn)).thenReturn("Book was deleted");
        doNothing().when(ioService).outputData("Book was deleted");

        libraryService.deleteBook(isbn);

        verify(bookRepository, times(1)).deleteById(isbn);
        verify(messageService, times(1))
                .getMessage("book.deleted", isbn);
        verify(ioService, times(1)).outputData("Book was deleted");
    }

    @DisplayName("delete book failed")
    @Test
    public void deleteBookTestFailed() {
        long isbn = 1L;
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("error"));
        doThrow(exception).when(bookRepository).deleteById(isbn);
        when(messageService.getMessage("book.deleted.error", isbn, exception.getMessage()))
                .thenReturn("Book cannot be deleted");

        var error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.deleteBook(isbn); });
        Assertions.assertEquals("Book cannot be deleted", error.getMessage());

        verify(bookRepository, times(1)).deleteById(isbn);
        verify(messageService, times(1))
                .getMessage("book.deleted.error", isbn, exception.getMessage());
    }

    @DisplayName("delete author test")
    @Test
    public void deleteAuthorTest() {
        long authorId = 1L;
        doNothing().when(authorRepository).deleteById(authorId);
        Author author = new Author(authorId, "Mary", "Fox", "234355", "mail");
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(bookRepository.findByAuthor(author.getFirstName(), author.getLastName())).thenReturn(new ArrayList<>());
        when(messageService.getMessage("author.deleted", authorId)).thenReturn("Author was deleted");
        doNothing().when(ioService).outputData("Author was deleted");

        libraryService.deleteAuthor(authorId);

        verify(authorRepository, times(1)).findById(authorId);
        verify(authorRepository, times(1)).deleteById(authorId);
        verify(bookRepository, times(1)).findByAuthor(author.getFirstName(), author.getLastName());
        verify(messageService, times(1)).getMessage("author.deleted", authorId);
        verify(ioService, times(1)).outputData("Author was deleted");
    }

    @DisplayName("cannot delete author test, because book exists")
    @Test
    public void deleteAuthorTestBookExists() {
        long authorId = 1L;
        Author author = new Author(authorId, "Mary", "Fox", "234355", "mail");
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        Book book = new Book(1L, "BOOK 1", 2019, author, new Genre());
        List<Book> books = new ArrayList<>();
        books.add(book);
        when(bookRepository.findByAuthor(author.getFirstName(), author.getLastName())).thenReturn(books);
        when(messageService.getMessage("book.for.author.exist", author.fullName(),
                books.stream().map(it -> it.getName()).collect(Collectors.joining(", ")))).thenReturn("Book exist");
        when(messageService.getMessage("author.deleted.error", authorId, "Book exist"))
                .thenReturn("Author cannot be deleted");

        var error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.deleteAuthor(authorId); });
        Assertions.assertEquals("Author cannot be deleted", error.getMessage());

        verify(authorRepository, never()).deleteById(anyLong());
        verify(authorRepository, times(1)).findById(authorId);
        verify(bookRepository, times(1)).findByAuthor(author.getFirstName(), author.getLastName());
        verify(messageService, times(1)).getMessage("book.for.author.exist", author.fullName(),
                books.stream().map(it -> it.getName()).collect(Collectors.joining(", ")));
        verify(messageService, times(1)).getMessage("author.deleted.error", authorId, "Book exist");
    }

    @DisplayName("cannot delete author test, because author does not exist")
    @Test
    public void deleteAuthorTestAuthorNotExist() {
        long authorId = 1L;
        when(authorRepository.findById(authorId)).thenReturn(Optional.ofNullable(null));
        when(messageService.getMessage("author.not.exist", authorId))
                .thenReturn("Author not exist");
        when(messageService.getMessage("author.deleted.error", authorId, "Author not exist"))
                .thenReturn("Author cannot be deleted");

        var error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.deleteAuthor(authorId); });
        Assertions.assertEquals("Author cannot be deleted", error.getMessage());

        verify(authorRepository, never()).deleteById(anyLong());
        verify(authorRepository, times(1)).findById(authorId);
        verify(bookRepository, never()).findByAuthor(anyString(), anyString());
        verify(messageService, times(1)).getMessage("author.not.exist", authorId);
        verify(messageService, times(1))
                .getMessage("author.deleted.error", authorId, "Author not exist");
    }

    @DisplayName("delete author test failed")
    @Test
    public void deleteAuthorTestFailed() {
        long authorId = 1L;
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("error"));
        doThrow(exception).when(authorRepository).deleteById(authorId);
        Author author = new Author(authorId, "Mary", "Fox", "234355", "mail");
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(bookRepository.findByAuthor(author.getFirstName(), author.getLastName())).thenReturn(new ArrayList<>());
        when(messageService.getMessage("author.deleted.error", authorId, exception.getMessage()))
                .thenReturn("Error");

        var error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.deleteAuthor(authorId); });
        Assertions.assertEquals("Error", error.getMessage());

        verify(authorRepository, times(1)).deleteById(authorId);
        verify(authorRepository, times(1)).findById(authorId);
        verify(bookRepository, times(1)).findByAuthor(author.getFirstName(), author.getLastName());
        verify(messageService, times(1)).
                getMessage("author.deleted.error", authorId, exception.getMessage());
    }

    @DisplayName("delete genre test")
    @Test
    public void deleteGenreTest() {
        long genreId = 1L;
        doNothing().when(genreRepository).delete(genreId);
        Genre genre = new Genre(genreId, "Genre");
        when(genreRepository.findById(genreId)).thenReturn(Optional.of(genre));
        when(bookRepository.findByGenre(genre.getGenreName())).thenReturn(new ArrayList<>());
        when(messageService.getMessage("genre.deleted", genreId)).thenReturn("Genre was deleted");
        doNothing().when(ioService).outputData("Genre was deleted");

        libraryService.deleteGenre(genreId);

        verify(genreRepository, times(1)).findById(genreId);
        verify(genreRepository, times(1)).delete(genreId);
        verify(bookRepository, times(1)).findByGenre(genre.getGenreName());
        verify(messageService, times(1)).getMessage("genre.deleted", genreId);
        verify(ioService, times(1)).outputData("Genre was deleted");
    }

    @DisplayName("cannot delete genre test, because book exists")
    @Test
    public void deleteGenreTestBookExists() {
        long genreId = 1L;
        Genre genre = new Genre(genreId, "Genre");
        when(genreRepository.findById(genreId)).thenReturn(Optional.of(genre));
        Book book = new Book(1L, "BOOK 1", 2019, new Author(), genre);
        List<Book> books = new ArrayList<>();
        books.add(book);
        when(bookRepository.findByGenre(genre.getGenreName())).thenReturn(books);
        when(messageService.getMessage("book.for.genre.exist", genre.getGenreName(),
                books.stream().map(it -> it.getName()).collect(Collectors.joining(", ")))).thenReturn("Book exist");
        when(messageService.getMessage("genre.deleted.error", genreId, "Book exist"))
                .thenReturn("Genre cannot be deleted");

        var error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.deleteGenre(genreId); });
        Assertions.assertEquals("Genre cannot be deleted", error.getMessage());

        verify(genreRepository, never()).delete(anyLong());
        verify(genreRepository, times(1)).findById(genreId);
        verify(bookRepository, times(1)).findByGenre(genre.getGenreName());
        verify(messageService, times(1)).getMessage("book.for.genre.exist", genre.getGenreName(),
                books.stream().map(it -> it.getName()).collect(Collectors.joining(", ")));
        verify(messageService, times(1)).getMessage("genre.deleted.error", genreId, "Book exist");
    }

    @DisplayName("cannot delete genre test, because genre does not exist")
    @Test
    public void deleteGenreTestGenreNotExist() {
        long genreId = 1L;
        when(genreRepository.findById(genreId)).thenReturn(Optional.ofNullable(null));
        when(messageService.getMessage("genre.not.exist", genreId))
                .thenReturn("Genre not exist");
        when(messageService.getMessage("genre.deleted.error", genreId, "Genre not exist"))
                .thenReturn("Genre cannot be deleted");

        var error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.deleteGenre(genreId); });
        Assertions.assertEquals("Genre cannot be deleted", error.getMessage());

        verify(genreRepository, never()).delete(anyLong());
        verify(genreRepository, times(1)).findById(genreId);
        verify(bookRepository, never()).findByGenre(anyString());
        verify(messageService, times(1)).getMessage("genre.not.exist", genreId);
        verify(messageService, times(1))
                .getMessage("genre.deleted.error", genreId, "Genre not exist");
    }

    @DisplayName("delete genre test failed")
    @Test
    public void deleteGenreTestFailed() {
        long genreId = 1L;
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("error"));
        doThrow(exception).when(genreRepository).delete(genreId);
        Genre genre = new Genre(genreId, "Genre");
        when(genreRepository.findById(genreId)).thenReturn(Optional.of(genre));
        when(bookRepository.findByGenre(genre.getGenreName())).thenReturn(new ArrayList<>());
        when(messageService.getMessage("genre.deleted.error", genreId, exception.getMessage()))
                .thenReturn("Error");

        var error = Assertions.assertThrows(LibraryException.class, () -> {
            libraryService.deleteGenre(genreId);
        });
        Assertions.assertEquals("Error", error.getMessage());

        verify(genreRepository, times(1)).delete(genreId);
        verify(genreRepository, times(1)).findById(genreId);
        verify(bookRepository, times(1)).findByGenre(genre.getGenreName());
        verify(messageService, times(1)).
                getMessage("genre.deleted.error", genreId, exception.getMessage());
    }

    @DisplayName("add comment test")
    @Test
    public void addCommentTest() {
        final long ISBN = 1L;
        final String COMMENT = "Comment";
        Optional<Book> book = Optional.of(new Book(ISBN, "Book 1", 2019, new Author(), new Genre()));
        when(bookRepository.findByIsbn(ISBN)).thenReturn(book);
        doNothing().when(commentRepository).add(any(Comment.class));
        when(messageService.getMessage("add.comment", ISBN)).thenReturn("Added");
        doNothing().when(ioService).outputData("Added");

        libraryService.addBookComment(ISBN, COMMENT);

        verify(bookRepository, times(1)).findByIsbn(ISBN);
        verify(commentRepository, times(1)).add(any(Comment.class));
        verify(messageService, times(1)).getMessage("add.comment", ISBN);
        verify(ioService, times(1)).outputData("Added");
    }

    @DisplayName("add comment test failed book not found")
    @Test
    public void addCommentTestFailedNoBook() {
        final long ISBN = 1L;
        final String COMMENT = "Comment";
        when(bookRepository.findByIsbn(ISBN)).thenReturn(Optional.ofNullable(null));
        when(messageService.getMessage("book.id.not.found", ISBN)).thenReturn("Not found");
        when(messageService.getMessage("add.comment.error", ISBN, "Not found")).thenReturn("Error");

        var error = Assertions.assertThrows(LibraryException.class, () -> {
            libraryService.addBookComment(ISBN, COMMENT);
        });
        Assertions.assertEquals("Error", error.getMessage());

        verify(bookRepository, times(1)).findByIsbn(ISBN);
        verify(commentRepository, never()).add(any(Comment.class));
        verify(messageService, times(1)).getMessage("book.id.not.found", ISBN);
        verify(messageService, times(1)).getMessage("add.comment.error", ISBN, "Not found");
    }

    @DisplayName("add comment test failed some error")
    @Test
    public void addCommentTestFailedSomeError() {
        final long ISBN = 1L;
        final String COMMENT = "Comment";
        Optional<Book> book = Optional.of(new Book(ISBN, "Book 1", 2019, new Author(), new Genre()));
        when(bookRepository.findByIsbn(ISBN)).thenReturn(book);
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("sql error"));
        doThrow(exception).when(commentRepository).add(any(Comment.class));
        when(messageService.getMessage("add.comment.error", ISBN,
                exception.getMessage())).thenReturn("Error");

        var error = Assertions.assertThrows(LibraryException.class, () -> {
            libraryService.addBookComment(ISBN, COMMENT);
        });
        Assertions.assertEquals("Error", error.getMessage());

        verify(bookRepository, times(1)).findByIsbn(ISBN);
        verify(commentRepository, times(1)).add(any(Comment.class));
        verify(messageService, times(1)).getMessage("add.comment.error",
                ISBN, exception.getMessage());
    }

    @DisplayName("find comment by book isbn test")
    @Test
    public void findCommentByBookTest() {
        final long ISBN = 1L;
        Book book = new Book(ISBN, "Book 1", 2019, new Author(), new Genre());
        Comment comment = new Comment(1, "Comment", book);
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);

        when(commentRepository.getCommentsOfBook(ISBN)).thenReturn(comments);
        String commentsStr = comments.stream().map(Comment::getComment).collect(Collectors.joining("\n"));
        when(messageService.getMessage("comment.info", book.getIsbn(), book.getName(), commentsStr))
                .thenReturn("Comments");
        doNothing().when(ioService).outputData("Comments");

        libraryService.findCommentByBookIdAndPrint(ISBN);

        verify(commentRepository, times(1)).getCommentsOfBook(ISBN);
        verify(messageService, times(1)).getMessage("comment.info",
                book.getIsbn(), book.getName(), commentsStr);
        verify(ioService, times(1)).outputData("Comments");
    }

    @DisplayName("find comment by book isbn test not found")
    @Test
    public void findCommentByBookTestNotFound() {
        final long ISBN = 1L;
        when(commentRepository.getCommentsOfBook(ISBN)).thenReturn(new ArrayList<>());
        when(messageService.getMessage("book.comment.not.found", ISBN))
                .thenReturn("Not found");
        doNothing().when(ioService).outputData("Not found");

        libraryService.findCommentByBookIdAndPrint(ISBN);

        verify(commentRepository, times(1)).getCommentsOfBook(ISBN);
        verify(messageService, times(1)).getMessage("book.comment.not.found", ISBN);
        verify(ioService, times(1)).outputData("Not found");
    }

    @DisplayName("find comment by book isbn test some error")
    @Test
    public void findCommentByBookTestSomeError() {
        final long ISBN = 1L;
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("sql error"));
        doThrow(exception).when(commentRepository).getCommentsOfBook(ISBN);
        when(messageService.getMessage("book.comment.not.found.error.reason", ISBN, exception.getMessage()))
                .thenReturn("Error");
        var error = Assertions.assertThrows(LibraryException.class, () -> {
            libraryService.findCommentByBookIdAndPrint(ISBN);
        });
        Assertions.assertEquals("Error", error.getMessage());

        verify(commentRepository, times(1)).getCommentsOfBook(ISBN);
        verify(messageService, times(1))
                .getMessage("book.comment.not.found.error.reason", ISBN, exception.getMessage());
    }

}

