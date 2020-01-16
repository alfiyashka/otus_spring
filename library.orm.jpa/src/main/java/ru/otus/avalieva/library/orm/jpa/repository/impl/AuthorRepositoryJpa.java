package ru.otus.avalieva.library.orm.jpa.repository.impl;

import org.springframework.stereotype.Repository;
import ru.otus.avalieva.library.orm.jpa.domain.Author;
import ru.otus.avalieva.library.orm.jpa.repository.AuthorRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class AuthorRepositoryJpa implements AuthorRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Author> getAll() {
        TypedQuery<Author> query = em.createQuery(
                "select a from Author a",
                Author.class);
        return query.getResultList();
    }

    @Override
    public void add(Author author) {
       em.persist(author);
    }

    @Override
    public Optional<Author>  findById(long id) {
        return Optional.ofNullable(em.find(Author.class, id));
    }

    @Override
    public Optional<Author> findByName(String firstName, String lastName) {
        TypedQuery<Author> query = em.createQuery(
                "select a from Author a where a.firstName = :firstname and a.lastName = :lastname",
                Author.class);
        query.setParameter("firstname", firstName);
        query.setParameter("lastname", lastName);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public void deleteById(long id) {
        Query query = em.createQuery("delete from Author a where a.id = :id ");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
