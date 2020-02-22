package ru.avalieva.com.library_hw11_webflux.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import ru.avalieva.com.library_hw11_webflux.domain.Comment;


public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {
    @Query(value = "{ 'book.isbn' : :#{#id} }")
    Flux<Comment> getCommentsOfBook(@Param("id") String isbn);
}
