package ru.avalieva.otus.library_hw09_mvc.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.avalieva.otus.library_hw09_mvc.domain.Author;
import ru.avalieva.otus.library_hw09_mvc.domain.Book;
import ru.avalieva.otus.library_hw09_mvc.domain.Comment;
import ru.avalieva.otus.library_hw09_mvc.domain.Genre;
import ru.avalieva.otus.library_hw09_mvc.dto.BookDTO;
import ru.avalieva.otus.library_hw09_mvc.dto.BookDtoConverter;
import ru.avalieva.otus.library_hw09_mvc.dto.CommentDTO;
import ru.avalieva.otus.library_hw09_mvc.service.LibraryService;
import ru.avalieva.otus.library_hw09_mvc.service.impl.LibraryException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith({SpringExtension.class})
@WebMvcTest({LibraryController.class, LibraryControllerAdvice.class})
public class LibraryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LibraryService libraryService;

    @Test
    public void allBooksTest() throws Exception {
        Book book = new Book(0, "NEW BOOK", 1990, new Author(), new Genre());
        List<Book> bookList = new ArrayList<>();
        bookList.add(book);
        when(libraryService.allBooks()).thenReturn(bookList);

        this.mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", bookList));

        verify(libraryService, times(1)).allBooks();
    }

    @Test
    public void allBooksTestFailed() throws Exception {
        LibraryException exception = new LibraryException("error");
        when(libraryService.allBooks()).thenThrow(exception);

        this.mvc.perform(get("/books"))
                .andExpect(status().isInternalServerError())
                .andExpect(model().attribute("message", exception.getMessage()));

        verify(libraryService, times(1)).allBooks();
    }

    @Test
    public void allAuthorsTest() throws Exception {
        Author author = new Author(0, "FIRST_NAME", "LAST_NAME", "12324324", "EMAIL");
        List<Author> authors = new ArrayList<>();
        authors.add(author);
        when(libraryService.allAuthors()).thenReturn(authors);

        this.mvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("authors", authors));

        verify(libraryService, times(1)).allAuthors();
    }

    @Test
    public void allAuthorsTestFailed() throws Exception {
        LibraryException exception = new LibraryException("error");
        when(libraryService.allAuthors()).thenThrow(exception);

        this.mvc.perform(get("/authors"))
                .andExpect(status().isInternalServerError())
                .andExpect(model().attribute("message", exception.getMessage()));

        verify(libraryService, times(1)).allAuthors();
    }

    @Test
    public void allGenresTest() throws Exception {
        Genre genre = new Genre(0,  "GENRE");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);
        when(libraryService.allGenres()).thenReturn(genres);

        this.mvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("genres", genres));

        verify(libraryService, times(1)).allGenres();
    }

    @Test
    public void allGenreTestFailed() throws Exception {
        LibraryException exception = new LibraryException("error");
        when(libraryService.allGenres()).thenThrow(exception);

        this.mvc.perform(get("/genres"))
                .andExpect(status().isInternalServerError())
                .andExpect(model().attribute("message", exception.getMessage()));

        verify(libraryService, times(1)).allGenres();
    }

    @Test
    public void  commentForBookTest() throws  Exception {
        long bookISBN = 1L;
        Comment comment = new Comment(1L, "COMMENT", new Book());
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        when(libraryService.findCommentByBookId(bookISBN)).thenReturn(comments);

        this.mvc.perform(get("/comment/book?id=1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("comments", comments))
                .andExpect(model().attribute("bookisbn", Long.toString(bookISBN)));

        verify(libraryService, times(1)).findCommentByBookId(bookISBN);
    }

    @Test
    public void commentForBookTestFailed() throws  Exception {
        long bookISBN = 1L;
        LibraryException exception = new LibraryException("error");
        when(libraryService.findCommentByBookId(bookISBN)).thenThrow(exception);

        this.mvc.perform(get("/comment/book?id=1"))
                .andExpect(status().isInternalServerError())
                .andExpect(model().attribute("message", exception.getMessage()));

        verify(libraryService, times(1)).findCommentByBookId(bookISBN);
    }

    @Test
    public void newCommentForBookPageTest() throws Exception {
        this.mvc.perform(get("/add/comment/book?id=1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("bookid", 1L))
                .andExpect(model().attribute("commentDTO", new CommentDTO()));
    }

    @Test
    public void newCommentForBookTest() throws Exception {
        long bookISBN = 1L;
        CommentDTO commentDTO = new CommentDTO("COMMENT");
        Comment comment = new Comment(1L, commentDTO.getComment(), new Book());
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        doNothing().when(libraryService).addBookComment(bookISBN, commentDTO.getComment());
        when(libraryService.findCommentByBookId(bookISBN)).thenReturn(comments);

        this.mvc.perform(post("/add/comment/book?id=1").flashAttr("commentDTO", commentDTO))
                .andExpect(status().isOk())
                .andExpect(model().attribute("comments", comments));

        verify(libraryService, times(1)).addBookComment(bookISBN, commentDTO.getComment());
        verify(libraryService, times(1)).findCommentByBookId(bookISBN);
    }

    @Test
    public void newCommentForBookTestFailed() throws Exception {
        long bookISBN = 1L;
        CommentDTO commentDTO = new CommentDTO("COMMENT");
        LibraryException exception = new LibraryException("error");
        doThrow(exception).when(libraryService).addBookComment(bookISBN, commentDTO.getComment());

        this.mvc.perform(post("/add/comment/book?id=1").flashAttr("commentDTO", commentDTO))
                .andExpect(status().isInternalServerError())
                .andExpect(model().attribute("message", exception.getMessage()));

        verify(libraryService, times(1)).addBookComment(bookISBN, commentDTO.getComment());
        verify(libraryService, never()).findCommentByBookId(anyLong());
    }

    @Test
    public void getEditBookPageTest() throws Exception {
        long isbn = 1;
        Book book = new Book(isbn, "NEW BOOK", 1990, new Author(), new Genre());

        when(libraryService.findBookByISBN(isbn)).thenReturn(book);

        BookDTO bookDTO = BookDtoConverter.convert(book);
        this.mvc.perform(get("/book/edit?id=1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("bookDTO", bookDTO));

        verify(libraryService, times(1)).findBookByISBN(isbn);
    }

    @Test
    public void getEditBookPageTestFailed() throws Exception {
        long isbn = 1l;
        LibraryException exception = new LibraryException("error");
        when(libraryService.findBookByISBN(isbn)).thenThrow(exception);

        this.mvc.perform(get("/book/edit?id=1"))
                .andExpect(status().isInternalServerError())
                .andExpect(model().attribute("message", exception.getMessage()));

        verify(libraryService, times(1)).findBookByISBN(isbn);
    }

    @Test
    public void addBookTest() throws Exception {
        Author author = new Author(1, "FIRST_NAME", "LAST_NAME", "12324324", "EMAIL");
        Genre genre = new Genre(1,  "GENRE");
        Book book = new Book(1, "NEW BOOK", 1990, author, genre);
        BookDTO bookDTO = BookDtoConverter.convert(book);
        List<Book> bookList = new ArrayList<>();
        bookList.add(book);
        when(libraryService.allBooks()).thenReturn(bookList);
        when(libraryService.findAuthorByID(1)).thenReturn(author);
        when(libraryService.findGenreByID(1)).thenReturn(genre);
        doNothing().when(libraryService).addNewBook(book);

        this.mvc.perform(post("/book/add").flashAttr("bookDTO", bookDTO))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", bookList));

        verify(libraryService, times(1)).findAuthorByID(1);
        verify(libraryService, times(1)).findGenreByID(1);
        verify(libraryService, times(1)).allBooks();
        verify(libraryService, times(1)).addNewBook(book);
    }

    @Test
    public void addBookTestFailed() throws Exception {
        Author author = new Author(1, "FIRST_NAME", "LAST_NAME", "12324324", "EMAIL");
        Genre genre = new Genre(1,  "GENRE");
        Book book = new Book(1, "NEW BOOK", 1990, author, genre);
        BookDTO bookDTO = BookDtoConverter.convert(book);
        when(libraryService.findAuthorByID(1)).thenReturn(author);
        when(libraryService.findGenreByID(1)).thenReturn(genre);
        LibraryException exception = new LibraryException("error");
        doThrow(exception).when(libraryService).addNewBook(book);

        this.mvc.perform(post("/book/add").flashAttr("bookDTO", bookDTO))
                .andExpect(status().isInternalServerError())
                .andExpect(model().attribute("message", exception.getMessage()));

        verify(libraryService, times(1)).findAuthorByID(1);
        verify(libraryService, times(1)).findGenreByID(1);
        verify(libraryService, times(1)).addNewBook(book);
        verify(libraryService, never()).allBooks();
    }

    @Test
    public void editBookTest() throws Exception {
        Author author = new Author(1, "FIRST_NAME", "LAST_NAME", "12324324", "EMAIL");
        Genre genre = new Genre(1,  "GENRE");
        Book book = new Book(1, "NEW BOOK", 1990, author, genre);
        BookDTO bookDTO = BookDtoConverter.convert(book);
        List<Book> bookList = new ArrayList<>();
        bookList.add(book);
        when(libraryService.allBooks()).thenReturn(bookList);
        when(libraryService.findAuthorByID(1)).thenReturn(author);
        when(libraryService.findGenreByID(1)).thenReturn(genre);
        doNothing().when(libraryService).addNewBook(book);

        this.mvc.perform(post("/book/edit").flashAttr("bookDTO", bookDTO))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", bookList));

        verify(libraryService, times(1)).findAuthorByID(1);
        verify(libraryService, times(1)).findGenreByID(1);
        verify(libraryService, times(1)).allBooks();
        verify(libraryService, times(1)).addNewBook(book);
    }

    @Test
    public void addBookPageTest() throws Exception {
        this.mvc.perform(get("/book/add"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("bookDTO", new BookDTO()));
    }

    @Test
    public void searchBookPageTest() throws Exception {
        this.mvc.perform(get("/book"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("book", new Book()));
    }

    @Test
    public void searchBookTest() throws Exception {
        Book book = new Book(1, "NEW BOOK", 1990, new Author(), new Genre());
        List<Book> bookList = new ArrayList<>();
        bookList.add(book);
        when(libraryService.findBookByName(book.getName())).thenReturn(bookList);

        this.mvc.perform(post("/book").flashAttr("book", book))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", bookList));

        verify(libraryService, times(1)).findBookByName(book.getName());
    }

    @Test
    public void searchBookTestFailed() throws Exception {
        Book book = new Book(1, "NEW BOOK", 1990, new Author(), new Genre());
        LibraryException exception = new LibraryException("error");
        when(libraryService.findBookByName(book.getName())).thenThrow(exception);

        this.mvc.perform(post("/book").flashAttr("book", book))
                .andExpect(status().isInternalServerError())
                .andExpect(model().attribute("message", exception.getMessage()));

        verify(libraryService, times(1)).findBookByName(book.getName());
    }

    @Test
    public void deleteBookTest() throws Exception {
        long isbn = 1;
        Book book = new Book(isbn, "NEW BOOK", 1990, new Author(), new Genre());
        List<Book> bookList = new ArrayList<>();
        bookList.add(book);
        when(libraryService.allBooks()).thenReturn(bookList);
        doNothing().when(libraryService).deleteBook(isbn);

        this.mvc.perform(get("/book/delete/?id=1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", bookList));

        verify(libraryService, times(1)).deleteBook(isbn);
        verify(libraryService, times(1)).allBooks();
    }

    @Test
    public void deleteBookTestFailed() throws Exception {
        long isbn = 1;
        LibraryException exception = new LibraryException("error");
        doThrow(exception).when(libraryService).deleteBook(isbn);

        this.mvc.perform(get("/book/delete/?id=1"))
                .andExpect(status().isInternalServerError())
                .andExpect(model().attribute("message", exception.getMessage()));

        verify(libraryService, times(1)).deleteBook(isbn);
        verify(libraryService, never()).allBooks();
    }
}
