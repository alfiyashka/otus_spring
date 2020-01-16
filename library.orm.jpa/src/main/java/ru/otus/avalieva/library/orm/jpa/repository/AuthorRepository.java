package ru.otus.avalieva.library.orm.jpa.repository;

import ru.otus.avalieva.library.orm.jpa.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    List<Author> getAll();
    void add(Author author);
    Optional<Author> findById(long id);
    Optional<Author> findByName(String firstName, String lastName);
    void deleteById(long id);
}
