package ru.avalieva.com.library_hw11_webflux.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.avalieva.com.library_hw11_webflux.domain.Author;
import ru.avalieva.com.library_hw11_webflux.domain.Book;
import ru.avalieva.com.library_hw11_webflux.domain.Comment;
import ru.avalieva.com.library_hw11_webflux.domain.Genre;
import ru.avalieva.com.library_hw11_webflux.dto.BookDTO;

public interface LibraryService {
    Flux<Genre> allGenres();
    Flux<Book> allBooks();
    Flux<Author> allAuthors();

    Mono<Book> findBookByISBN(String isbn);
    Flux<Book> findBookByName(String name);
    Mono<Book> addNewBook(BookDTO bookDTO);
    Mono<Void> deleteBook(String isbn);

    Mono<Author> findAuthorByID(String id);

    Mono<Genre> findGenreByID(String id);

    Mono<Comment> addBookComment(String commentValue, String isbn);
    Mono<Void> deleteComment(String commentId);
    Flux<Comment> findCommentByBookId(String isbn);
}