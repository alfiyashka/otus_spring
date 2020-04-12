package ru.avalieva.otus.hw18.library.hystrix.controller;

import org.springframework.web.bind.annotation.*;
import ru.avalieva.otus.hw18.library.hystrix.domain.Author;
import ru.avalieva.otus.hw18.library.hystrix.domain.Book;
import ru.avalieva.otus.hw18.library.hystrix.domain.Genre;
import ru.avalieva.otus.hw18.library.hystrix.dto.BookDTO;
import ru.avalieva.otus.hw18.library.hystrix.dto.BookDtoConverter;
import ru.avalieva.otus.hw18.library.hystrix.dto.CommentDTO;
import ru.avalieva.otus.hw18.library.hystrix.dto.CommentDtoConverter;
import ru.avalieva.otus.hw18.library.hystrix.service.LibraryService;

import java.util.List;
import java.util.stream.Collectors;


@RestController
public class LibraryRestController {
    private final LibraryService libraryService;

    public LibraryRestController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("/api/authors")
    public List<Author> getAllAuthors() {
        return libraryService.allAuthors();
    }

    @GetMapping("/api/genres")
    public List<Genre> getAllGenre() {
        return libraryService.allGenres();
    }

    @GetMapping("/api/books/{isbn}/comments")
    public List<CommentDTO> getAllCommentsOfBook(@PathVariable("isbn") long isbn) {
        return libraryService.findCommentByBookId(isbn)
                .stream()
                .map(CommentDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @PostMapping("/api/books/{isbn}/comments")
    public void addNewComment(@PathVariable("isbn") long isbn,
                              @RequestBody CommentDTO commentDTO) {
        libraryService.addBookComment(isbn, commentDTO.getComment());
    }

    @DeleteMapping("/api/books/comments/{commentId}")
    public void dropCommentOfBook(@PathVariable("commentId") long commentId) {
        libraryService.deleteComment(commentId);
    }

    @GetMapping("/api/books")
    public List<BookDTO> getAllBook() {
        return libraryService.allBooks().stream().map(BookDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/api/books", params = "name")
    public List<BookDTO> findBookByName(@RequestParam(value = "name") String bookName) {
        List<Book> bookDTOS = libraryService.findBookByName(bookName);
        return bookDTOS.stream().map(BookDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/books/{id}")
    public BookDTO findBookById(@PathVariable("id") String bookId) {
        return BookDtoConverter.convert(libraryService.findBookByISBN(Long.parseLong(bookId)));
    }

    @PostMapping({"/api/books"})
    public void addNewBook(@RequestBody BookDTO bookDTO) {
        Book book = BookDtoConverter.convert(bookDTO);
        Author author = libraryService.findAuthorByID(bookDTO.getAuthorId());
        Genre genre = libraryService.findGenreByID(bookDTO.getGenreId());
        book.setAuthor(author);
        book.setGenre(genre);
        libraryService.addNewBook(book);
    }

    @DeleteMapping({"/api/books/{isbn}"})
    public void deleteBook(@PathVariable("isbn") long isbn) {
        libraryService.deleteBook(isbn);
    }
}

