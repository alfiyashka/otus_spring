package ru.avalieva.otus.hw18.library.web.client.controller;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.avalieva.otus.hw18.library.web.client.model.BookDTO;
import ru.avalieva.otus.hw18.library.web.client.model.CommentDTO;
import ru.avalieva.otus.hw18.library.web.client.feing.LibraryFeingController;
import ru.avalieva.otus.hw18.library.web.client.model.Author;
import ru.avalieva.otus.hw18.library.web.client.model.Genre;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/library")
public class LibraryWebController {

    private final LibraryFeingController feingController;

    @HystrixCommand(
            commandKey = "getAuthorsFromDB",
            fallbackMethod = "getEmptyAuthorsList")
    @GetMapping("/api/authors")
    public List<Author> getAllAuthors() {
        return feingController.getAllAuthors();
    }

    public List<Author> getEmptyAuthorsList() {
        return new ArrayList<>();
    }

    @HystrixCommand(
            commandKey = "getGenresFromDB",
            fallbackMethod = "getEmptyGenresList")
    @GetMapping("/api/genres")
    public List<Genre> getAllGenre() {
        return feingController.getAllGenres();
    }

    public List<Genre> getEmptyGenresList() {
        return new ArrayList<>();
    }

    @HystrixCommand(
            commandKey = "getCommentsFromDB",
            fallbackMethod = "getEmptyCommentsList")
    @GetMapping("/api/books/{isbn}/comments")
    public List<CommentDTO> getAllCommentsOfBook(@PathVariable("isbn") long isbn) {
        return feingController.getAllCommentsOfBook(isbn);
    }

    public List<CommentDTO> getEmptyCommentsList() {
        return new ArrayList<>();
    }

    @PostMapping("/api/books/{isbn}/comments")
    public void addNewComment(@PathVariable("isbn") long isbn,
                              @RequestBody CommentDTO commentDTO) {
        feingController.addNewComment(isbn, commentDTO);
    }


    @DeleteMapping("/api/books/comments/{commentId}")
    public void dropCommentOfBook(@PathVariable("commentId") long commentId) {
        feingController.dropCommentOfBook(commentId);
    }

    @HystrixCommand(
            commandKey = "getBookFromDB",
            fallbackMethod = "getEmptyBooksList")
    @GetMapping("/api/books")
    public List<BookDTO> getAllBook() {
        return feingController.getAllBooks();
    }

    public List<BookDTO> getEmptyBooksList() {
        return new ArrayList<>();
    }

    @HystrixCommand(
            commandKey = "getBookByNameFromDB",
            fallbackMethod = "getEmptyBooksByNameList")
    @GetMapping(value = "/api/books", params = "name")
    public List<BookDTO> findBookByName(@RequestParam(value = "name") String bookName) {
        return feingController.findBookByName(bookName);
    }

    public List<BookDTO> getBookByNameFromDB(String bookName) {
        return new ArrayList<>();
    }

    @GetMapping("/api/books/{id}")
    public BookDTO findBookById(@PathVariable("id") String bookId) {
        return feingController.findBookById(bookId);
    }

    @PostMapping({"/api/books"})
    public void addNewBook(@RequestBody BookDTO bookDTO) {
        feingController.addNewBook(bookDTO);
    }

    @DeleteMapping({"/api/books/{isbn}"})
    public void deleteBook(@PathVariable("isbn") long isbn) {
        feingController.deleteBook(isbn);
    }


}
