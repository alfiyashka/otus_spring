package ru.avalieva.otus.library_hw10_ajax.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.avalieva.otus.library_hw10_ajax.domain.Author;
import ru.avalieva.otus.library_hw10_ajax.domain.Book;
import ru.avalieva.otus.library_hw10_ajax.domain.Genre;
import ru.avalieva.otus.library_hw10_ajax.dto.BookDTO;
import ru.avalieva.otus.library_hw10_ajax.dto.BookDtoConverter;
import ru.avalieva.otus.library_hw10_ajax.dto.CommentDTO;
import ru.avalieva.otus.library_hw10_ajax.dto.CommentDtoConverter;
import ru.avalieva.otus.library_hw10_ajax.service.LibraryService;

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

    @GetMapping("/api/comments/{isbn}")
    public List<CommentDTO> getAllCommentsOfBook(@PathVariable("isbn") long isbn) {
        return libraryService.findCommentByBookId(isbn)
                .stream()
                .map(CommentDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/books")
    public List<BookDTO> getAllBook() {
        return libraryService.allBooks().stream().map(BookDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/book")
    public List<BookDTO> findBookByName(@RequestParam("name") String bookName) {
        List<Book> bookDTOS = libraryService.findBookByName(bookName);
        return bookDTOS.stream().map(BookDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/book/{id}")
    public BookDTO findBookById(@PathVariable("id") String bookId) {
        return BookDtoConverter.convert(libraryService.findBookByISBN(Long.parseLong(bookId)));
    }

    @PostMapping({"/api/new/book", "/api/edit/book"})
    public ResponseEntity<String> addNewBook(@RequestBody BookDTO bookDTO) {
        try {
            Book book = BookDtoConverter.convert(bookDTO);
            Author author = libraryService.findAuthorByID(bookDTO.getAuthorId());
            Genre genre = libraryService.findGenreByID(bookDTO.getGenreId());
            book.setAuthor(author);
            book.setGenre(genre);
            libraryService.addNewBook(book);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("{}");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/api/new/comment/book")
    public ResponseEntity<String> addNewComment(@RequestBody CommentDTO commentDTO) {
        try {
            libraryService.addBookComment(commentDTO.getBookIsbn(), commentDTO.getComment());
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("{}");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping({"/api/book/{isbn}"})
    public ResponseEntity<String> deleteBookToBooks(@PathVariable("isbn") long isbn) {
        try {
            libraryService.deleteBook(isbn);
            return ResponseEntity.ok().body("");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
