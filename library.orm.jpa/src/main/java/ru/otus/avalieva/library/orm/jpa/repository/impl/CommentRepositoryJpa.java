package ru.otus.avalieva.library.orm.jpa.repository.impl;

import org.springframework.stereotype.Repository;
import ru.otus.avalieva.library.orm.jpa.domain.Comment;
import ru.otus.avalieva.library.orm.jpa.repository.CommentRepository;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class CommentRepositoryJpa implements CommentRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void add(Comment comment) {
        em.persist(comment);
    }

    @Override
    public List<Comment> getCommentsOfBook(long isbn) {
        EntityGraph<?> entityGraph = em.getEntityGraph("CommentWithBook");
        TypedQuery<Comment> query = em.createQuery(
                "select c from Comment c JOIN FETCH c.book b where b.isbn = :id",
                Comment.class);
        query.setParameter("id", isbn);
        query.setHint("javax.persistence.fetchgraph", entityGraph);
        return query.getResultList();
    }

    @Override
    public void deleteById(long id) {

        Query query = em.createQuery("delete from Comment c where c.id = :id ");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public List<Comment> getAll() {
        EntityGraph<?> entityGraph = em.getEntityGraph("CommentWithBook");

        TypedQuery<Comment> query = em.createQuery(
                "select c from Comment c",
                Comment.class);
        query.setHint("javax.persistence.fetchgraph", entityGraph);
        return query.getResultList();
    }

}
