package ru.avalieva.otus.hw14SpringBatch.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.avalieva.otus.hw14SpringBatch.model.mongo.BookMongo;

public interface BookRepository extends MongoRepository<BookMongo, String>, BookRepositoryCustom {

}
