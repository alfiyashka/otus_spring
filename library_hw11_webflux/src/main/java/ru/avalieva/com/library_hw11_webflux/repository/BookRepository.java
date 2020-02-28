package ru.avalieva.com.library_hw11_webflux.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.avalieva.com.library_hw11_webflux.domain.Author;
import ru.avalieva.com.library_hw11_webflux.domain.Book;
import ru.avalieva.com.library_hw11_webflux.domain.Genre;


public interface BookRepository extends ReactiveMongoRepository<Book, String> {
    Mono<Book> save(Mono<Book> book);
    Flux<Book> findByNameLike(String name);
    Flux<Book> findByGenre(Mono<Genre> genre);
    Flux<Book> findByAuthor(Mono<Author> author);
}
