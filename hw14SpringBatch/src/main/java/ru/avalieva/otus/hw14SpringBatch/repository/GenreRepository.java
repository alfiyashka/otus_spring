package ru.avalieva.otus.hw14SpringBatch.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.avalieva.otus.hw14SpringBatch.model.mongo.GenreMongo;

import java.util.Optional;

public interface GenreRepository extends MongoRepository<GenreMongo, String> {
    Optional<GenreMongo> findByGenreName(String name);
}