package ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.test.context.ContextConfiguration;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.Application;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.domain.Author;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.domain.Book;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.domain.Comment;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.domain.Genre;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.repository.BookRepository;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.repository.CommentRepository;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.service.impl.LibraryException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ContextConfiguration(classes = Application.class)
@SpringBootTest
public class LibraryServiceTest {


    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private MessageService messageService;

    @Autowired
    private LibraryService libraryService;


    @Test
    public void addNewBookTest() {
        Book book = new Book(1L, "BOOK 1", 2019, new Author(), new Genre());
        when(bookRepository.save(book)).thenReturn(book);

        libraryService.addNewBook(book);

        verify(bookRepository, times(1)).save(book);

    }

    @Test
    public void addNewBookTestFailed() {
        Book book = new Book(1L, "BOOK 1", 2019, new Author(), new Genre());
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("error"));
        doThrow(exception).when(bookRepository).save(book);
        when(messageService.getMessage("book.add.error", book.getName(), exception.getMessage()))
                .thenReturn("Book cannot be added");

        var error = Assertions.assertThrows(LibraryException.class, () -> { libraryService.addNewBook(book); });
        Assertions.assertEquals("Book cannot be added", error.getMessage());


        verify(bookRepository, times(1)).save(book);
        verify(messageService, times(1))
                .getMessage("book.add.error", book.getName(), exception.getMessage());
    }


    @Test
    public void addCommentTest() {
        final long ISBN = 1L;
        final String COMMENT = "Comment";
        Optional<Book> book = Optional.of(new Book(ISBN, "Book 1", 2019, new Author(), new Genre()));
        when(bookRepository.findById(ISBN)).thenReturn(book);
        when(commentRepository.save(any(Comment.class))).thenReturn(new Comment(1L, COMMENT, book.get()));

        libraryService.addBookComment(ISBN, COMMENT);

        verify(bookRepository, times(1)).findById(ISBN);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    public void addCommentTestFailedNoBook() {
        final long ISBN = 1L;
        final String COMMENT = "Comment";
        when(bookRepository.findById(ISBN)).thenReturn(Optional.ofNullable(null));
        when(messageService.getMessage("book.id.not.found", ISBN)).thenReturn("Not found");
        when(messageService.getMessage("add.comment.error", ISBN, "Not found")).thenReturn("Error");

        var error = Assertions.assertThrows(LibraryException.class, () -> {
            libraryService.addBookComment(ISBN, COMMENT);
        });
        Assertions.assertEquals("Error", error.getMessage());

        verify(bookRepository, times(1)).findById(ISBN);
        verify(commentRepository, never()).save(any(Comment.class));
        verify(messageService, times(1)).getMessage("book.id.not.found", ISBN);
        verify(messageService, times(1)).getMessage("add.comment.error", ISBN, "Not found");
    }

    @Test
    public void addCommentTestFailedSomeError() {
        final long ISBN = 1L;
        final String COMMENT = "Comment";
        Optional<Book> book = Optional.of(new Book(ISBN, "Book 1", 2019, new Author(), new Genre()));
        when(bookRepository.findById(ISBN)).thenReturn(book);
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("sql error"));
        doThrow(exception).when(commentRepository).save(any(Comment.class));
        when(messageService.getMessage("add.comment.error", ISBN,
                exception.getMessage())).thenReturn("Error");

        var error = Assertions.assertThrows(LibraryException.class, () -> {
            libraryService.addBookComment(ISBN, COMMENT);
        });
        Assertions.assertEquals("Error", error.getMessage());

        verify(bookRepository, times(1)).findById(ISBN);
        verify(commentRepository, times(1)).save(any(Comment.class));
        verify(messageService, times(1)).getMessage("add.comment.error",
                ISBN, exception.getMessage());
    }

}


