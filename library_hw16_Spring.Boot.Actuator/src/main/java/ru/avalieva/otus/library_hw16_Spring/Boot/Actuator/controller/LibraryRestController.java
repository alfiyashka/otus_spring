package ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.controller;


import org.springframework.web.bind.annotation.*;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.domain.Book;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.domain.Comment;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.service.LibraryService;


@RestController
public class LibraryRestController {
    private final LibraryService libraryService;

    public LibraryRestController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }


    @PostMapping({"/api/book"})
    public void addNewBook(@RequestBody Book book) {
        libraryService.addNewBook(book);
    }

    @PostMapping("/api/book/{isbn}/comment")
    public void addNewComment(@PathVariable("isbn") long isbn,
                              @RequestBody Comment comment) {
        libraryService.addBookComment(isbn, comment.getComment());
    }

}
