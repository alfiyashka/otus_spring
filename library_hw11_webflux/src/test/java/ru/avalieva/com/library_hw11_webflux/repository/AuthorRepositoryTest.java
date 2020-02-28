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
import ru.avalieva.com.library_hw11_webflux.domain.Author;
import ru.avalieva.com.library_hw11_webflux.domain.Book;
import ru.avalieva.com.library_hw11_webflux.service.MessageService;


import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.avalieva.com.library_hw11_webflux.repository",
        "ru.avalieva.com.library_hw11_webflux.events"})
@Import({MongoSettings.class, MongockConfiguration.class})
public class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @MockBean
    private MessageService messageService;

    @DisplayName("find author by firstName and lastName test")
    @Test
    public void findAuthorNameTest() {
        Flux<Author> authors = authorRepository.findAll();
        Author author = authors.blockFirst();
        Mono<Author> foundAuthor = authorRepository.findByFirstNameAndLastName(author.getFirstName(), author.getLastName());
        StepVerifier
                .create(foundAuthor)
                .assertNext(a -> {
                    assertEquals(author.getFirstName(), a.getFirstName());
                    assertEquals(author.getLastName() , a.getLastName());
                })
                .expectComplete()
                .verify();
    }

    @DisplayName("try to delete author, cascade delete, test")
    @Test
    public void cascadeDeleteAuthorTest() {
        final String AUTHOR_FIRSTNAME = "Michail";
        final String AUTHOR_LASTNAME = "Fry";
        Mono<Author> authorMono = authorRepository.findByFirstNameAndLastName(AUTHOR_FIRSTNAME, AUTHOR_LASTNAME);
        Author author = authorMono.block();
        final int EXPECTED_BOOKS_COUNT_BEFORE = 2;
        Flux<Book> bookFluxBefore = bookRepository.findByAuthor(Mono.just(author));
        StepVerifier
                .create(bookFluxBefore)
                .expectNextCount(EXPECTED_BOOKS_COUNT_BEFORE)
                .thenConsumeWhile(t -> true)
                .verifyComplete();

        Mono<Void> deleteResult = authorRepository.deleteById(author.getId());
        StepVerifier
                .create(deleteResult)
                .thenConsumeWhile(t -> true)
                .verifyComplete();

        final int EXPECTED_BOOKS_COUNT_AFTER = 0;
        Flux<Book> bookFlux = bookRepository.findByAuthor(Mono.just(author));
        StepVerifier
                .create(bookFlux)
                .expectNextCount(EXPECTED_BOOKS_COUNT_AFTER)
                .thenConsumeWhile(t -> true)
                .verifyComplete();
    }
}
