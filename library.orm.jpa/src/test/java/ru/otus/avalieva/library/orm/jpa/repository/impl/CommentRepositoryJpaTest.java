package ru.otus.avalieva.library.orm.jpa.repository.impl;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.avalieva.library.orm.jpa.domain.Book;
import ru.otus.avalieva.library.orm.jpa.domain.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(CommentRepositoryJpa.class)
@DataJpaTest
public class CommentRepositoryJpaTest {
    @Autowired
    private CommentRepositoryJpa commentRepository;

    @Autowired
    private TestEntityManager em;

    private SessionFactory sessionFactory;
    private final int EXPECTED_QUERIES_COUNT = 1;

    @BeforeEach
    void setUp() {
        sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        sessionFactory.getStatistics().clear();
    }

    @DisplayName("get all comments test")
    @Test
    public void allCommentsTest() {
        List<Comment> comments = commentRepository.getAll();
        final int EXPECTED_COMMENT_COUNT = 2;
        assertThat(comments).isNotNull().hasSize(EXPECTED_COMMENT_COUNT);

        assertThat(sessionFactory.getStatistics().getPrepareStatementCount())
                .isEqualTo(EXPECTED_QUERIES_COUNT);
    }

    @DisplayName("add and delete comment test")
    @Test
    public void addAndDeleteCommentTest() {
        Book book = new Book(1L, "NEW BOOK", 1990, 1L, 1L);
        Comment comment = new Comment(0, "COMMENT", book);
        commentRepository.add(comment);
        assertThat(comment.getId()).isGreaterThan(0);

        Comment actualComment = em.find(Comment.class, comment.getId());
        assertThat(actualComment).isNotNull()
                .matches(c -> c.getComment().equals(comment.getComment()))
                .matches(c -> c.getBook() != null)
                .matches(c -> c.getBook().getName() != null && c.getBook().getName().equals(comment.getBook().getName()));
        em.detach(actualComment);

        commentRepository.deleteById(comment.getId());
        Comment deletedComment = em.find(Comment.class, comment.getId());
        assertThat(deletedComment).isNull();
    }

    @DisplayName("find comment by book isbn test")
    @Test
    public void findCommentTest() {
        long bookIsbn = 1L;
        List<Comment> comments = commentRepository.getCommentsOfBook(bookIsbn);
        final int EXPECTED_COMMENT_COUNT = 2;
        assertThat(comments).isNotNull().hasSize(EXPECTED_COMMENT_COUNT);

        assertThat(sessionFactory.getStatistics().getPrepareStatementCount())
                .isEqualTo(EXPECTED_QUERIES_COUNT);
    }

}
