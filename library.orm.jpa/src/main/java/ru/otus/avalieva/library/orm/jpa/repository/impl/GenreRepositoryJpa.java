package ru.otus.avalieva.library.orm.jpa.repository.impl;

import org.springframework.stereotype.Repository;
import ru.otus.avalieva.library.orm.jpa.domain.Genre;
import ru.otus.avalieva.library.orm.jpa.repository.GenreRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class GenreRepositoryJpa implements GenreRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Genre> getAll() {
        TypedQuery<Genre> query = em.createQuery(
                "select g from Genre g",
                Genre.class);
        return query.getResultList();
    }

    @Override
    public void add(Genre genre) {
        em.persist(genre);
    }

    @Override
    public Optional<Genre> findById(long id) {
        return Optional.ofNullable(em.find(Genre.class, id));
    }

    @Override
    public void delete(long id) {
        Query query = em.createQuery("delete from Genre g where g.id = :id ");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public Optional<Genre> findByName(String name) {
        TypedQuery<Genre> query = em.createQuery(
                "select g from Genre g where g.genreName = :name",
                Genre.class);
        query.setParameter("name", name);
        return Optional.ofNullable(query.getSingleResult());
    }
}
