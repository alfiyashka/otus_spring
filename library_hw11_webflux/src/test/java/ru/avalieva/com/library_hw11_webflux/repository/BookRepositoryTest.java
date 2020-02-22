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
import ru.avalieva.com.library_hw11_webflux.domain.Comment;
import ru.avalieva.com.library_hw11_webflux.domain.Genre;
import ru.avalieva.com.library_hw11_webflux.events.MongoEventsException;
import ru.avalieva.com.library_hw11_webflux.service.MessageService;



@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.avalieva.com.library_hw11_webflux.repository",
        "ru.avalieva.com.library_hw11_webflux.events"})
@Import({MongoSettings.class, MongockConfiguration.class})
public class BookRepositoryTest {
    @MockBean
    private MessageService messageService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CommentRepository commentRepository;

    @DisplayName("try add book with null author test")
    @Test
    public void addBookAuthorTestFailedNullAuthor() {
        Flux<Genre> genres = genreRepository.findAll();
        Genre genre = genres.blockFirst();
        Book newBook = new Book("", "NEW BOOK", 1990, null, genre);
        Mono<Book> bookMono = bookRepository.save(newBook);
        StepVerifier
                .create(bookMono)
                .expectError(MongoEventsException.class)
                .verify();
    }

    @DisplayName("try add book with null genre test")
    @Test
    public void addBookAuthorTestFailedUnknownAuthor() {
        Flux<Genre> genres = genreRepository.findAll();
        Genre genre = genres.blockFirst();
        Book newBook = new Book("", "NEW BOOK", 1990, null, genre);
        Mono<Book> bookMono = bookRepository.save(newBook);
        StepVerifier
                .create(bookMono)
                .expectError(MongoEventsException.class)
                .verify();
    }

    @DisplayName("find book by name test")
    @Test
    public void findBooksByNameTest() {
        Flux<Book> bookFlux = bookRepository.findByNameLike("*My Life*");
        final int EXPECTED_BOOKS_COUNT = 2;
        StepVerifier
                .create(bookFlux)
                .expectNextCount(EXPECTED_BOOKS_COUNT)
                .expectComplete()
                .verify();
    }


    @DisplayName("find book by genre test")
    @Test
    public void findBooksByGenreTest() {
        Flux<Genre> genres = genreRepository.findAll();
        Genre genre = genres.blockFirst();
        Flux<Book> bookFlux = bookRepository.findByGenre(Mono.just(genre));
        final int EXPECTED_BOOKS_COUNT = 1;
        StepVerifier
                .create(bookFlux)
                .expectNextCount(EXPECTED_BOOKS_COUNT)
                .verifyComplete();
    }

    @DisplayName("find book by author test")
    @Test
    public void findBooksByAuthorTest() {
        Flux<Author> authors = authorRepository.findAll();
        Author author = authors.blockFirst();
        Flux<Book> bookFlux  = bookRepository.findByAuthor(Mono.just(author));
        final int EXPECTED_BOOKS_COUNT = 2;
        StepVerifier
                .create(bookFlux)
                .expectNextCount(EXPECTED_BOOKS_COUNT)
                .verifyComplete();
    }

    @DisplayName("try to delete book, cascade delete, test")
    @Test
    public void cascadeDeleteBookTest() {
        Flux<Book> books = bookRepository.findAll();
        Book book = books.blockFirst();
        final int EXPECTED_COMMENTS_COUNT_BEFORE = 2;
        Flux<Comment> commentFluxBefore = commentRepository.getCommentsOfBook(book.getIsbn());
        StepVerifier
                .create(commentFluxBefore)
                .expectNextCount(EXPECTED_COMMENTS_COUNT_BEFORE)
                .thenConsumeWhile(t -> true)
                .verifyComplete();

        Mono<Void> deleteResult = bookRepository.deleteById(book.getIsbn());
        StepVerifier
                .create(deleteResult)
                .thenConsumeWhile(t -> true)
                .verifyComplete();

        Flux<Comment> commentFlux = commentRepository.getCommentsOfBook(book.getIsbn());
        final int EXPECTED_COMMENTS_COUNT_AFTER = 0;
        StepVerifier
                .create(commentFlux)
                .expectNextCount(EXPECTED_COMMENTS_COUNT_AFTER)
                .thenConsumeWhile(t -> true)
                .verifyComplete();
    }
}
