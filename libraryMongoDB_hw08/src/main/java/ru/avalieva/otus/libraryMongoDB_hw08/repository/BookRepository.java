package ru.avalieva.otus.libraryMongoDB_hw08.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.avalieva.otus.libraryMongoDB_hw08.domain.Author;
import ru.avalieva.otus.libraryMongoDB_hw08.domain.Book;
import ru.avalieva.otus.libraryMongoDB_hw08.domain.Genre;

import java.util.List;

@Transactional
public interface BookRepository extends MongoRepository<Book, String> {
    List<Book> findByNameLike(String name);
    List<Book> findByGenre(Genre genre);
    List<Book>  findByAuthor(Author author);
}

