package ru.avalieva.com.library_hw11_webflux.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import ru.avalieva.com.library_hw11_webflux.domain.Author;


public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {
    Mono<Author> findByFirstNameAndLastName(String firstName, String lastName);
}
