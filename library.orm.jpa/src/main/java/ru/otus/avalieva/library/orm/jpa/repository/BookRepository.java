package ru.otus.avalieva.library.orm.jpa.repository;

import ru.otus.avalieva.library.orm.jpa.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    List<Book> getAll();
    void add(Book book);
    Optional<Book> findByIsbn(long id);
    List<Book> findByName(String name);
    List<Book> findByGenre(String genre);
    List<Book> findByAuthor(String firstName, String lastName);
    void deleteById(long id);
}
