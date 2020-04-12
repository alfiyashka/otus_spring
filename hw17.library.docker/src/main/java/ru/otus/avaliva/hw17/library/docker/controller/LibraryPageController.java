package ru.otus.avaliva.hw17.library.docker.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class LibraryPageController {

    @GetMapping("/")
    public String indexPage() {
        return "index";
    }

    @GetMapping("/newuser")
    public String newUserPage() {
        return "newuser";
    }

    @GetMapping("/books")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String getBooksPage() {
        return "books";
    }

    @GetMapping("/authors")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String getAuthorsPage() {
        return "authors";
    }

    @GetMapping("/genres")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String getGenresPage() {
        return "genres";
    }

    @GetMapping({"/book"})
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String getSearchBookPage() {
        return "findbook";
    }

    @GetMapping({"/book/new"})
    @Secured({"ROLE_ADMIN"})
    public String getAddBookPage() {
        return "newbook";
    }

    @GetMapping({"/comments/book/{id}"})
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String getCommentsPage(@PathVariable("id") String id) {
        return "comments";
    }

    @GetMapping("/add/comment/book/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public String getNewCommentForBookPage(@PathVariable("id") String id) {
        return "newcomment";
    }

    @GetMapping("/book/edit/{id}")
    @Secured({"ROLE_ADMIN"})
    public String getEditBookPage(@PathVariable("id") String id) {
        return "edit_book";
    }

}

