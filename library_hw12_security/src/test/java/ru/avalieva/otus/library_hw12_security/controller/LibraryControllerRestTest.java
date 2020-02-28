package ru.avalieva.otus.library_hw12_security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.avalieva.otus.library_hw12_security.domain.Author;
import ru.avalieva.otus.library_hw12_security.domain.Book;
import ru.avalieva.otus.library_hw12_security.domain.Comment;
import ru.avalieva.otus.library_hw12_security.domain.Genre;
import ru.avalieva.otus.library_hw12_security.dto.BookDTO;
import ru.avalieva.otus.library_hw12_security.dto.BookDtoConverter;
import ru.avalieva.otus.library_hw12_security.dto.CommentDTO;
import ru.avalieva.otus.library_hw12_security.dto.CommentDtoConverter;
import ru.avalieva.otus.library_hw12_security.security.UserDetailsServiceImpl;
import ru.avalieva.otus.library_hw12_security.service.LibraryService;
import ru.avalieva.otus.library_hw12_security.service.MessageService;
import ru.avalieva.otus.library_hw12_security.service.UserService;
import ru.avalieva.otus.library_hw12_security.service.impl.LibraryException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@WebMvcTest({LibraryRestController.class, UserDetailsServiceImpl.class})
public class LibraryControllerRestTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private LibraryService libraryService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private MessageService messageService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
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

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
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

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
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

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
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

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
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

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
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

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void getAllCommentsOfBookTest() throws Exception {
        long bookISBN = 1L;
        Comment comment = new Comment(1L, "COMMENT", new Book());
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        when(libraryService.findCommentByBookId(bookISBN)).thenReturn(comments);

        String expected = objectMapper.writeValueAsString(comments.stream()
                .map(CommentDtoConverter::convert).collect(Collectors.toList()));

        MvcResult result = this.mvc.perform(get( "/api/books/" + bookISBN + "/comments").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals(expected, result.getResponse().getContentAsString());

        verify(libraryService, times(1)).findCommentByBookId(bookISBN);

    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void getAllCommentsOfBookTestFailed() throws Exception {
        long bookISBN = 1L;
        LibraryException exception = new LibraryException("error");
        when(libraryService.findCommentByBookId(bookISBN)).thenThrow(exception);

        MvcResult result = this.mvc.perform(get("/api/books/" + bookISBN + "/comments").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals(exception.getMessage(), result.getResponse().getContentAsString());

        verify(libraryService, times(1)).findCommentByBookId(bookISBN);
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void findBookByNameTest() throws Exception {
        Book book = new Book(0, "NEW BOOK", 1990, new Author(), new Genre());
        List<Book> bookList = new ArrayList<>();
        bookList.add(book);
        when(libraryService.findBookByName(book.getName())).thenReturn(bookList);

        String expected = objectMapper.writeValueAsString(bookList.stream().map(BookDtoConverter::convert)
                .collect(Collectors.toList()));

        MvcResult result =this.mvc.perform(get("/api/books?name=" + book.getName()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals(expected,result.getResponse().getContentAsString());

        verify(libraryService, times(1)).findBookByName(book.getName());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void findBookByNameTestFailed() throws Exception {
        String bookName = "NAME";
        LibraryException exception = new LibraryException("error");
        when(libraryService.findBookByName(bookName)).thenThrow(exception);

        MvcResult result =this.mvc.perform(get("/api/books?name=" + bookName).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals(exception.getMessage(), result.getResponse().getContentAsString());

        verify(libraryService, times(1)).findBookByName(bookName);
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void findBookByIdTest() throws Exception {
        Book book = new Book(0, "NEW BOOK", 1990, new Author(), new Genre());
        when(libraryService.findBookByISBN(book.getIsbn())).thenReturn(book);

        String expected = objectMapper.writeValueAsString(BookDtoConverter.convert(book));

        MvcResult result =this.mvc.perform(get("/api/books/" + book.getIsbn()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals(expected,result.getResponse().getContentAsString());

        verify(libraryService, times(1)).findBookByISBN(book.getIsbn());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void findBookByIdTestFailed() throws Exception {
        long isbn = 1;
        LibraryException exception = new LibraryException("error");
        when(libraryService.findBookByISBN(isbn)).thenThrow(exception);

        MvcResult result =this.mvc.perform(get("/api/books/" + isbn).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals(exception.getMessage(), result.getResponse().getContentAsString());

        verify(libraryService, times(1)).findBookByISBN(isbn);
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void addNewBookTest() throws Exception {
        Author author = new Author(1, "FIRST_NAME", "LAST_NAME", "12324324", "EMAIL");
        Genre genre = new Genre(1,  "GENRE");
        Book book = new Book(1, "NEW BOOK", 1990, author, genre);
        BookDTO bookDTO = BookDtoConverter.convert(book);
        when(libraryService.findAuthorByID(1)).thenReturn(author);
        when(libraryService.findGenreByID(1)).thenReturn(genre);
        doNothing().when(libraryService).addNewBook(book);

        this.mvc.perform(post("/api/books")
                .content(objectMapper.writeValueAsString(bookDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(libraryService, times(1)).findAuthorByID(1);
        verify(libraryService, times(1)).findGenreByID(1);
        verify(libraryService, times(1)).addNewBook(book);
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
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

        var result = this.mvc.perform(post("/api/books")
                .content(objectMapper.writeValueAsString(bookDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals(exception.getMessage(), result.getResponse().getContentAsString());

        verify(libraryService, times(1)).findAuthorByID(1);
        verify(libraryService, times(1)).findGenreByID(1);
        verify(libraryService, times(1)).addNewBook(book);
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void addNewCommentTest() throws Exception {
        long bookISBN = 1L;
        CommentDTO commentDTO = new CommentDTO("COMMENT", 1);
        doNothing().when(libraryService).addBookComment(bookISBN, commentDTO.getComment());

        this.mvc.perform(post("/api/books/" + bookISBN +"/comments")
                .content(objectMapper.writeValueAsString(commentDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(libraryService, times(1)).addBookComment(bookISBN, commentDTO.getComment());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void addNewCommentTestFailed() throws Exception {
        long bookISBN = 1L;
        CommentDTO commentDTO = new CommentDTO("COMMENT", 1);
        LibraryException exception = new LibraryException("error");
        doThrow(exception).when(libraryService).addBookComment(bookISBN, commentDTO.getComment());

        var result = this.mvc.perform(post("/api/books/" + bookISBN + "/comments")
                .content(objectMapper.writeValueAsString(commentDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals(exception.getMessage(), result.getResponse().getContentAsString());

        verify(libraryService, times(1)).addBookComment(bookISBN, commentDTO.getComment());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void deleteBookTest() throws Exception {
        long bookISBN = 1L;
        doNothing().when(libraryService).deleteBook(bookISBN);

        this.mvc.perform(delete("/api/books/" + bookISBN))
                .andExpect(status().isOk());

        verify(libraryService, times(1)).deleteBook(bookISBN);
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void deleteBookTestFailed() throws Exception {
        long bookISBN = 1L;
        LibraryException exception = new LibraryException("error");
        doThrow(exception).when(libraryService).deleteBook(bookISBN);

        var result = this.mvc.perform(delete("/api/books/" + bookISBN))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals(exception.getMessage(), result.getResponse().getContentAsString());

        verify(libraryService, times(1)).deleteBook(bookISBN);
    }


    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void deleteCommentTest() throws Exception {
        long commentId = 1L;
        doNothing().when(libraryService).deleteComment(commentId);

        this.mvc.perform(delete("/api/books/comments/" + commentId))
                .andExpect(status().isOk());

        verify(libraryService, times(1)).deleteComment(commentId);
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void deleteCommentTestFailed() throws Exception {
        long commentId = 1L;
        LibraryException exception = new LibraryException("error");
        doThrow(exception).when(libraryService).deleteComment(commentId);

        var result = this.mvc.perform(delete("/api/books/comments/" + commentId))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals(exception.getMessage(), result.getResponse().getContentAsString());

        verify(libraryService, times(1)).deleteComment(commentId);
    }
}
