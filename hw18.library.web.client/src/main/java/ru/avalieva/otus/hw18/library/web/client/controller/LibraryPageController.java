package ru.avalieva.otus.hw18.library.web.client.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class LibraryPageController {

    @GetMapping("/newuser")
    public String newUserPage() {
        return "newuser";
    }

    @GetMapping({"/", "/books"})
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



