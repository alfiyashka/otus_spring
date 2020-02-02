package ru.avalieva.otus.libraryMongoDB_hw08.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import ru.avalieva.otus.libraryMongoDB_hw08.configuration.MongoSettings;
import ru.avalieva.otus.libraryMongoDB_hw08.configuration.MongockConfiguration;
import ru.avalieva.otus.libraryMongoDB_hw08.domain.Author;
import ru.avalieva.otus.libraryMongoDB_hw08.domain.Book;
import ru.avalieva.otus.libraryMongoDB_hw08.domain.Genre;
import ru.avalieva.otus.libraryMongoDB_hw08.events.MongoEventsException;
import ru.avalieva.otus.libraryMongoDB_hw08.service.MessageService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.avalieva.otus.libraryMongoDB_hw08.repository", "ru.avalieva.otus.libraryMongoDB_hw08.events"})
@Import({MongoSettings.class, MongockConfiguration.class})
public class BookRepositoryTest {
    @MockBean
    private MessageService messageService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @DisplayName("try add book with null author test")
    @Test
    public void addBookAuthorTestFailedNullAuthor() {
        List<Genre> genres = genreRepository.findAll();
        Genre genre = genres.get(0);
        Book newBook = new Book("", "NEW BOOK", 1990, null, genre);

        Assertions.assertThrows(MongoEventsException.class, () -> { bookRepository.save(newBook); });
    }

    @DisplayName("try add book with null author test")
    @Test
    public void addBookAuthorTestFailedUnknownAuthor() {
        List<Genre> genres = genreRepository.findAll();
        Genre genre = genres.get(0);
        Author unknownAuthor = new Author("ffffffffffffffffffffffff", "FailName", "FailLastName", null, null);
        Book newBook = new Book("", "NEW BOOK", 1990, unknownAuthor, genre);

        Assertions.assertThrows(MongoEventsException.class, () -> { bookRepository.save(newBook); });
    }

    @DisplayName("try add book with null genre test")
    @Test
    public void addBookAuthorTestFailedNullGenre() {
        List<Author> authors = authorRepository.findAll();
        Author author = authors.get(0);
        Book newBook = new Book("", "NEW BOOK", 1990, author, null);

        Assertions.assertThrows(MongoEventsException.class, () -> { bookRepository.save(newBook); });
    }

    @DisplayName("try add book with unknown genre test")
    @Test
    public void addBookAuthorTestFailedUnknownGenre() {
        List<Author> authors = authorRepository.findAll();
        Author author = authors.get(0);
        Genre unknownGenre = new Genre("ffffffffffffffffffffffff", "FailGenre");
        Book newBook = new Book("", "NEW BOOK", 1990, author, unknownGenre);

        Assertions.assertThrows(MongoEventsException.class, () -> { bookRepository.save(newBook); });
    }

    @DisplayName("find book by name test")
    @Test
    public void findBooksByNameTest() {
        List<Book> books = bookRepository.findByNameLike("*Vetra*");
        final int EXPECTED_BOOKS_COUNT = 2;
        Assertions.assertEquals(EXPECTED_BOOKS_COUNT, books.size());
    }


    @DisplayName("find book by genre test")
    @Test
    public void findBooksByGenreTest() {
        List<Genre> genres = genreRepository.findAll();
        Genre genre = genres.get(0);
        List<Book> books = bookRepository.findByGenre(genre);
        final int EXPECTED_BOOKS_COUNT = 1;
        assertThat(books).isNotNull().hasSize(EXPECTED_BOOKS_COUNT);
    }

    @DisplayName("find book by author test")
    @Test
    public void findBooksByAuthorTest() {
        List<Author> authors = authorRepository.findAll();
        Author author = authors.get(0);
        List<Book> books = bookRepository.findByAuthor(author);
        final int EXPECTED_BOOKS_COUNT = 3;
        assertThat(books).isNotNull().hasSize(EXPECTED_BOOKS_COUNT);
    }

    @DisplayName("try to delete book, that is used, test")
    @Test
    public void deleteBookTestFailedCannotDeletedUsed() {
        List<Book> books = bookRepository.findAll();
        Book book = books.get(0);
        Assertions.assertThrows(MongoEventsException.class, () -> { bookRepository.deleteById(book.getIsbn()); });
    }
}
