package ru.otus.avalieva.library.orm.jpa.repository.impl;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.avalieva.library.orm.jpa.domain.Book;

import javax.persistence.PersistenceException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(BookRepositoryJpa.class)
@DataJpaTest
public class BookRepositoryJpaTest {
    @Autowired
    private BookRepositoryJpa bookRepository;

    @Autowired
    private TestEntityManager em;

    private SessionFactory sessionFactory;

    private final int EXPECTED_QUERIES_COUNT = 1;

    @BeforeEach
    void setUp() {
        sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        sessionFactory.getStatistics().clear();
    }

    @DisplayName("get all books test")
    @Test
    public void allBooksTest() {
        List<Book> books = bookRepository.getAll();
        final int EXPECTED_BOOKS_COUNT = 6;
        assertThat(books).isNotNull().hasSize(EXPECTED_BOOKS_COUNT)
                .allMatch(a -> a.getAuthor() != null)
                .allMatch(a -> a.getGenre() != null);

        assertThat(sessionFactory.getStatistics().getPrepareStatementCount())
                .isEqualTo(EXPECTED_QUERIES_COUNT);
    }

    @DisplayName("add and delete book test")
    @Test
    public void addAndDeleteBookAuthorTest() {
        long authorID = 1L;
        long genreID = 1L;
        Book newBook = new Book(0, "NEW BOOK", 1990, authorID, genreID);
        bookRepository.add(newBook);
        assertThat(newBook.getIsbn()).isGreaterThan(0);

        Book actualBook = em.find(Book.class, newBook.getIsbn());
        assertThat(actualBook).isNotNull().matches(b -> b.getName().equals(newBook.getName()))
                .matches(b -> b.getAuthor() != null && b.getAuthor().getId() == newBook.getAuthor().getId())
                .matches(b -> b.getGenre() != null && b.getGenre().getId() == newBook.getGenre().getId());
        em.detach(actualBook);

        bookRepository.deleteById(newBook.getIsbn());
        Book deletedBook = em.find(Book.class, newBook.getIsbn());
        assertThat(deletedBook).isNull();
    }

    @DisplayName("try add book with unknown author test")
    @Test
    public void addBookAuthorTestFailedUnknownAuthor() {
        long authorID = 10L;
        long genreID = 1L;
        Book newBook = new Book(0, "NEW BOOK", 1990, authorID, genreID);

        Assertions.assertThrows(PersistenceException.class, () -> { bookRepository.add(newBook); });
    }

    @DisplayName("try add book with unknown genre test")
    @Test
    public void addBookAuthorTestFailedUnknownGenre() {
        long authorID = 1L;
        long genreID = 10L;
        Book newBook = new Book(0, "NEW BOOK", 1990, authorID, genreID);

        Assertions.assertThrows(PersistenceException.class, () -> { bookRepository.add(newBook); });
    }

    @DisplayName("find book by isbn test")
    @Test
    public void findBooksByIsbnTest() {
        Book book = bookRepository.findByIsbn(1L)
                .orElseThrow(() -> new AssertionFailedError("empty"));
        Assertions.assertEquals("Vetra", book.getName());
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount())
                .isEqualTo(EXPECTED_QUERIES_COUNT);
    }

    @DisplayName("find book by name test")
    @Test
    public void findBooksByNameTest() {
        List<Book> books = bookRepository.findByName("Vetra%");
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
