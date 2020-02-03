package ru.avalieva.otus.library_hw09_mvc.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.avalieva.otus.library_hw09_mvc.domain.Author;
import ru.avalieva.otus.library_hw09_mvc.domain.Book;
import ru.avalieva.otus.library_hw09_mvc.domain.Comment;
import ru.avalieva.otus.library_hw09_mvc.domain.Genre;
import ru.avalieva.otus.library_hw09_mvc.dto.BookDTO;
import ru.avalieva.otus.library_hw09_mvc.dto.BookDtoConverter;
import ru.avalieva.otus.library_hw09_mvc.dto.CommentDTO;
import ru.avalieva.otus.library_hw09_mvc.service.LibraryService;

import java.util.List;

@Controller
public class LibraryController {

    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping({"/books", "/"})
    public String books(Model model) {
        List<Book> books = libraryService.allBooks();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/authors")
    public String authors(Model model) {
        List<Author> authors = libraryService.allAuthors();
        model.addAttribute("authors", authors);
        return "authors";
    }

    @GetMapping("/genres")
    public String genres(Model model) {
        List<Genre> genres = libraryService.allGenres();
        model.addAttribute("genres", genres);
        return "genres";
    }

    @GetMapping("/comment/book")
    public String commentForBook(@RequestParam("id") long id, Model model) {
        List<Comment> comments = libraryService.findCommentByBookId(id);
        model.addAttribute("comments", comments);
        model.addAttribute("bookisbn", Long.toString(id));
        return "comments";
    }

    @GetMapping("/add/comment/book")
    public String newCommentForBookPage(@RequestParam("id") long id, Model model) {
        model.addAttribute("bookid", id);
        model.addAttribute("commentDTO", new CommentDTO());
        return "newcomment";
    }

    @PostMapping("/add/comment/book")
    public String newCommentForBook(@RequestParam("id") long id, @ModelAttribute CommentDTO commentDTO, Model model) {
        libraryService.addBookComment(id, commentDTO.getComment());
        model.addAttribute("bookisbn", id);
        model.addAttribute("comments", libraryService.findCommentByBookId(id));
        return "comments";
    }


    @GetMapping("/book/edit")
    public String editBook(@RequestParam("id") long id, Model model) {
        Book book = libraryService.findBookByISBN(id);
        BookDTO bookDTO = BookDtoConverter.convert(book);
        model.addAttribute("bookDTO", bookDTO);
        return "edit_book";
    }

    @PostMapping({"/book/add", "/book/edit"})
    public String addBook(@ModelAttribute BookDTO bookDTO, Model model) {
        Book book = BookDtoConverter.convert(bookDTO);
        Author author = libraryService.findAuthorByID(bookDTO.getAuthorId());
        Genre genre = libraryService.findGenreByID(bookDTO.getGenreId());
        book.setAuthor(author);
        book.setGenre(genre);
        libraryService.addNewBook(book);
        List<Book> books = libraryService.allBooks();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping({"/book/add"})
    public String addBookPage(Model model) {
        model.addAttribute("bookDTO", new BookDTO());
        return "newbook";
    }

    @GetMapping({"/book"})
    public String searchBookPage(Model model) {
        model.addAttribute("book", new Book());
        return "findbook";
    }

    @PostMapping({"/book"})
    public String searchBook(@ModelAttribute Book book, Model model) {
        List<Book> books = libraryService.findBookByName(book.getName());
        model.addAttribute("books", books);
        return "findbook";
    }

    @GetMapping({"/book/delete/"})
    public String deleteBook(@RequestParam("id") long id, Model model) {
        libraryService.deleteBook(id);
        model.addAttribute("books", libraryService.allBooks());
        return "books";
    }

}
