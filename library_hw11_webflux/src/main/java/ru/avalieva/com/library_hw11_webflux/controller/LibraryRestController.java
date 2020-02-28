package ru.avalieva.com.library_hw11_webflux.controller;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.avalieva.com.library_hw11_webflux.domain.Author;
import ru.avalieva.com.library_hw11_webflux.domain.Comment;
import ru.avalieva.com.library_hw11_webflux.domain.Genre;
import ru.avalieva.com.library_hw11_webflux.dto.BookDTO;
import ru.avalieva.com.library_hw11_webflux.dto.BookDtoConverter;
import ru.avalieva.com.library_hw11_webflux.dto.CommentDTO;
import ru.avalieva.com.library_hw11_webflux.dto.CommentDtoConverter;
import ru.avalieva.com.library_hw11_webflux.service.LibraryService;

@RestController
public class LibraryRestController {
    private final LibraryService libraryService;

    public LibraryRestController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("/api/authors")
    public Flux<Author> getAllAuthors() {
        return libraryService.allAuthors();
    }

    @GetMapping("/api/genres")
    public Flux<Genre> getAllGenre() {
        return libraryService.allGenres();
    }

    @GetMapping("/api/books/{isbn}/comments")
    public Flux<CommentDTO> getAllCommentsOfBook(@PathVariable("isbn") String isbn) {
        return libraryService.findCommentByBookId(isbn).map(CommentDtoConverter::convert);
    }

    @PostMapping("/api/books/{isbn}/comments")
    public Mono<CommentDTO> addNewComment(@PathVariable("isbn") String isbn,
                                          @RequestBody CommentDTO commentDTO) {
        return libraryService.addBookComment(commentDTO.getComment(), isbn).map(CommentDtoConverter::convert);
    }

    @DeleteMapping("/api/books/comments/{commentId}")
    public Mono<Void> dropCommentOfBook(@PathVariable("commentId") String commentId) {
        return libraryService.deleteComment(commentId);
    }

    @GetMapping("/api/books")
    public Flux<BookDTO> getAllBook() {
        return libraryService.allBooks().map(BookDtoConverter::convert);
    }

    @GetMapping(value = "/api/books", params = "name")
    public Flux<BookDTO> findBookByName(@RequestParam(value = "name", required = true) String bookName) {
        return libraryService.findBookByName(bookName).map(BookDtoConverter::convert);
    }

    @GetMapping("/api/books/{id}")
    public Mono<BookDTO> findBookById(@PathVariable("id") String bookId) {
        return libraryService.findBookByISBN(bookId).map(BookDtoConverter::convert);
    }

    @PostMapping({"/api/books"})
    public Mono<BookDTO> addNewBook(@RequestBody BookDTO bookDTO) {
        return libraryService.addNewBook(bookDTO).map(BookDtoConverter::convert);
    }

    @DeleteMapping({"/api/books/{isbn}"})
    public Mono<Void> deleteBook(@PathVariable("isbn") String isbn) {
        return libraryService.deleteBook(isbn);
    }
}
