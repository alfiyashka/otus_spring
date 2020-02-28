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
import ru.avalieva.com.library_hw11_webflux.domain.Comment;
import ru.avalieva.com.library_hw11_webflux.events.MongoEventsException;
import ru.avalieva.com.library_hw11_webflux.service.MessageService;



@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.avalieva.com.library_hw11_webflux.repository",
        "ru.avalieva.com.library_hw11_webflux.events"})
@Import({MongoSettings.class, MongockConfiguration.class})
public class CommentRepositoryTest {
    @MockBean
    MessageService messageService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    @DisplayName("try add comment with null book test")
    @Test
    public void addCommentTestFailedNullBook() {
        Comment comment = new Comment("", "Commment", null);
        Mono<Comment> commentMono = commentRepository.save(comment);
        StepVerifier
                .create(commentMono)
                .expectError(MongoEventsException.class)
                .verify();

    }


    @DisplayName("try get comments of book test")
    @Test
    public void getCommentsOfBookTest() {
        final String BOOK_NAME = "Human Anatomy";
        Flux<Book> books = bookRepository.findByNameLike(BOOK_NAME);
        Book book = books.blockFirst();
        Flux<Comment> comments = commentRepository.getCommentsOfBook(book.getIsbn());
        final int EXPECTED_COMMENT_COUNT = 2;
        StepVerifier
                .create(comments)
                .expectNextCount(EXPECTED_COMMENT_COUNT)
                .verifyComplete();
    }
}
