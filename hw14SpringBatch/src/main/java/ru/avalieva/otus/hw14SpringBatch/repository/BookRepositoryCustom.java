package ru.avalieva.otus.hw14SpringBatch.repository;

import ru.avalieva.otus.hw14SpringBatch.model.mongo.BookMongo;

import java.util.Optional;

public interface BookRepositoryCustom {
    Optional<BookMongo> getBookByNameAuthorGenre(String name, String authorId, String genreId);

}
