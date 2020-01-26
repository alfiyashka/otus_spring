package ru.avalieva.otus.libraryMongoDB_hw08.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import ru.avalieva.otus.libraryMongoDB_hw08.configuration.MongoSettings;
import ru.avalieva.otus.libraryMongoDB_hw08.configuration.MongockConfiguration;
import ru.avalieva.otus.libraryMongoDB_hw08.domain.Author;
import ru.avalieva.otus.libraryMongoDB_hw08.events.MongoEventsException;
import ru.avalieva.otus.libraryMongoDB_hw08.service.MessageService;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.avalieva.otus.libraryMongoDB_hw08.repository", "ru.avalieva.otus.libraryMongoDB_hw08.events"})
@Import({MongoSettings.class, MongockConfiguration.class})
public class AuthorRepositoryTest {

    @MockBean
    MessageService messageService;

    @Autowired
    private AuthorRepository authorRepository;

    @DisplayName("find author by firstName and lastName test")
    @Test
    public void findAuthorNameTest() {
        List<Author> authors = authorRepository.findAll();
        Author author = authors.get(0);
        Author foundAuthor = authorRepository.findByFirstNameAndLastName(author.getFirstName(), author.getLastName())
                .orElseThrow(() -> new AssertionFailedError("empty"));
        assertThat(foundAuthor).isNotNull()
                .matches(a -> a.getFirstName().equals(author.getFirstName()))
                .matches(a -> a.getLastName().equals(author.getLastName()));
    }

    @DisplayName("try to delete author, that is used, test")
    @Test
    public void deleteAuthorTestFailedCannotDeletedUsed() {
        List<Author> authors = authorRepository.findAll();
        Author author = authors.get(0);
        Assertions.assertThrows(MongoEventsException.class, () -> { authorRepository.deleteById(author.getId()); });
    }

    @DisplayName("try to delete author, that is already absent, test")
    @Test
    public void deleteAuthorTestFailedCannotDeletedAlreadyAbsent() {
        Assertions.assertThrows(MongoEventsException.class, () -> { authorRepository.deleteById("ffffffffffffffffffffffff"); });
    }

}
