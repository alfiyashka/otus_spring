package ru.avalieva.otus.hw14SpringBatch.service.impl;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.avalieva.otus.hw14SpringBatch.model.jpa.Author;
import ru.avalieva.otus.hw14SpringBatch.model.jpa.Book;
import ru.avalieva.otus.hw14SpringBatch.model.jpa.Comment;
import ru.avalieva.otus.hw14SpringBatch.model.jpa.Genre;
import ru.avalieva.otus.hw14SpringBatch.model.mongo.AuthorMongo;
import ru.avalieva.otus.hw14SpringBatch.model.mongo.BookMongo;
import ru.avalieva.otus.hw14SpringBatch.model.mongo.GenreMongo;
import ru.avalieva.otus.hw14SpringBatch.repository.AuthorRepository;
import ru.avalieva.otus.hw14SpringBatch.repository.BookRepository;
import ru.avalieva.otus.hw14SpringBatch.repository.GenreRepository;
import ru.avalieva.otus.hw14SpringBatch.service.LibraryConverterException;
import ru.avalieva.otus.hw14SpringBatch.service.LibraryConverterService;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ContextConfiguration(classes = LibraryConverterServiceImpl.class)
@ExtendWith({SpringExtension.class})
public class LibraryConverterServiceImplTest {
    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private LibraryConverterService libraryConverter;

    @DisplayName("convert jpa book to mongo db book")
    @Test
    public void convertToMongoBookTest() {
        Genre genre = new Genre(1, "GENRE");
        Author author = new Author(1L, "Mary", "Panak",
                "23432435", "mary@mail.com");
        Book book = new Book(1L, "Book 1", 2019, author, genre);
        AuthorMongo authorMongo = new AuthorMongo("ID", "Mary", "Panak",
                "23432435", "mary@mail.com");
        when(authorRepository.findByFirstNameAndLastName(author.getFirstName(),
                author.getLastName())).thenReturn(Optional.of(authorMongo));
        GenreMongo genreMongo = new GenreMongo("ID", "GENRE");
        when(genreRepository.findByGenreName(genre.getGenreName()))
                .thenReturn(Optional.of(genreMongo));

        var bookMongo = libraryConverter.convertToMongoBook(book);
        Assertions.assertEquals(genreMongo, bookMongo.getGenre());
        Assertions.assertEquals(authorMongo, bookMongo.getAuthor());

        verify(authorRepository, times(1))
                .findByFirstNameAndLastName(author.getFirstName(), author.getLastName());
        verify(genreRepository, times(1)).findByGenreName(genre.getGenreName());

    }

    @DisplayName("convert jpa book to mongo db book failed - no author")
    @Test
    public void convertToMongoBookTestFailedNoAuthor() {
        Genre genre = new Genre(1, "GENRE");
        Author author = new Author(1L, "Mary", "Panak",
                "23432435", "mary@mail.com");
        Book book = new Book(1L, "Book 1", 2019, author, genre);
        when(authorRepository.findByFirstNameAndLastName(author.getFirstName(),
                author.getLastName())).thenReturn(Optional.ofNullable(null));

        Assertions.assertThrows(LibraryConverterException.class, () -> {
            libraryConverter.convertToMongoBook(book);
        });

        verify(authorRepository, times(1))
                .findByFirstNameAndLastName(author.getFirstName(), author.getLastName());
        verify(genreRepository, never()).findByGenreName(anyString());
    }


    @DisplayName("convert jpa book to mongo db book -  no genre")
    @Test
    public void convertToMongoBookTestFailedNoGenre() {
        Genre genre = new Genre(1, "GENRE");
        Author author = new Author(1L, "Mary", "Panak",
                "23432435", "mary@mail.com");
        Book book = new Book(1L, "Book 1", 2019, author, genre);
        AuthorMongo authorMongo = new AuthorMongo("ID", "Mary", "Panak",
                "23432435", "mary@mail.com");
        when(authorRepository.findByFirstNameAndLastName(author.getFirstName(),
                author.getLastName())).thenReturn(Optional.of(authorMongo));
        when(genreRepository.findByGenreName(genre.getGenreName()))
                .thenReturn(Optional.ofNullable(null));

        Assertions.assertThrows(LibraryConverterException.class, () -> {
            libraryConverter.convertToMongoBook(book);
        });

        verify(authorRepository, times(1))
                .findByFirstNameAndLastName(author.getFirstName(), author.getLastName());
        verify(genreRepository, times(1)).findByGenreName(genre.getGenreName());

    }


    @DisplayName("convert jpa comment to mongo db comment")
    @Test
    public void convertToMongoCommentTest() {
        Genre genre = new Genre(1, "GENRE");
        Author author = new Author(1L, "Mary", "Panak",
                "23432435", "mary@mail.com");
        Book book = new Book(1L, "Book 1", 2019, author, genre);
        Comment comment = new Comment(1L, "COMMENT", book);
        AuthorMongo authorMongo = new AuthorMongo("ID", "Mary", "Panak",
                "23432435", "mary@mail.com");
        when(authorRepository.findByFirstNameAndLastName(author.getFirstName(),
                author.getLastName())).thenReturn(Optional.of(authorMongo));
        GenreMongo genreMongo = new GenreMongo("ID", "GENRE");
        when(genreRepository.findByGenreName(genre.getGenreName()))
                .thenReturn(Optional.of(genreMongo));
        BookMongo bookMongo = new BookMongo("ID", "Book 1", 2019, authorMongo, genreMongo);
        when(bookRepository.getBookByNameAuthorGenre(book.getName(), authorMongo.getId(),
                genreMongo.getId())).thenReturn(Optional.of(bookMongo));

        var commentMongo = libraryConverter.convertToMongoComment(comment);
        Assertions.assertEquals(bookMongo, commentMongo.getBook());

        verify(authorRepository, times(1))
                .findByFirstNameAndLastName(author.getFirstName(), author.getLastName());
        verify(genreRepository, times(1)).findByGenreName(genre.getGenreName());
        verify(bookRepository, times(1))
                .getBookByNameAuthorGenre(book.getName(), authorMongo.getId(), genreMongo.getId());
    }

    @DisplayName("convert jpa comment to mongo db comment failed no author")
    @Test
    public void convertToMongoCommentTestFailedNoAuthor() {
        Genre genre = new Genre(1, "GENRE");
        Author author = new Author(1L, "Mary", "Panak",
                "23432435", "mary@mail.com");
        Book book = new Book(1L, "Book 1", 2019, author, genre);
        Comment comment = new Comment(1L, "COMMENT", book);
        when(authorRepository.findByFirstNameAndLastName(author.getFirstName(),
                author.getLastName())).thenReturn(Optional.ofNullable(null));

        Assertions.assertThrows(LibraryConverterException.class, () -> {
            libraryConverter.convertToMongoComment(comment);
        });

        verify(authorRepository, times(1))
                .findByFirstNameAndLastName(author.getFirstName(), author.getLastName());
        verify(genreRepository, never()).findByGenreName(genre.getGenreName());
        verify(bookRepository, never()).getBookByNameAuthorGenre(anyString(), anyString(), anyString());
    }

    @DisplayName("convert jpa comment to mongo db comment failed - no genre")
    @Test
    public void convertToMongoCommentTestFailedNoGenre() {
        Genre genre = new Genre(1, "GENRE");
        Author author = new Author(1L, "Mary", "Panak",
                "23432435", "mary@mail.com");
        Book book = new Book(1L, "Book 1", 2019, author, genre);
        Comment comment = new Comment(1L, "COMMENT", book);
        AuthorMongo authorMongo = new AuthorMongo("ID", "Mary", "Panak",
                "23432435", "mary@mail.com");
        when(authorRepository.findByFirstNameAndLastName(author.getFirstName(),
                author.getLastName())).thenReturn(Optional.of(authorMongo));
        when(genreRepository.findByGenreName(genre.getGenreName()))
                .thenReturn(Optional.ofNullable(null));

        Assertions.assertThrows(LibraryConverterException.class, () -> {
            libraryConverter.convertToMongoComment(comment);
        });

        verify(authorRepository, times(1))
                .findByFirstNameAndLastName(author.getFirstName(), author.getLastName());
        verify(genreRepository, times(1)).findByGenreName(genre.getGenreName());
        verify(bookRepository, never()).getBookByNameAuthorGenre(anyString(), anyString(), anyString());
    }

    @DisplayName("convert jpa comment to mongo db comment failed - no book")
    @Test
    public void convertToMongoCommentTestFailedNoBook() {
        Genre genre = new Genre(1, "GENRE");
        Author author = new Author(1L, "Mary", "Panak",
                "23432435", "mary@mail.com");
        Book book = new Book(1L, "Book 1", 2019, author, genre);
        Comment comment = new Comment(1L, "COMMENT", book);
        AuthorMongo authorMongo = new AuthorMongo("ID", "Mary", "Panak",
                "23432435", "mary@mail.com");
        when(authorRepository.findByFirstNameAndLastName(author.getFirstName(),
                author.getLastName())).thenReturn(Optional.of(authorMongo));
        GenreMongo genreMongo = new GenreMongo("ID", "GENRE");
        when(genreRepository.findByGenreName(genre.getGenreName()))
                .thenReturn(Optional.of(genreMongo));
        when(bookRepository.getBookByNameAuthorGenre(book.getName(), authorMongo.getId(),
                genreMongo.getId())).thenReturn(Optional.ofNullable(null));

        Assertions.assertThrows(LibraryConverterException.class, () -> {
            libraryConverter.convertToMongoComment(comment);
        });

        verify(authorRepository, times(1))
                .findByFirstNameAndLastName(author.getFirstName(), author.getLastName());
        verify(genreRepository, times(1)).findByGenreName(genre.getGenreName());
        verify(bookRepository, times(1))
                .getBookByNameAuthorGenre(book.getName(), authorMongo.getId(), genreMongo.getId());
    }
}

