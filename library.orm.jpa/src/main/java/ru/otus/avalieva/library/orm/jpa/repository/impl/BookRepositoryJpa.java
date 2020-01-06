package ru.otus.avalieva.library.orm.jpa.repository.impl;


import org.springframework.stereotype.Repository;
import ru.otus.avalieva.library.orm.jpa.domain.Book;
import ru.otus.avalieva.library.orm.jpa.repository.BookRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class BookRepositoryJpa implements BookRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Book> getAll() {
        TypedQuery<Book> query = em.createQuery(
                "select b from Book b",
                Book.class);
        return query.getResultList();
    }

    @Override
    public void add(Book book) {
        em.persist(book);
    }

    @Override
    public Optional<Book> findByIsbn(long id) {
        return Optional.ofNullable(em.find(Book.class, id));
    }

    @Override
    public List<Book> findByName(String name) {
        TypedQuery<Book> query = em.createQuery(
                "select b from Book b where b.name like :name",
                Book.class);
        query.setParameter("name", name);
        return query.getResultList();
    }

    @Override
    public List<Book> findByGenre(String genre) {
        TypedQuery<Book> query = em.createQuery(
                "select b from Book b JOIN FETCH b.genre g where g.genreName = :genre", Book.class);
        query.setParameter("genre", genre);
        return query.getResultList();
    }

    @Override
    public List<Book> findByAuthor(String firstName, String lastName) {
        TypedQuery<Book>  query = em.createQuery(
                "select b from Book b JOIN FETCH b.author a where a.firstName = :first_name and a.lastName = :last_name",
                Book.class);
        query.setParameter("first_name", firstName);
        query.setParameter("last_name", lastName);
        return query.getResultList();
    }

    @Override
    public void deleteById(long id) {
        Query query = em.createQuery("delete from Book b where b.isbn = :isbn ");
        query.setParameter("isbn", id);
        query.executeUpdate();
    }

}
