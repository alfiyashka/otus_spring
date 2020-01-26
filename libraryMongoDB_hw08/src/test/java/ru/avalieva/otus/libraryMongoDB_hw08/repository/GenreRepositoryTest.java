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
import ru.avalieva.otus.libraryMongoDB_hw08.domain.Genre;
import ru.avalieva.otus.libraryMongoDB_hw08.events.MongoEventsException;
import ru.avalieva.otus.libraryMongoDB_hw08.service.MessageService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.avalieva.otus.libraryMongoDB_hw08.repository", "ru.avalieva.otus.libraryMongoDB_hw08.events"})
@Import({MongoSettings.class, MongockConfiguration.class})
public class GenreRepositoryTest {

    @MockBean
    MessageService messageService;

    @Autowired
    private GenreRepository genreRepository;

    @DisplayName("find genre by name test")
    @Test
    public void findGenreNameTest() {
        List<Genre> genres = genreRepository.findAll();
        Genre genre = genres.get(0);
        Genre foundGenre = genreRepository.findByGenreName(genre.getGenreName())
                .orElseThrow(() -> new AssertionFailedError("empty"));
        assertThat(foundGenre).isNotNull()
                .matches(a -> a.getGenreName().equals(genre.getGenreName()));
    }

    @DisplayName("try to delete genre, that is used, test")
    @Test
    public void deleteGenreTestFailedCannotDeletedUsed() {
        List<Genre> genres = genreRepository.findAll();
        Genre genre = genres.get(0);
        Assertions.assertThrows(MongoEventsException.class, () -> { genreRepository.deleteById(genre.getId()); });
    }

    @DisplayName("try to delete genre, that is already absent, test")
    @Test
    public void deleteGenreTestFailedCannotDeletedAlreadyAbsent() {
        Assertions.assertThrows(MongoEventsException.class, () -> { genreRepository.deleteById("ffffffffffffffffffffffff"); });
    }

}
