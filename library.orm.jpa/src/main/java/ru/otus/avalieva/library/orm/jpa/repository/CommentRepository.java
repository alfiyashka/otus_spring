package ru.otus.avalieva.library.orm.jpa.repository;

import ru.otus.avalieva.library.orm.jpa.domain.Comment;

import java.util.List;

public interface CommentRepository {
    void add(Comment comment);
    List<Comment> getCommentsOfBook(long isbn);
    void deleteById(long id);
    List<Comment> getAll();
}
