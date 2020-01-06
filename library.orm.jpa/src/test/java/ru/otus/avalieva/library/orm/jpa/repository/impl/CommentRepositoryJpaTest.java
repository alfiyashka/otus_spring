package ru.otus.avalieva.library.orm.jpa.repository.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.avalieva.library.orm.jpa.domain.Book;
import ru.otus.avalieva.library.orm.jpa.domain.Comment;

import java.util.List;

@Import(CommentRepositoryJpa.class)
@DataJpaTest
public class CommentRepositoryJpaTest {
    @Autowired
    private CommentRepositoryJpa commentRepository;

    @DisplayName("get all comments test")
    @Test
    public void allGenresTest() {
        List<Comment> comments = commentRepository.getAll();
        final int EXPECTED_COMMENT_COUNT = 2;
        Assertions.assertEquals(EXPECTED_COMMENT_COUNT, comments.size());
    }

    @DisplayName("add and delete comment test")
    @Test
    public void addAndDeleteAuthorTest() {
        Book book = new Book(1L, "NEW BOOK", 1990, 1L, 1L);
        Comment comment = new Comment(0, "COMMENT", book);
        commentRepository.add(comment);
        final int EXPECTED_COMMENT_COUNT_AFTER_ADD = 3;
        Assertions.assertEquals(EXPECTED_COMMENT_COUNT_AFTER_ADD, commentRepository.getAll().size());

        final long DELETED_COMMENT_ID = 3;
        commentRepository.deleteById(DELETED_COMMENT_ID);
        final int EXPECTED_COMMENT_COUNT_AFTER_DELETE = 2;
        Assertions.assertEquals(EXPECTED_COMMENT_COUNT_AFTER_DELETE, commentRepository.getAll().size());
    }

    @DisplayName("find comment by book isbn test")
    @Test
    public void findGenreTest() {
        long bookIsbn = 1L;
        List<Comment> comments = commentRepository.getCommentsOfBook(bookIsbn);
        final int EXPECTED_COMMENT_COUNT = 2;
        Assertions.assertEquals(EXPECTED_COMMENT_COUNT, comments.size());
    }

}
