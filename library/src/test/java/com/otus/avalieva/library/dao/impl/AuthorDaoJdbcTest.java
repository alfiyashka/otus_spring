package com.otus.avalieva.library.dao.impl;

import com.otus.avalieva.library.dao.AuthorDao;
import com.otus.avalieva.library.domain.Author;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

@Import(AuthorDaoJdbc.class)
@JdbcTest
public class AuthorDaoJdbcTest {
    @Autowired
    private AuthorDao authorDao;

    @DisplayName("get all authors test")
    @Test
    public void allAuthorsTest() {
        List<Author> authors = authorDao.allAuthors();
        Assertions.assertEquals(4, authors.size());
    }

    @DisplayName("add and delete author test")
    @Test
    public void addAndDeleteAuthorTest() {
        Author newAuthor = new Author();
        newAuthor.setFirstName("FIRST_NAME");
        newAuthor.setLastName("LAST_NAME");
        newAuthor.setEmail("EMAIL");
        authorDao.addAuthor(newAuthor);
        Assertions.assertEquals(5, authorDao.allAuthors().size());

        Author findByNameAuthor = authorDao.findAuthorByName(newAuthor.getFirstName(), newAuthor.getLastName());

        authorDao.deleteAuthorById(findByNameAuthor.getId());
        Assertions.assertEquals(4, authorDao.allAuthors().size());
    }

    @DisplayName("find author by id test")
    @Test
    public void findAuthorTest() {
        long authorId = 1;
        Author foundAuthor = authorDao.findAuthorById(authorId);
        Assertions.assertEquals("Dmitry", foundAuthor.getFirstName());
        Assertions.assertEquals("Tolstoy", foundAuthor.getLastName());
    }


    @DisplayName("try to delete author, that is used, test")
    @Test
    public void deleteAuthorTestFailedCannotDeletedUsed() {
        long authorId = 1;
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> { authorDao.deleteAuthorById(authorId); });
    }
}
