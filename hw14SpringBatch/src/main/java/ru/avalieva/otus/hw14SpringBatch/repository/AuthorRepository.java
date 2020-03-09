package ru.avalieva.otus.hw14SpringBatch.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.avalieva.otus.hw14SpringBatch.model.mongo.AuthorMongo;

import java.util.Optional;

public interface AuthorRepository extends MongoRepository<AuthorMongo, String> {
    Optional<AuthorMongo> findByFirstNameAndLastName(String firstName, String lastName);
}
