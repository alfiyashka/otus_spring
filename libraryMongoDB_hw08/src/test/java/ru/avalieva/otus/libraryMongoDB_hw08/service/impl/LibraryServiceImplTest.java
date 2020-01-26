package ru.avalieva.otus.libraryMongoDB_hw08.service.impl;

import com.mongodb.MongoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration")
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@ComponentScan({"ru.avalieva.otus.libraryMongoDB_hw08.repository",
        "ru.avalieva.otus.libraryMongoDB_hw08.service"})
@ExtendWith({SpringExtension.class})
public class LibraryServiceImplTest {
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
        genres.add(new Genre("id1", "GENRE"));
        genres.add(new Genre("id2", "GENRE2"));
        when(genreRepository.findAll()).thenReturn(genres);

        for (Genre genre : genres) {
            when(messageService.getMessage("genre.info", genre.getId(), genre.getGenreName()))
                    .thenReturn("Genre Info");
            doNothing().when(ioService).outputData("Genre Info");
        }
        libraryService.printAllGenres();

        verify(genreRepository, times(1)).findAll();
        for (Genre genre : genres) {
            verify(messageService, times(1))
                    .getMessage("genre.info", genre.getId(), genre.getGenreName());
        }
        verify(ioService, times(genres.size())).outputData("Genre Info");
    }


    @DisplayName("print all genres test failed")
    @Test
    public void printAllGenresTestFailed() {
        MongoException exception = new MongoException("error");
        when(genreRepository.findAll()).thenThrow(exception);
        when(messageService.getMessage("genre.info.error", exception.getMessage())).thenReturn("Error");

        var error = Assertions.assertThrows(LibraryException.class, () -> {
            libraryService.printAllGenres();
        });
        Assertions.assertEquals("Error", error.getMessage());

        verify(genreRepository, times(1)).findAll();
        verify(messageService, times(1)).getMessage("genre.info.error", exception.getMessage());
    }

    @DisplayName("print all books test")
    @Test
    public void printAllBooksTest() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("ID2", "Book 1", 2019, new Author(), new Genre()));
        books.add(new Book("ID1", "Book 2", 2019, new Author(), new Genre()));
        when(bookRepository.findAll()).thenReturn(books);

        for (Book book : books) {
            when(messageService.getMessage("book.info",
                    book.getIsbn(), book.getName(), book.authorFullName(), book.genreName()))
                    .thenReturn("Book Info");
            doNothing().when(ioService).outputData("Book Info");
        }
        libraryService.printAllBooks();

        verify(bookRepository, times(1)).findAll();
        for (Book book : books) {
            verify(messageService, times(1)).getMessage("book.info",
                    book.getIsbn(), book.getName(), book.authorFullName(), book.genreName());
        }
        verify(ioService, times(books.size())).outputData("Book Info");
    }

    @DisplayName("print all books test failed")
    @Test
    public void printAllBooksTestFailed() {
        MongoException exception = new MongoException("error");
        when(bookRepository.findAll()).thenThrow(exception);
        when(messageService.getMessage("book.info.error", exception.getMessage())).thenReturn("Error");

        var error = Assertions.assertThrows(LibraryException.class, () -> {
            libraryService.printAllBooks();
        });
        Assertions.assertEquals("Error", error.getMessage());

        verify(bookRepository, times(1)).findAll();
        verify(messageService, times(1)).getMessage("book.info.error", exception.getMessage());
    }

    @DisplayName("print all authors test")
    @Test
    public void printAllAuthorTest() {
        List<Author> authors = new ArrayList<>();
        authors.add(new Author("ID1", "Mary", "Panak", "23432435", "mary@mail.com"));
        authors.add(new Author("ID2", "Mark", "Smith", "11111", "mark@mail.com"));
        when(authorRepository.findAll()).thenReturn(authors);

        for (Author author : authors) {
            when(messageService.getMessage("author.info",
                    author.getId(), author.fullName(), author.getEmail(), author.getPhoneNumber()))
                    .thenReturn("Author Info");
            doNothing().when(ioService).outputData("Author Info");
        }
        libraryService.printAllAuthors();

        verify(authorRepository, times(1)).findAll();
        for (Author author : authors) {
            verify(messageService, times(1)).getMessage("author.info",
                    author.getId(), author.fullName(), author.getEmail(), author.getPhoneNumber());
        }
        verify(ioService, times(authors.size())).outputData("Author Info");
    }

    @DisplayName("print all authors test failed")
    @Test
    public void printAllAuthorsTestFailed() {
        MongoException exception = new MongoException("error");
        when(authorRepository.findAll()).thenThrow(exception);
        when(messageService.getMessage("author.info.error", exception.getMessage())).thenReturn("Error");

        var error = Assertions.assertThrows(LibraryException.class, () -> {
            libraryService.printAllAuthors();
        });
        Assertions.assertEquals("Error", error.getMessage());

        verify(authorRepository, times(1)).findAll();
        verify(messageService, times(1)).getMessage("author.info.error", exception.getMessage());
    }


    @DisplayName("find book by isbn test")
    @Test
    public void findBookByISBNAndPrintTest() {
        Optional<Book> book = Optional.of(new Book("ID1", "Book 1", 2019, new Author(), new Genre()));
        when(bookRepository.findById("ID1")).thenReturn(book);
        when(messageService.getMessage("book.info", book.get().getIsbn(), book.get().getName(),
                book.get().authorFullName(), book.get().genreName())).thenReturn("Book Info");
        doNothing().when(ioService).outputData("Book Info");

        libraryService.findBookByISBNAndPrint("ID1");

        verify(bookRepository, times(1)).findById("ID1");
        verify(messageService, times(1)).getMessage("book.info", book.get().getIsbn(),
                book.get().getName(), book.get().authorFullName(), book.get().genreName());
        verify(ioService, times(1)).outputData("Book Info");
    }


    @DisplayName("find book by isbn test failed")
    @Test
    public void findBookByISBNAndPrintTestFailed() {
        MongoException exception = new MongoException("error");
        when(bookRepository.findById("ID1")).thenThrow(exception);
        when(messageService.getMessage("book.id.not.found.error.cause",
                "ID1", exception.getMessage())).thenReturn("Error");

        var error = Assertions.assertThrows(LibraryException.class, () -> {
            libraryService.findBookByISBNAndPrint("ID1");
        });
        Assertions.assertEquals("Error", error.getMessage());

        verify(bookRepository, times(1)).findById("ID1");
        verify(messageService, times(1))
                .getMessage("book.id.not.found.error.cause", "ID1", exception.getMessage());
    }

    @DisplayName("find book by isbn test not found")
    @Test
    public void findBookByISBNAndPrintTestNotFound() {
        String isbn = "ID1";
        when(bookRepository.findById(isbn)).thenReturn(Optional.ofNullable(null));
        when(messageService.getMessage("book.id.not.found", isbn)).thenReturn("Not Found");

        when(messageService.getMessage("book.id.not.found.error.cause",
                isbn, "Not Found")).thenReturn("Error");

        var error = Assertions.assertThrows(LibraryException.class, () -> {
            libraryService.findBookByISBNAndPrint(isbn);
        });
        Assertions.assertEquals("Error", error.getMessage());

        verify(bookRepository, times(1)).findById(isbn);
        verify(messageService, times(1)).getMessage("book.id.not.found", isbn);
        verify(messageService, times(1))
                .getMessage("book.id.not.found.error.cause", isbn, "Not Found");
    }

    @DisplayName("find book by name test")
    @Test
    public void findBookByNameAndPrintTest() {
        String bookName = "BOOK 1";
        String isbn = "ID1";
        Book book = new Book(isbn, bookName, 2019, new Author(), new Genre());
        List<Book> books = new ArrayList<>();
        books.add(book);
        when(bookRepository.findByNameLike(bookName)).thenReturn(books);
        when(messageService.getMessage("book.info", book.getIsbn(), book.getName(),
                book.authorFullName(), book.genreName())).thenReturn("Book Info");
        doNothing().when(ioService).outputData("Book Info");

        libraryService.findBookByNameAndPrint(bookName);

        verify(bookRepository, times(1)).findByNameLike(bookName);
        verify(messageService, times(1)).getMessage("book.info",
                book.getIsbn(), book.getName(), book.authorFullName(), book.genreName());
        verify(ioService, times(1)).outputData("Book Info");
    }

    @DisplayName("not find any book by name test")
    @Test
    public void findBookByNameAndPrintTestResultEmpty() {
        String bookName = "BOOK 1";
        List<Book> books = new ArrayList<>();
        when(bookRepository.findByNameLike(bookName)).thenReturn(books);
        when(messageService.getMessage("book.name.not.found", bookName)).thenReturn("Not found");
        doNothing().when(ioService).outputData("Not found");

        libraryService.findBookByNameAndPrint(bookName);

        verify(bookRepository, times(1)).findByNameLike(bookName);
        verify(messageService, times(1)).getMessage("book.name.not.found", bookName);
        verify(ioService, times(1)).outputData("Not found");
    }

    @DisplayName("find book by name test failed")
    @Test
    public void findBookByNameAndPrintTestFailed() {
        MongoException exception = new MongoException("error");
        String bookName = "BOOK 1";
        when(bookRepository.findByNameLike(bookName)).thenThrow(exception);
        when(messageService.getMessage("book.name.not.found.error.reason",
                bookName, exception.getMessage())).thenReturn("Error");

        var error = Assertions.assertThrows(LibraryException.class, () -> {
            libraryService.findBookByNameAndPrint(bookName);
        });
        Assertions.assertEquals("Error", error.getMessage());

        verify(bookRepository, times(1)).findByNameLike(bookName);
        verify(messageService, times(1))
                .getMessage("book.name.not.found.error.reason", bookName, exception.getMessage());
    }

    @DisplayName("find book by author test")
    @Test
    public void findBookByAuthorAndPrintTest() {
        String authorFirstName = "Mary";
        String authorLastName = "Fox";
        Author author = new Author("ID1", authorFirstName, authorLastName, "234355", "mail");
        Book book = new Book("ID1", "BOOK 1", 2019, author, new Genre());
        List<Book> books = new ArrayList<>();
        books.add(book);
        when(authorRepository.findByFirstNameAndLastName(authorFirstName, authorLastName)).thenReturn(Optional.of(author));
        when(bookRepository.findByAuthor(author)).thenReturn(books);
        when(messageService.getMessage("book.info", book.getIsbn(), book.getName(),
                book.authorFullName(), book.genreName())).thenReturn("Book Info");
        doNothing().when(ioService).outputData("Book Info");

        libraryService.findBookByAuthorAndPrint(authorFirstName, authorLastName);

        verify(authorRepository, times(1)).findByFirstNameAndLastName(authorFirstName, authorLastName);
        verify(bookRepository, times(1)).findByAuthor(author);
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
        Author author = new Author("ID1", authorFirstName, authorLastName, "234355", "mail");
        when(authorRepository.findByFirstNameAndLastName(authorFirstName, authorLastName)).thenReturn(Optional.of(author));

        when(bookRepository.findByAuthor(author)).thenReturn(books);
        when(messageService.getMessage("book.author.not.found", authorFirstName, authorLastName)).thenReturn("Not found");
        doNothing().when(ioService).outputData("Not found");

        libraryService.findBookByAuthorAndPrint(authorFirstName, authorLastName);

        verify(authorRepository, times(1)).findByFirstNameAndLastName(authorFirstName, authorLastName);
        verify(bookRepository, times(1)).findByAuthor(author);
        verify(messageService, times(1))
                .getMessage("book.author.not.found", authorFirstName, authorLastName);
        verify(ioService, times(1)).outputData("Not found");
    }

    @DisplayName("find book by author test failed")
    @Test
    public void findBookByAuthorAndPrintTestFailed() {
        MongoException exception = new MongoException("error");
        String authorFirstName = "Mary";
        String authorLastName = "Fox";
        Author author = new Author("ID1", authorFirstName, authorLastName, "234355", "mail");
        when(authorRepository.findByFirstNameAndLastName(authorFirstName, authorLastName)).thenReturn(Optional.of(author));
        when(bookRepository.findByAuthor(author)).thenThrow(exception);
        when(messageService.getMessage("book.author.not.found.error.reason",
                authorFirstName, authorLastName, exception.getMessage())).thenReturn("Error");

        var error = Assertions.assertThrows(LibraryException.class, () -> {
            libraryService.findBookByAuthorAndPrint(authorFirstName, authorLastName);
        });
        Assertions.assertEquals("Error", error.getMessage());

        verify(authorRepository, times(1)).findByFirstNameAndLastName(authorFirstName, authorLastName);
        verify(bookRepository, times(1)).findByAuthor(author);
        verify(messageService, times(1))
                .getMessage("book.author.not.found.error.reason",
                        authorFirstName, authorLastName, exception.getMessage());
    }

    @DisplayName("find book by genre test")
    @Test
    public void findBookByGenreAndPrintTest() {
        String genreName = "GENRE";
        Genre genre = new Genre("ID1", genreName);
        Book book = new Book("ID1", "BOOK 1", 2019, new Author(), genre);
        List<Book> books = new ArrayList<>();
        books.add(book);
        when(genreRepository.findByGenreName(genreName)).thenReturn(Optional.of(genre));
        when(bookRepository.findByGenre(genre)).thenReturn(books);
        when(messageService.getMessage("book.info", book.getIsbn(), book.getName(),
                book.authorFullName(), book.genreName())).thenReturn("Book Info");
        doNothing().when(ioService).outputData("Book Info");

        libraryService.findBookByGenreAndPrint(genreName);

        verify(genreRepository, times(1)).findByGenreName(genreName);
        verify(bookRepository, times(1)).findByGenre(genre);
        verify(messageService, times(1)).getMessage("book.info",
                book.getIsbn(), book.getName(), book.authorFullName(), book.genreName());
        verify(ioService, times(1)).outputData("Book Info");
    }

    @DisplayName("not find any book by genre test")
    @Test
    public void findBookByGenreAndPrintTestResultEmpty() {
        String genreName = "GENRE";
        List<Book> books = new ArrayList<>();
        Genre genre = new Genre("ID1", genreName);
        when(genreRepository.findByGenreName(genreName)).thenReturn(Optional.of(genre));
        when(bookRepository.findByGenre(genre)).thenReturn(books);
        when(messageService.getMessage("book.genre.not.found", genreName)).thenReturn("Not found");
        doNothing().when(ioService).outputData("Not found");

        libraryService.findBookByGenreAndPrint(genreName);

        verify(genreRepository, times(1)).findByGenreName(genreName);
        verify(bookRepository, times(1)).findByGenre(genre);
        verify(messageService, times(1))
                .getMessage("book.genre.not.found", genreName);
        verify(ioService, times(1)).outputData("Not found");
    }

    @DisplayName("find book by genre test failed")
    @Test
    public void findBookByGenreAndPrintTestFailed() {
        MongoException exception = new MongoException("error");
        String genreName = "GENRE";
        Genre genre = new Genre("ID1", genreName);
        when(genreRepository.findByGenreName(genreName)).thenReturn(Optional.of(genre));
        when(bookRepository.findByGenre(genre)).thenThrow(exception);
        when(messageService.getMessage("book.genre.not.found.error.reason",
                genreName, exception.getMessage())).thenReturn("Error");

        var error = Assertions.assertThrows(LibraryException.class, () -> {
            libraryService.findBookByGenreAndPrint(genreName);
        });
        Assertions.assertEquals("Error", error.getMessage());

        verify(genreRepository, times(1)).findByGenreName(genreName);
        verify(bookRepository, times(1)).findByGenre(genre);
        verify(messageService, times(1))
                .getMessage("book.genre.not.found.error.reason", genreName, exception.getMessage());
    }

    @DisplayName("add book test")
    @Test
    public void addNewBookTest() {
        Author author = new Author("ID1", "Mary", "Fox", "234355", "mail");
        Book book = new Book("ID1", "BOOK 1", 2019, author, new Genre("ID1", "Genre"));
        when(bookRepository.save(book)).thenReturn(book);
        when(messageService.getMessage("book.added", book.getIsbn())).thenReturn("Book was added");
        doNothing().when(ioService).outputData("Book was added");

        libraryService.addNewBook(book);

        verify(bookRepository, times(1)).save(book);
        verify(messageService, times(1)).getMessage("book.added", book.getIsbn());
        verify(ioService, times(1)).outputData("Book was added");
    }


    @DisplayName("add book test failed")
    @Test
    public void addNewBookTestFailed() {
        Author author = new Author("ID1", "Mary", "Fox", "234355", "mail");
        Book book = new Book("ID1", "BOOK 1", 2019, author,
                new Genre("ID1", "Genre"));
        MongoException exception = new MongoException("error");
        doThrow(exception).when(bookRepository).save(book);
        when(messageService.getMessage("book.add.error", book.getName(), exception.getMessage()))
                .thenReturn("Book cannot be added");

        var error = Assertions.assertThrows(LibraryException.class, () -> {
            libraryService.addNewBook(book);
        });
        Assertions.assertEquals("Book cannot be added", error.getMessage());

        verify(bookRepository, times(1)).save(book);
        verify(messageService, times(1))
                .getMessage("book.add.error", book.getName(), exception.getMessage());
    }

    @DisplayName("add author test")
    @Test
    public void addNewAuthorTest() {
        Author author = new Author("ID1", "Mary", "Fox", "234355", "mail");
        when(authorRepository.save(author)).thenReturn(author);
        when(messageService.getMessage("author.added", author.fullName())).thenReturn("Author was added");
        doNothing().when(ioService).outputData("Author was added");

        libraryService.addNewAuthor(author);

        verify(authorRepository, times(1)).save(author);
        verify(messageService, times(1))
                .getMessage("author.added", author.fullName());
        verify(ioService, times(1)).outputData("Author was added");
    }

    @DisplayName("add author test failed")
    @Test
    public void addNewAuthorTestFailed() {
        Author author = new Author("ID1", "Mary", "Fox", "234355", "mail");
        MongoException exception = new MongoException("error");
        doThrow(exception).when(authorRepository).save(author);
        when(messageService.getMessage("author.added.error", author.fullName(), exception.getMessage()))
                .thenReturn("Author cannot be added");

        var error = Assertions.assertThrows(LibraryException.class, () -> {
            libraryService.addNewAuthor(author);
        });
        Assertions.assertEquals("Author cannot be added", error.getMessage());

        verify(authorRepository, times(1)).save(author);
        verify(messageService, times(1))
                .getMessage("author.added.error", author.fullName(), exception.getMessage());
    }

    @DisplayName("add genre test")
    @Test
    public void addNewGenreTest() {
        Genre genre = new Genre("ID1", "Genre");
        when(genreRepository.save(genre)).thenReturn(genre);
        when(messageService.getMessage("genre.added", genre.getGenreName())).thenReturn("Genre was added");
        doNothing().when(ioService).outputData("Genre was added");

        libraryService.addNewGenre(genre);

        verify(genreRepository, times(1)).save(genre);
        verify(messageService, times(1))
                .getMessage("genre.added", genre.getGenreName());
        verify(ioService, times(1)).outputData("Genre was added");
    }

    @DisplayName("add genre test failed")
    @Test
    public void addNewGenreTestFailed() {
        Genre genre = new Genre("ID1", "Genre");
        MongoException exception = new MongoException("error");
        doThrow(exception).when(genreRepository).save(genre);
        when(messageService.getMessage("genre.added.error", genre.getGenreName(), exception.getMessage()))
                .thenReturn("Genre cannot be added");

        var error = Assertions.assertThrows(LibraryException.class, () -> {
            libraryService.addNewGenre(genre);
        });
        Assertions.assertEquals("Genre cannot be added", error.getMessage());

        verify(genreRepository, times(1)).save(genre);
        verify(messageService, times(1))
                .getMessage("genre.added.error", genre.getGenreName(), exception.getMessage());
    }

    @DisplayName("delete book test")
    @Test
    public void deleteBookTest() {
        String isbn = "ID1";
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
        String isbn = "ID1";
        MongoException exception = new MongoException("error");
        doThrow(exception).when(bookRepository).deleteById(isbn);
        when(messageService.getMessage("book.deleted.error", isbn, exception.getMessage()))
                .thenReturn("Book cannot be deleted");

        var error = Assertions.assertThrows(LibraryException.class, () -> {
            libraryService.deleteBook(isbn);
        });
        Assertions.assertEquals("Book cannot be deleted", error.getMessage());

        verify(bookRepository, times(1)).deleteById(isbn);
        verify(messageService, times(1))
                .getMessage("book.deleted.error", isbn, exception.getMessage());
    }

    @DisplayName("delete author test")
    @Test
    public void deleteAuthorTest() {
        String authorId = "ID1";
        doNothing().when(authorRepository).deleteById(authorId);
        when(messageService.getMessage("author.deleted", authorId)).thenReturn("Author was deleted");
        doNothing().when(ioService).outputData("Author was deleted");

        libraryService.deleteAuthor(authorId);

        verify(authorRepository, times(1)).deleteById(authorId);
        verify(messageService, times(1)).getMessage("author.deleted", authorId);
        verify(ioService, times(1)).outputData("Author was deleted");
    }


    @DisplayName("delete author test failed")
    @Test
    public void deleteAuthorTestFailed() {
        String authorId = "ID1";
        MongoException exception = new MongoException("error");
        doThrow(exception).when(authorRepository).deleteById(authorId);
        when(messageService.getMessage("author.deleted.error", authorId, exception.getMessage()))
                .thenReturn("Error");

        var error = Assertions.assertThrows(LibraryException.class, () -> {
            libraryService.deleteAuthor(authorId);
        });
        Assertions.assertEquals("Error", error.getMessage());

        verify(authorRepository, times(1)).deleteById(authorId);
        verify(messageService, times(1)).
                getMessage("author.deleted.error", authorId, exception.getMessage());
    }

    @DisplayName("delete genre test")
    @Test
    public void deleteGenreTest() {
        String genreId = "ID1";
        doNothing().when(genreRepository).deleteById(genreId);
        when(messageService.getMessage("genre.deleted", genreId)).thenReturn("Genre was deleted");
        doNothing().when(ioService).outputData("Genre was deleted");

        libraryService.deleteGenre(genreId);

        verify(genreRepository, times(1)).deleteById(genreId);
        verify(messageService, times(1)).getMessage("genre.deleted", genreId);
        verify(ioService, times(1)).outputData("Genre was deleted");
    }


    @DisplayName("delete genre test failed")
    @Test
    public void deleteGenreTestFailed() {
        String genreId = "ID1";
        MongoException exception = new MongoException("error");
        doThrow(exception).when(genreRepository).deleteById(genreId);
        when(messageService.getMessage("genre.deleted.error", genreId, exception.getMessage()))
                .thenReturn("Error");

        var error = Assertions.assertThrows(LibraryException.class, () -> {
            libraryService.deleteGenre(genreId);
        });
        Assertions.assertEquals("Error", error.getMessage());

        verify(genreRepository, times(1)).deleteById(genreId);
        verify(messageService, times(1)).
                getMessage("genre.deleted.error", genreId, exception.getMessage());
    }

    @DisplayName("add comment test")
    @Test
    public void addCommentTest() {
        final String ISBN = "ID1";
        final String COMMENT = "Comment";
        Optional<Book> book = Optional.of(new Book(ISBN, "Book 1", 2019, new Author(), new Genre()));
        when(bookRepository.findById(ISBN)).thenReturn(book);
        when(commentRepository.save(any(Comment.class))).thenReturn(new Comment("ID1", COMMENT, book.get()));
        when(messageService.getMessage("add.comment", ISBN)).thenReturn("Added");
        doNothing().when(ioService).outputData("Added");

        libraryService.addBookComment(ISBN, COMMENT);

        verify(bookRepository, times(1)).findById(ISBN);
        verify(commentRepository, times(1)).save(any(Comment.class));
        verify(messageService, times(1)).getMessage("add.comment", ISBN);
        verify(ioService, times(1)).outputData("Added");
    }

    @DisplayName("add comment test failed book not found")
    @Test
    public void addCommentTestFailedNoBook() {
        final String ISBN = "ID1";
        final String COMMENT = "Comment";
        when(bookRepository.findById(ISBN)).thenReturn(Optional.ofNullable(null));
        when(messageService.getMessage("book.id.not.found", ISBN)).thenReturn("Not found");
        when(messageService.getMessage("add.comment.error", ISBN, "Not found")).thenReturn("Error");

        var error = Assertions.assertThrows(LibraryException.class, () -> {
            libraryService.addBookComment(ISBN, COMMENT);
        });
        Assertions.assertEquals("Error", error.getMessage());

        verify(bookRepository, times(1)).findById(ISBN);
        verify(commentRepository, never()).save(any(Comment.class));
        verify(messageService, times(1)).getMessage("book.id.not.found", ISBN);
        verify(messageService, times(1)).getMessage("add.comment.error", ISBN, "Not found");
    }

    @DisplayName("add comment test failed some error")
    @Test
    public void addCommentTestFailedSomeError() {
        final String ISBN = "ID1";
        final String COMMENT = "Comment";
        Optional<Book> book = Optional.of(new Book(ISBN, "Book 1", 2019, new Author(), new Genre()));
        when(bookRepository.findById(ISBN)).thenReturn(book);
        MongoException exception = new MongoException("error");
        doThrow(exception).when(commentRepository).save(any(Comment.class));
        when(messageService.getMessage("add.comment.error", ISBN,
                exception.getMessage())).thenReturn("Error");

        var error = Assertions.assertThrows(LibraryException.class, () -> {
            libraryService.addBookComment(ISBN, COMMENT);
        });
        Assertions.assertEquals("Error", error.getMessage());

        verify(bookRepository, times(1)).findById(ISBN);
        verify(commentRepository, times(1)).save(any(Comment.class));
        verify(messageService, times(1)).getMessage("add.comment.error",
                ISBN, exception.getMessage());
    }

    @DisplayName("find comment by book isbn test")
    @Test
    public void findCommentByBookTest() {
        final String ISBN = "ID1";
        Book book = new Book(ISBN, "Book 1", 2019, new Author(), new Genre());
        Comment comment = new Comment("ID1", "Comment", book);
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
        final String ISBN = "ID1";
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
        final String ISBN = "ID1";
        MongoException exception = new MongoException("error");
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