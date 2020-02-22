package ru.avalieva.com.library_hw11_webflux.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.avalieva.com.library_hw11_webflux.service.LibraryService;

@Controller
public class LibraryPageController {

    public LibraryPageController() {
    }

    @GetMapping({"/books", "/"})
    public String getBooksPage() {
        return "books";
    }

    @GetMapping("/authors")
    public String getAuthorsPage() {
        return "authors";
    }

    @GetMapping("/genres")
    public String getGenresPage() {
        return "genres";
    }

    @GetMapping({"/book"})
    public String getSearchBookPage() {
        return "findbook";
    }

    @GetMapping({"/book/new"})
    public String getAddBookPage() {
        return "newbook";
    }

    @GetMapping({"/comments/book/{id}"})
    public String getCommentsPage(@PathVariable("id") String id) {
        return "comments";
    }

    @GetMapping("/add/comment/book/{id}")
    public String getNewCommentForBookPage(@PathVariable("id") String id) {
        return "newcomment";
    }

    @GetMapping("/book/edit/{id}")
    public String getEditBookPage(@PathVariable("id") String id) {
        return "edit_book";
    }
}

