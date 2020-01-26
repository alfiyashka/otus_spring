package ru.avalieva.otus.libraryMongoDB_hw08.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.avalieva.otus.libraryMongoDB_hw08.domain.Comment;

import java.util.List;

@Transactional
public interface CommentRepository extends MongoRepository<Comment, String> {

    @Query(value = "{ 'book.isbn' : :#{#id} }")
    List<Comment> getCommentsOfBook(@Param("id") String isbn);
}
