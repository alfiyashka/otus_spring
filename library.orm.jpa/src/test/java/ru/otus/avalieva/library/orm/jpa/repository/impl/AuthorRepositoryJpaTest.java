package ru.otus.avalieva.library.orm.jpa.repository.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.avalieva.library.orm.jpa.domain.Author;

import javax.persistence.PersistenceException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(AuthorRepositoryJpa.class)
@DataJpaTest
public class AuthorRepositoryJpaTest {
    @Autowired
    private AuthorRepositoryJpa authorRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("get all authors test")
    @Test
    public void allAuthorsTest() {
        List<Author> authors = authorRepository.getAll();
        final int EXPECTED_AUTHORS_COUNT = 4;
        assertThat(authors).isNotNull().hasSize(EXPECTED_AUTHORS_COUNT);
    }

    @DisplayName("add and delete author test")
    @Test
    public void addAndDeleteAuthorTest() {
        Author newAuthor = new Author();
        newAuthor.setFirstName("FIRST_NAME");
        newAuthor.setLastName("LAST_NAME");
        newAuthor.setEmail("EMAIL");
        authorRepository.add(newAuthor);
        assertThat(newAuthor.getId()).isGreaterThan(0);

        Author actualAuthor = em.find(Author.class, newAuthor.getId());
        assertThat(actualAuthor).isNotNull()
                .matches(a -> a.getFirstName() != null && a.getFirstName().equals(newAuthor.getFirstName()))
                .matches(a -> a.getLastName() != null && a.getLastName().equals(newAuthor.getLastName()))
                .matches(a -> a.getEmail() != null && a.getEmail().equals(newAuthor.getEmail()))
                .matches(a -> a.getPhoneNumber() == null);
        em.detach(actualAuthor);

        authorRepository.deleteById(newAuthor.getId());
        Author deletedAuthor = em.find(Author.class, newAuthor.getId());
        assertThat(deletedAuthor).isNull();
    }

    @DisplayName("find author by id test")
    @Test
    public void findAuthorTest() {
        long authorId = 1;
        Author foundAuthor = authorRepository.findById(authorId)
                .orElseThrow(() -> new AssertionFailedError("empty"));
        final String EXPECTED_FIRST_NAME = "Dmitry";
        final String EXPECTED_LAST_NAME = "Tolstoy";
        assertThat(foundAuthor).isNotNull()
                .matches(a -> a.getFirstName().equals(EXPECTED_FIRST_NAME))
                .matches(a -> a.getLastName().equals(EXPECTED_LAST_NAME));
    }


    @DisplayName("try to delete author, that is used, test")
    @Test
    public void deleteAuthorTestFailedCannotDeletedUsed() {
        long authorId = 1;
        Assertions.assertThrows(PersistenceException.class, () -> { authorRepository.deleteById(authorId); });
    }
}
