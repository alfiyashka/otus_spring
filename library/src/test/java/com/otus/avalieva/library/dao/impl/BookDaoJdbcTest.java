package com.otus.avalieva.library.dao.impl;

import com.otus.avalieva.library.dao.BookDao;
import com.otus.avalieva.library.domain.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import java.util.List;

@Import(BookDaoJdbc.class)
@JdbcTest
public class BookDaoJdbcTest {
    @Autowired
    private BookDao bookDao;

    @DisplayName("get all books test")
    @Test
    public void allBooksTest() {
        List<Book> books = bookDao.allBooks();
        Assertions.assertEquals(6, books.size());
    }

    @DisplayName("add and delete book test")
    @Test
    public void addAndDeleteBookAuthorTest() {
        long authorID = 1L;
        long genreID = 1L;
        Book newBook = new Book(10L, "NEW BOOK", 1990, authorID, genreID);
        bookDao.addBook(newBook);
        Assertions.assertEquals(7, bookDao.allBooks().size());

        bookDao.deleteBookById(newBook.getIsbn());
        Assertions.assertEquals(6, bookDao.allBooks().size());
    }

    @DisplayName("try add book with unknown author test")
    @Test
    public void addBookAuthorTestFailedUnknownAuthor() {
        long authorID = 10L;
        long genreID = 1L;
        Book newBook = new Book(10L, "NEW BOOK", 1990, authorID, genreID);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> { bookDao.addBook(newBook); });
    }

    @DisplayName("try add book with unknown genre test")
    @Test
    public void addBookAuthorTestFailedUnknownGenre() {
        long authorID = 1L;
        long genreID = 10L;
        Book newBook = new Book(10L, "NEW BOOK", 1990, authorID, genreID);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> { bookDao.addBook(newBook); });
    }

    @DisplayName("find book by isbn test")
    @Test
    public void findBooksByIsbnTest() {
        Book book = bookDao.findBookByIsbn(1L);
        Assertions.assertEquals("Vetra", book.getName());
    }

    @DisplayName("find book by name test")
    @Test
    public void findBooksByNameTest() {
        List<Book> books = bookDao.findBookByName("Vetra%");
        Assertions.assertEquals(2, books.size());
    }


    @DisplayName("find book by genre test")
    @Test
    public void findBooksByGenreTest() {
        List<Book> books = bookDao.findBookByGenre("DRAMA");
        Assertions.assertEquals(1, books.size());
    }

    @DisplayName("find book by author test")
    @Test
    public void findBooksByAuthorTest() {
        List<Book> books = bookDao.findBookByAuthor("Dmitry", "Tolstoy");
        Assertions.assertEquals(3, books.size());
    }
}
