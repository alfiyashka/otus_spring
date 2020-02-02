package ru.avalieva.otus.libraryMongoDB_hw08.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.avalieva.otus.libraryMongoDB_hw08.domain.Genre;

import java.util.Optional;

@Transactional
public interface GenreRepository extends MongoRepository<Genre, String> {
    Optional<Genre> findByGenreName(String name);
}