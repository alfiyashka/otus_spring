package ru.avalieva.otus.hw18.library.web.client.feing;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.avalieva.otus.hw18.library.web.client.model.BookDTO;
import ru.avalieva.otus.hw18.library.web.client.model.CommentDTO;
import ru.avalieva.otus.hw18.library.web.client.model.Author;
import ru.avalieva.otus.hw18.library.web.client.model.Genre;

import java.util.List;

@FeignClient(name = "library-service")
public interface LibraryFeingController {

    @GetMapping("/api/authors")
    List<Author> getAllAuthors();

    @GetMapping("/api/genres")
    List<Genre> getAllGenres() ;

    @GetMapping("/api/books/{isbn}/comments")
    List<CommentDTO> getAllCommentsOfBook(@PathVariable("isbn") long isbn);

    @PostMapping("/api/books/{isbn}/comments")
    void addNewComment(@PathVariable("isbn") long isbn,
                       @RequestBody CommentDTO commentDTO);

    @DeleteMapping("/api/books/comments/{commentId}")
    void dropCommentOfBook(@PathVariable("commentId") long commentId);

    @GetMapping("/api/books")
    List<BookDTO> getAllBooks();

    @GetMapping(value = "/api/books", params = "name")
    List<BookDTO> findBookByName(@RequestParam(value = "name") String bookName);

    @GetMapping("/api/books/{id}")
    BookDTO findBookById(@PathVariable("id") String bookId);

    @PostMapping({"/api/books"})
    void addNewBook(@RequestBody BookDTO bookDTO);

    @DeleteMapping({"/api/books/{isbn}"})
    void deleteBook(@PathVariable("isbn") long isbn);
}
