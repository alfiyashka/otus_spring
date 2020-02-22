package ru.avalieva.com.library_hw11_webflux.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import ru.avalieva.com.library_hw11_webflux.domain.Genre;


public interface GenreRepository extends ReactiveMongoRepository<Genre, String> {
    Mono<Genre> findByGenreName(String name);
}
