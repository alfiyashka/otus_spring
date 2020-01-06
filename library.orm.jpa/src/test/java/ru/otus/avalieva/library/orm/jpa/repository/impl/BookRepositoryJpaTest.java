package ru.otus.avalieva.library.orm.jpa.repository.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.avalieva.library.orm.jpa.domain.Book;

import javax.persistence.PersistenceException;
import java.util.List;

@Import(BookRepositoryJpa.class)
@DataJpaTest
public class BookRepositoryJpaTest {
    @Autowired
    private BookRepositoryJpa bookRepository;

    @DisplayName("get all books test")
    @Test
    public void allBooksTest() {
        List<Book> books = bookRepository.getAll();
        final int EXPECTED_BOOKS_COUNT = 6;
        Assertions.assertEquals(EXPECTED_BOOKS_COUNT, books.size());
    }

    @DisplayName("add and delete book test")
    @Test
    public void addAndDeleteBookAuthorTest() {
        long authorID = 1L;
        long genreID = 1L;
        Book newBook = new Book(0, "NEW BOOK", 1990, authorID, genreID);
        bookRepository.add(newBook);
        final int EXPECTED_BOOKS_COUNT_AFTER_ADD = 7;
        Assertions.assertEquals(EXPECTED_BOOKS_COUNT_AFTER_ADD, bookRepository.getAll().size());

        bookRepository.deleteById(newBook.getIsbn());
        final int EXPECTED_BOOKS_COUNT_AFTER_DELETE = 6;
        Assertions.assertEquals(EXPECTED_BOOKS_COUNT_AFTER_DELETE, bookRepository.getAll().size());
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
    }

    @DisplayName("find book by name test")
    @Test
    public void findBooksByNameTest() {
        List<Book> books = bookRepository.findByName("Vetra%");
        final int EXPECTED_BOOKS_COUNT = 2;
        Assertions.assertEquals(EXPECTED_BOOKS_COUNT, books.size());
    }


    @DisplayName("find book by genre test")
    @Test
    public void findBooksByGenreTest() {
        List<Book> books = bookRepository.findByGenre("DRAMA");
        final int EXPECTED_BOOKS_COUNT = 1;
        Assertions.assertEquals(EXPECTED_BOOKS_COUNT, books.size());
    }

    @DisplayName("find book by author test")
    @Test
    public void findBooksByAuthorTest() {
        List<Book> books = bookRepository.findByAuthor("Dmitry", "Tolstoy");
        final int EXPECTED_BOOKS_COUNT = 3;
        Assertions.assertEquals(EXPECTED_BOOKS_COUNT, books.size());
    }
}
