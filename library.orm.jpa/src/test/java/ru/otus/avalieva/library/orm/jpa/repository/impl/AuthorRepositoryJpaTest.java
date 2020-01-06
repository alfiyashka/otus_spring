package ru.otus.avalieva.library.orm.jpa.repository.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.avalieva.library.orm.jpa.domain.Author;

import javax.persistence.PersistenceException;
import java.util.List;

@Import(AuthorRepositoryJpa.class)
@DataJpaTest
public class AuthorRepositoryJpaTest {
    @Autowired
    private AuthorRepositoryJpa authorRepository;

    @DisplayName("get all authors test")
    @Test
    public void allAuthorsTest() {
        List<Author> authors = authorRepository.getAll();
        final int EXPECTED_AUTHORS_COUNT = 4;
        Assertions.assertEquals(EXPECTED_AUTHORS_COUNT, authors.size());
    }

    @DisplayName("add and delete author test")
    @Test
    public void addAndDeleteAuthorTest() {
        Author newAuthor = new Author();
        newAuthor.setFirstName("FIRST_NAME");
        newAuthor.setLastName("LAST_NAME");
        newAuthor.setEmail("EMAIL");
        authorRepository.add(newAuthor);
        final int EXPECTED_AUTHORS_COUNT_AFTER_ADD = 5;
        Assertions.assertEquals(EXPECTED_AUTHORS_COUNT_AFTER_ADD, authorRepository.getAll().size());

        Author findByNameAuthor = authorRepository.findByName(newAuthor.getFirstName(), newAuthor.getLastName())
                .orElseThrow(() -> new AssertionFailedError("empty"));

        authorRepository.deleteById(findByNameAuthor.getId());
        final int EXPECTED_AUTHORS_COUNT_AFTER_DELETE = 4;
        Assertions.assertEquals(EXPECTED_AUTHORS_COUNT_AFTER_DELETE, authorRepository.getAll().size());
    }

    @DisplayName("find author by id test")
    @Test
    public void findAuthorTest() {
        long authorId = 1;
        Author foundAuthor = authorRepository.findById(authorId)
                .orElseThrow(() -> new AssertionFailedError("empty"));
        Assertions.assertEquals("Dmitry", foundAuthor.getFirstName());
        Assertions.assertEquals("Tolstoy", foundAuthor.getLastName());
    }


    @DisplayName("try to delete author, that is used, test")
    @Test
    public void deleteAuthorTestFailedCannotDeletedUsed() {
        long authorId = 1;
        Assertions.assertThrows(PersistenceException.class, () -> { authorRepository.deleteById(authorId); });
    }
}
