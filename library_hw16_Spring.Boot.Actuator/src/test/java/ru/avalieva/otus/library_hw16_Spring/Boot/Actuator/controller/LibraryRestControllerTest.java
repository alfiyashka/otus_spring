package ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.controller;

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
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.domain.Author;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.domain.Book;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.domain.Comment;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.domain.Genre;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.service.LibraryService;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.service.impl.LibraryException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@WebMvcTest({LibraryRestController.class})
public class LibraryRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LibraryService libraryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void addNewBookTest() throws Exception {
        Author author = new Author(1, "FIRST_NAME", "LAST_NAME", "12324324", "EMAIL");
        Genre genre = new Genre(1,  "GENRE");
        Book book = new Book(1, "NEW BOOK", 1990, author, genre);
        doNothing().when(libraryService).addNewBook(any());

        this.mvc.perform(post("/api/book")
                .content(objectMapper.writeValueAsString(book))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(libraryService, times(1)).addNewBook(any());
    }

    @Test
    public void addNewBookTestFailed() throws Exception {
        Author author = new Author(1, "FIRST_NAME", "LAST_NAME", "12324324", "EMAIL");
        Genre genre = new Genre(1,  "GENRE");
        Book book = new Book(1, "NEW BOOK", 1990, author, genre);
        LibraryException exception = new LibraryException("error");
        doThrow(exception).when(libraryService).addNewBook(any());

        var result = this.mvc.perform(post("/api/book")
                .content(objectMapper.writeValueAsString(book))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals(exception.getMessage(), result.getResponse().getContentAsString());

        verify(libraryService, times(1)).addNewBook(any());
    }

    @Test
    public void addNewCommentTest() throws Exception {
        long bookISBN = 1L;
        Comment comment = new Comment(1, "COMMENT", new Book());
        doNothing().when(libraryService).addBookComment(bookISBN, comment.getComment());

        this.mvc.perform(post("/api/book/" + bookISBN +"/comment")
                .content(objectMapper.writeValueAsString(comment))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(libraryService, times(1)).addBookComment(bookISBN, comment.getComment());
    }

    @Test
    public void addNewCommentTestFailed() throws Exception {
        long bookISBN = 1L;
        Comment comment = new Comment(1, "COMMENT", new Book());
        LibraryException exception = new LibraryException("error");
        doThrow(exception).when(libraryService).addBookComment(bookISBN, comment.getComment());

        var result = this.mvc.perform(post("/api/book/" + bookISBN + "/comment")
                .content(objectMapper.writeValueAsString(comment))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals(exception.getMessage(), result.getResponse().getContentAsString());

        verify(libraryService, times(1)).addBookComment(bookISBN, comment.getComment());
    }
}
