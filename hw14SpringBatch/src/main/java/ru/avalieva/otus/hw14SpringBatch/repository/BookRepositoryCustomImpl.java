package ru.avalieva.otus.hw14SpringBatch.repository;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.avalieva.otus.hw14SpringBatch.model.mongo.BookMongo;

import java.util.Optional;

@RequiredArgsConstructor
public class BookRepositoryCustomImpl implements BookRepositoryCustom {
    private final MongoTemplate mongoTemplate;

    @Override
    public Optional<BookMongo> getBookByNameAuthorGenre(String name, String authorId, String genreId) {
        Criteria criteria = new Criteria();
        criteria = criteria.and("author.$id").is(new ObjectId(authorId))
                .and("name").is(name)
                .and("genre.$id").is(new ObjectId(genreId));
        Query query = new Query(criteria);
        var books = mongoTemplate.find(query, BookMongo.class);
        return Optional.ofNullable(books.isEmpty() ? null : books.get(0));
    }
}
