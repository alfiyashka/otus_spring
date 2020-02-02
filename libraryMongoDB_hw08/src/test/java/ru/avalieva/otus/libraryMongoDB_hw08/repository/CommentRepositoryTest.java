package ru.avalieva.otus.libraryMongoDB_hw08.repository;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import ru.avalieva.otus.libraryMongoDB_hw08.configuration.MongoSettings;
import ru.avalieva.otus.libraryMongoDB_hw08.configuration.MongockConfiguration;
import ru.avalieva.otus.libraryMongoDB_hw08.domain.Book;
import ru.avalieva.otus.libraryMongoDB_hw08.domain.Comment;
import ru.avalieva.otus.libraryMongoDB_hw08.events.MongoEventsException;
import ru.avalieva.otus.libraryMongoDB_hw08.service.MessageService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.avalieva.otus.libraryMongoDB_hw08.repository", "ru.avalieva.otus.libraryMongoDB_hw08.events"})
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
        Assertions.assertThrows(MongoEventsException.class, () -> { commentRepository.save(comment); });
    }

    @DisplayName("try add book with null author test")
    @Test
    public void addBookAuthorTestFailedUnknownAuthor() {
        Book book = new Book("ffffffffffffffffffffffff", "NEW BOOK", 1990, null, null);
        Comment comment = new Comment("", "Commment", book);
        Assertions.assertThrows(MongoEventsException.class, () -> {  commentRepository.save(comment); });
    }

    @DisplayName("try get comments of book test")
    @Test
    public void getCommentsOfBookTest() {
        List<Book> books = bookRepository.findAll();
        Book book = books.get(0);
        List<Comment> comments = commentRepository.getCommentsOfBook(book.getIsbn());
        final int EXPECTED_COMMENT_COUNT = 2;
        assertThat(comments).isNotNull().hasSize(EXPECTED_COMMENT_COUNT);
    }
}
