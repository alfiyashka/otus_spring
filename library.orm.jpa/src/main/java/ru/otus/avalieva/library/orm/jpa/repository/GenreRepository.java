package ru.otus.avalieva.library.orm.jpa.repository;

import ru.otus.avalieva.library.orm.jpa.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    List<Genre> getAll();
    void add(Genre genre);
    Optional<Genre> findById(long id);
    void delete(long id);
    Optional<Genre> findByName(String name);
}
