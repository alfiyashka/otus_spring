package ru.avalieva.com.library_hw11_webflux.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.avalieva.com.library_hw11_webflux.configuration.MongoSettings;
import ru.avalieva.com.library_hw11_webflux.configuration.MongockConfiguration;
import ru.avalieva.com.library_hw11_webflux.domain.Book;
import ru.avalieva.com.library_hw11_webflux.domain.Genre;
import ru.avalieva.com.library_hw11_webflux.service.MessageService;


import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.avalieva.com.library_hw11_webflux.repository",
        "ru.avalieva.com.library_hw11_webflux.events"})
@Import({MongoSettings.class, MongockConfiguration.class})
public class GenreRepositoryTest {
    @MockBean
    MessageService messageService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GenreRepository genreRepository;

    @DisplayName("find genre by name test")
    @Test
    public void findGenreNameTest() {
        Flux<Genre> genres = genreRepository.findAll();
        Genre genre = genres.blockFirst();
        Mono<Genre> foundGenre = genreRepository.findByGenreName(genre.getGenreName());
        StepVerifier
                .create(foundGenre)
                .assertNext(g -> {
                    assertEquals(genre.getGenreName(), g.getGenreName());
                })
                .expectComplete()
                .verify();
    }

    @DisplayName("try to delete genre, that is used, test")
    @Test
    public void deleteGenreTestFailedCannotDeletedUsed() {
        final String GENRE_VALUE="DRAMA";
        Mono<Genre> genreMono = genreRepository.findByGenreName(GENRE_VALUE);
        Genre genre = genreMono.block();

        final int EXPECTED_BOOKS_COUNT_BEFORE = 1;
        Flux<Book> bookFluxBefore = bookRepository.findByGenre(Mono.just(genre));
        StepVerifier
                .create(bookFluxBefore)
                .expectNextCount(EXPECTED_BOOKS_COUNT_BEFORE)
                .verifyComplete();

        Mono<Void> deleteResult = genreRepository.deleteById(genre.getId());
        StepVerifier
                .create(deleteResult)
                .thenConsumeWhile(t -> true)
                .verifyComplete();

        final int EXPECTED_BOOKS_COUNT_AFTER = 0;
        Flux<Book> bookFlux = bookRepository.findByGenre(Mono.just(genre));
        StepVerifier
                .create(bookFlux)
                .expectNextCount(EXPECTED_BOOKS_COUNT_AFTER)
                .thenConsumeWhile(t -> true)
                .verifyComplete();
    }
}
