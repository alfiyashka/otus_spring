package ru.avalieva.otus.library_hw10_ajax.repository;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import ru.avalieva.otus.library_hw10_ajax.LibraryHw10AjaxApplication;
import ru.avalieva.otus.library_hw10_ajax.domain.Book;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(LibraryHw10AjaxApplication.class)
@DataJpaTest
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager em;

    private SessionFactory sessionFactory;

    private final int EXPECTED_QUERIES_COUNT = 1;

    @DisplayName("get all books test")
    @Test
    public void allBooksTest() {
        List<Book> books = bookRepository.findAll();
        final int EXPECTED_BOOKS_COUNT = 6;
        assertThat(books).isNotNull().hasSize(EXPECTED_BOOKS_COUNT)
                .allMatch(a -> a.getAuthor() != null)
                .allMatch(a -> a.getGenre() != null);

        assertThat(sessionFactory.getStatistics().getPrepareStatementCount())
                .isEqualTo(EXPECTED_QUERIES_COUNT);
    }

    @BeforeEach
    void setUp() {
        sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        sessionFactory.getStatistics().clear();
    }

    @DisplayName("try add book with unknown author test")
    @Test
    public void addBookAuthorTestFailedUnknownAuthor() {
        long authorID = 10L;
        long genreID = 1L;
        Book newBook = new Book(0, "NEW BOOK", 1990, authorID, genreID);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> { bookRepository.save(newBook); });
    }

    @DisplayName("try add book with unknown genre test")
    @Test
    public void addBookAuthorTestFailedUnknownGenre() {
        long authorID = 1L;
        long genreID = 10L;
        Book newBook = new Book(0, "NEW BOOK", 1990, authorID, genreID);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> { bookRepository.save(newBook); });
    }

    @DisplayName("find book by name test")
    @Test
    public void findBooksByNameTest() {
        List<Book> books = bookRepository.findByNameLike("Vetra%");
        final int EXPECTED_BOOKS_COUNT = 2;
        Assertions.assertEquals(EXPECTED_BOOKS_COUNT, books.size());
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount())
                .isEqualTo(EXPECTED_QUERIES_COUNT);
    }

    @DisplayName("find book by genre test")
    @Test
    public void findBooksByGenreTest() {
        List<Book> books = bookRepository.findByGenre("DRAMA");
        final int EXPECTED_BOOKS_COUNT = 1;
        assertThat(books).isNotNull().hasSize(EXPECTED_BOOKS_COUNT);
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount())
                .isEqualTo(EXPECTED_QUERIES_COUNT);
    }

    @DisplayName("find book by author test")
    @Test
    public void findBooksByAuthorTest() {
        List<Book> books = bookRepository.findByAuthor("Dmitry", "Tolstoy");
        final int EXPECTED_BOOKS_COUNT = 3;
        assertThat(books).isNotNull().hasSize(EXPECTED_BOOKS_COUNT);
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount())
                .isEqualTo(EXPECTED_QUERIES_COUNT);
    }
}
