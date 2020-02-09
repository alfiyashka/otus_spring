package ru.avalieva.otus.library_hw10_ajax.repository;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.avalieva.otus.library_hw10_ajax.LibraryHw10AjaxApplication;
import ru.avalieva.otus.library_hw10_ajax.domain.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(LibraryHw10AjaxApplication.class)
@DataJpaTest
public class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

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
        List<Comment> comments = commentRepository.findAll();
        final int EXPECTED_COMMENT_COUNT = 2;
        assertThat(comments).isNotNull().hasSize(EXPECTED_COMMENT_COUNT);

        assertThat(sessionFactory.getStatistics().getPrepareStatementCount())
                .isEqualTo(EXPECTED_QUERIES_COUNT);
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
