package ru.avalieva.otus.library_hw10_ajax.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.avalieva.otus.library_hw10_ajax.domain.Author;
import ru.avalieva.otus.library_hw10_ajax.domain.Book;
import ru.avalieva.otus.library_hw10_ajax.domain.Comment;
import ru.avalieva.otus.library_hw10_ajax.domain.Genre;
import ru.avalieva.otus.library_hw10_ajax.dto.BookDTO;
import ru.avalieva.otus.library_hw10_ajax.dto.BookDtoConverter;
import ru.avalieva.otus.library_hw10_ajax.dto.CommentDTO;
import ru.avalieva.otus.library_hw10_ajax.dto.CommentDtoConverter;
import ru.avalieva.otus.library_hw10_ajax.service.LibraryService;
import ru.avalieva.otus.library_hw10_ajax.service.impl.LibraryException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@WebMvcTest({LibraryRestController.class})
public class LibraryControllerRestTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private LibraryService libraryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getBooksTest() throws Exception {
        Book book = new Book(0, "NEW BOOK", 1990, new Author(), new Genre());
        List<Book> bookList = new ArrayList<>();
        bookList.add(book);
        when(libraryService.allBooks()).thenReturn(bookList);

        String expected = objectMapper.writeValueAsString(bookList.stream().map(BookDtoConverter::convert)
                .collect(Collectors.toList()));

        MvcResult result =this.mvc.perform(get("/api/books").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals(expected,result.getResponse().getContentAsString());

        verify(libraryService, times(1)).allBooks();
    }

    @Test
    public void getBooksTestFailed() throws Exception {
        LibraryException exception = new LibraryException("error");
        when(libraryService.allBooks()).thenThrow(exception);

        MvcResult result =this.mvc.perform(get("/api/books").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals(exception.getMessage(), result.getResponse().getContentAsString());

        verify(libraryService, times(1)).allBooks();
    }

    @Test
    public void getAuthorsTest() throws Exception {
        Author author = new Author(0, "FIRST_NAME", "LAST_NAME", "12324324", "EMAIL");
        List<Author> authors = new ArrayList<>();
        authors.add(author);
        when(libraryService.allAuthors()).thenReturn(authors);

        String expected = objectMapper.writeValueAsString(authors);

        MvcResult result =this.mvc.perform(get("/api/authors").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals(expected, result.getResponse().getContentAsString());

        verify(libraryService, times(1)).allAuthors();
    }

    @Test
    public void getAuthorsFailed() throws Exception {
        LibraryException exception = new LibraryException("error");
        when(libraryService.allAuthors()).thenThrow(exception);

        MvcResult result =this.mvc.perform(get("/api/authors").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals(exception.getMessage(), result.getResponse().getContentAsString());

        verify(libraryService, times(1)).allAuthors();
    }

    @Test
    public void getGenresTest() throws Exception {
        Genre genre = new Genre(0,  "GENRE");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);
        when(libraryService.allGenres()).thenReturn(genres);

        String expected = objectMapper.writeValueAsString(genres);

        MvcResult result =this.mvc.perform(get("/api/genres").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals(expected, result.getResponse().getContentAsString());

        verify(libraryService, times(1)).allGenres();
    }

    @Test
    public void getGenresTestFailed() throws Exception {
        LibraryException exception = new LibraryException("error");
        when(libraryService.allGenres()).thenThrow(exception);

        MvcResult result =this.mvc.perform(get("/api/genres").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals(exception.getMessage(), result.getResponse().getContentAsString());

        verify(libraryService, times(1)).allGenres();
    }

    @Test
    public void getAllCommentsOfBookTest() throws Exception {
        long bookISBN = 1L;
        Comment comment = new Comment(1L, "COMMENT", new Book());
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        when(libraryService.findCommentByBookId(bookISBN)).thenReturn(comments);

        String expected = objectMapper.writeValueAsString(comments.stream()
                .map(CommentDtoConverter::convert).collect(Collectors.toList()));

        MvcResult result = this.mvc.perform(get("/api/comments/" + bookISBN).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals(expected, result.getResponse().getContentAsString());

        verify(libraryService, times(1)).findCommentByBookId(bookISBN);

    }

    @Test
    public void getAllCommentsOfBookTestFailed() throws Exception {
        long bookISBN = 1L;
        LibraryException exception = new LibraryException("error");
        when(libraryService.findCommentByBookId(bookISBN)).thenThrow(exception);

        MvcResult result =this.mvc.perform(get("/api/comments/" + bookISBN).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals(exception.getMessage(), result.getResponse().getContentAsString());

        verify(libraryService, times(1)).findCommentByBookId(bookISBN);
    }

    @Test
    public void findBookByNameTest() throws Exception {
        Book book = new Book(0, "NEW BOOK", 1990, new Author(), new Genre());
        List<Book> bookList = new ArrayList<>();
        bookList.add(book);
        when(libraryService.findBookByName(book.getName())).thenReturn(bookList);

        String expected = objectMapper.writeValueAsString(bookList.stream().map(BookDtoConverter::convert)
                .collect(Collectors.toList()));

        MvcResult result =this.mvc.perform(get("/api/book?name=" + book.getName()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals(expected,result.getResponse().getContentAsString());

        verify(libraryService, times(1)).findBookByName(book.getName());
    }

    @Test
    public void findBookByNameTestFailed() throws Exception {
        String bookName = "NAME";
        LibraryException exception = new LibraryException("error");
        when(libraryService.findBookByName(bookName)).thenThrow(exception);

        MvcResult result =this.mvc.perform(get("/api/book?name=" + bookName).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals(exception.getMessage(), result.getResponse().getContentAsString());

        verify(libraryService, times(1)).findBookByName(bookName);
    }

    @Test
    public void findBookByIdTest() throws Exception {
        Book book = new Book(0, "NEW BOOK", 1990, new Author(), new Genre());
        when(libraryService.findBookByISBN(book.getIsbn())).thenReturn(book);

        String expected = objectMapper.writeValueAsString(BookDtoConverter.convert(book));

        MvcResult result =this.mvc.perform(get("/api/book/" + book.getIsbn()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals(expected,result.getResponse().getContentAsString());

        verify(libraryService, times(1)).findBookByISBN(book.getIsbn());
    }

    @Test
    public void findBookByIdTestFailed() throws Exception {
        long isbn = 1;
        LibraryException exception = new LibraryException("error");
        when(libraryService.findBookByISBN(isbn)).thenThrow(exception);

        MvcResult result =this.mvc.perform(get("/api/book/" + isbn).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals(exception.getMessage(), result.getResponse().getContentAsString());

        verify(libraryService, times(1)).findBookByISBN(isbn);
    }

    @Test
    public void addNewBookTest() throws Exception {
        Author author = new Author(1, "FIRST_NAME", "LAST_NAME", "12324324", "EMAIL");
        Genre genre = new Genre(1,  "GENRE");
        Book book = new Book(1, "NEW BOOK", 1990, author, genre);
        BookDTO bookDTO = BookDtoConverter.convert(book);
        when(libraryService.findAuthorByID(1)).thenReturn(author);
        when(libraryService.findGenreByID(1)).thenReturn(genre);
        doNothing().when(libraryService).addNewBook(book);

        this.mvc.perform(post("/api/new/book")
                .content(objectMapper.writeValueAsString(bookDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(libraryService, times(1)).findAuthorByID(1);
        verify(libraryService, times(1)).findGenreByID(1);
        verify(libraryService, times(1)).addNewBook(book);
    }

    @Test
    public void editBookTest() throws Exception {
        Author author = new Author(1, "FIRST_NAME", "LAST_NAME", "12324324", "EMAIL");
        Genre genre = new Genre(1,  "GENRE");
        Book book = new Book(1, "NEW BOOK", 1990, author, genre);
        BookDTO bookDTO = BookDtoConverter.convert(book);
        when(libraryService.findAuthorByID(1)).thenReturn(author);
        when(libraryService.findGenreByID(1)).thenReturn(genre);
        doNothing().when(libraryService).addNewBook(book);

        this.mvc.perform(post("/api/edit/book")
                .content(objectMapper.writeValueAsString(bookDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(libraryService, times(1)).findAuthorByID(1);
        verify(libraryService, times(1)).findGenreByID(1);
        verify(libraryService, times(1)).addNewBook(book);
    }

    @Test
    public void addNewBookTestFailed() throws Exception {
        Author author = new Author(1, "FIRST_NAME", "LAST_NAME", "12324324", "EMAIL");
        Genre genre = new Genre(1,  "GENRE");
        Book book = new Book(1, "NEW BOOK", 1990, author, genre);
        BookDTO bookDTO = BookDtoConverter.convert(book);
        when(libraryService.findAuthorByID(1)).thenReturn(author);
        when(libraryService.findGenreByID(1)).thenReturn(genre);
        LibraryException exception = new LibraryException("error");
        doThrow(exception).when(libraryService).addNewBook(book);

        var result = this.mvc.perform(post("/api/new/book")
                .content(objectMapper.writeValueAsString(bookDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals(exception.getMessage(), result.getResponse().getContentAsString());

        verify(libraryService, times(1)).findAuthorByID(1);
        verify(libraryService, times(1)).findGenreByID(1);
        verify(libraryService, times(1)).addNewBook(book);
    }

    @Test
    public void addNewCommentTest() throws Exception {
        long bookISBN = 1L;
        CommentDTO commentDTO = new CommentDTO("COMMENT", bookISBN);
        doNothing().when(libraryService).addBookComment(commentDTO.getBookIsbn(), commentDTO.getComment());

        this.mvc.perform(post("/api/new/comment/book")
                .content(objectMapper.writeValueAsString(commentDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(libraryService, times(1)).addBookComment(commentDTO.getBookIsbn(), commentDTO.getComment());
    }

    @Test
    public void addNewCommentTestFailed() throws Exception {
        long bookISBN = 1L;
        CommentDTO commentDTO = new CommentDTO("COMMENT", bookISBN);
        LibraryException exception = new LibraryException("error");
        doThrow(exception).when(libraryService).addBookComment(commentDTO.getBookIsbn(), commentDTO.getComment());

        var result = this.mvc.perform(post("/api/new/comment/book")
                .content(objectMapper.writeValueAsString(commentDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals(exception.getMessage(), result.getResponse().getContentAsString());

        verify(libraryService, times(1)).addBookComment(commentDTO.getBookIsbn(), commentDTO.getComment());
    }

    @Test
    public void deleteBookTest() throws Exception {
        long bookISBN = 1L;
        doNothing().when(libraryService).deleteBook(bookISBN);

        this.mvc.perform(delete("/api/book/" + bookISBN))
                .andExpect(status().isOk());

        verify(libraryService, times(1)).deleteBook(bookISBN);
    }

    @Test
    public void deleteBookTestFailed() throws Exception {
        long bookISBN = 1L;
        LibraryException exception = new LibraryException("error");
        doThrow(exception).when(libraryService).deleteBook(bookISBN);

        var result = this.mvc.perform(delete("/api/book/" + bookISBN))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals(exception.getMessage(), result.getResponse().getContentAsString());

        verify(libraryService, times(1)).deleteBook(bookISBN);
    }

}
