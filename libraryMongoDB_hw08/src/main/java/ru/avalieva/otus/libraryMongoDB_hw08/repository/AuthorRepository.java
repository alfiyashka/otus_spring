package ru.avalieva.otus.libraryMongoDB_hw08.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.avalieva.otus.libraryMongoDB_hw08.domain.Author;

import java.util.Optional;

@Transactional
public interface AuthorRepository extends MongoRepository<Author, String> {
    Optional<Author> findByFirstNameAndLastName(String firstName, String lastName);
}
