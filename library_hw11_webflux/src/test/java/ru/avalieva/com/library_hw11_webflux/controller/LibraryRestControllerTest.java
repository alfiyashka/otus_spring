package ru.avalieva.com.library_hw11_webflux.controller;

import com.mongodb.MongoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.avalieva.com.library_hw11_webflux.domain.Author;
import ru.avalieva.com.library_hw11_webflux.domain.Book;
import ru.avalieva.com.library_hw11_webflux.domain.Comment;
import ru.avalieva.com.library_hw11_webflux.domain.Genre;
import ru.avalieva.com.library_hw11_webflux.dto.BookDTO;
import ru.avalieva.com.library_hw11_webflux.dto.BookDtoConverter;
import ru.avalieva.com.library_hw11_webflux.dto.CommentDTO;
import ru.avalieva.com.library_hw11_webflux.dto.CommentDtoConverter;
import ru.avalieva.com.library_hw11_webflux.service.LibraryService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;


@ExtendWith({SpringExtension.class})
@SpringBootTest
public class LibraryRestControllerTest {
    @MockBean
    private LibraryService libraryService;

    private WebTestClient client;

    @BeforeEach
    void init() {
        LibraryRestController restController = new LibraryRestController(libraryService);
        client = WebTestClient
                .bindToController(restController)
                .build();
    }

    @Test
    public void testGetAuthors() {
        Author author = new Author("ID", "FIRST_NAME", "LAST_NAME", "12324324", "EMAIL");
        List<Author> authors = new ArrayList<>();
        authors.add(author);
        when(libraryService.allAuthors()).thenReturn(Flux.fromIterable(authors));

        client.get()
                .uri("/api/authors")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Author.class).hasSize(authors.size()).contains(author);

        verify(libraryService, times(1)).allAuthors();
    }

    @Test
    public void testGetAuthorsFailed() {
        MongoException exception = new MongoException("Error");
        when(libraryService.allAuthors()).thenReturn(Flux.error(exception));

        client.get()
                .uri("/api/authors")
                .exchange()
                .expectStatus()
                .is5xxServerError();

        verify(libraryService, times(1)).allAuthors();
    }

    @Test
    public void testGetGenres() {
        Genre genre = new Genre("ID",  "GENRE");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);
        when(libraryService.allGenres()).thenReturn(Flux.fromIterable(genres));

        client.get()
                .uri("/api/genres")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Genre.class).hasSize(genres.size()).contains(genre);

        verify(libraryService, times(1)).allGenres();
    }

    @Test
    public void testGetGenresFailed() {
        MongoException exception = new MongoException("Error");
        when(libraryService.allGenres()).thenReturn(Flux.error(exception));

        client.get()
                .uri("/api/genres")
                .exchange()
                .expectStatus()
                .is5xxServerError();

        verify(libraryService, times(1)).allGenres();
    }

    @Test
    public void testGetComments() {
        String bookISBN = "ID1";
        Comment comment = new Comment("ID1", "COMMENT", new Book());
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        when(libraryService.findCommentByBookId(bookISBN)).thenReturn(Flux.fromIterable(comments));

        client.get()
                .uri("/api/books/" + bookISBN + "/comments")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(CommentDTO.class).hasSize(comments.size()).contains(CommentDtoConverter.convert(comment));

        verify(libraryService, times(1)).findCommentByBookId(bookISBN);
    }

    @Test
    public void testGetCommentsFailed() {
        String bookISBN = "ID1";
        MongoException exception = new MongoException("Error");
        when(libraryService.findCommentByBookId(bookISBN)).thenReturn(Flux.error(exception));

        client.get()
                .uri("/api/books/" + bookISBN + "/comments")
                .exchange()
                .expectStatus()
                .is5xxServerError();

        verify(libraryService, times(1)).findCommentByBookId(bookISBN);
    }

    @Test
    public void testAddComment() {
        String bookISBN = "ID1";
        Comment comment = new Comment("ID1", "COMMENT", new Book());
        CommentDTO commentDTO = CommentDtoConverter.convert(comment);
        when(libraryService.addBookComment(commentDTO.getComment(), bookISBN))
                .thenReturn(Mono.just(comment));


        client.post()
                .uri("/api/books/" + bookISBN + "/comments")
                .bodyValue(commentDTO)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CommentDTO.class).isEqualTo(commentDTO);

        verify(libraryService, times(1)).addBookComment(commentDTO.getComment(), bookISBN);
    }

    @Test
    public void testAddCommentFailed() {
        String bookISBN = "ID1";
        CommentDTO commentDTO = new CommentDTO("COMMENT", "ID1");
        MongoException exception = new MongoException("Error");
        when(libraryService.addBookComment(commentDTO.getComment(), bookISBN))
                .thenReturn(Mono.error(exception));

        client.post()
                .uri("/api/books/" + bookISBN + "/comments")
                .bodyValue(commentDTO)
                .exchange()
                .expectStatus()
                .is5xxServerError();

        verify(libraryService, times(1)).addBookComment(commentDTO.getComment(), bookISBN);
    }

    @Test
    public void testDeleteComment() {
        String commentID = "ID1";
        when(libraryService.deleteComment(commentID)).thenReturn(Mono.empty());

        client.delete()
                .uri("/api/books/comments/" + commentID)
                .exchange()
                .expectStatus()
                .isOk();

        verify(libraryService, times(1)).deleteComment(commentID);
    }

    @Test
    public void testDeleteCommentFailed() {
        String commentID = "ID1";
        MongoException exception = new MongoException("Error");
        when(libraryService.deleteComment(commentID)).thenReturn(Mono.error(exception));

        client.delete()
                .uri("/api/books/comments/" + commentID)
                .exchange()
                .expectStatus()
                .is5xxServerError();

        verify(libraryService, times(1)).deleteComment(commentID);
    }


    @Test
    public void testGetBooks() {
        Book book = new Book("ID1", "NEW BOOK", 1990, new Author(), new Genre());
        List<Book> bookList = new ArrayList<>();
        bookList.add(book);
        when(libraryService.allBooks()).thenReturn(Flux.fromIterable(bookList));

        client.get()
                .uri("/api/books")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(BookDTO.class).hasSize(bookList.size()).contains(BookDtoConverter.convert(book));

        verify(libraryService, times(1)).allBooks();
    }

    @Test
    public void testGetBooksFailed() {
        MongoException exception = new MongoException("Error");
        when(libraryService.allBooks()).thenReturn(Flux.error(exception));

        client.get()
                .uri("/api/books")
                .exchange()
                .expectStatus()
                .is5xxServerError();

        verify(libraryService, times(1)).allBooks();
    }

    @Test
    public void testFindBooksByName() {
        Book book = new Book("ID1", "NEW BOOK", 1990, new Author(), new Genre());
        List<Book> bookList = new ArrayList<>();
        bookList.add(book);
        when(libraryService.findBookByName(book.getName())).thenReturn(Flux.fromIterable(bookList));

        client.get()
                .uri(uriBuilder -> uriBuilder.path("/api/books")
                        .queryParam("name", book.getName())
                        .build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(BookDTO.class).hasSize(bookList.size()).contains(BookDtoConverter.convert(book));

        verify(libraryService, times(1)).findBookByName(book.getName());
    }


    @Test
    public void testFindBooksByNameFailed() {
        final String BOOK_NAME = "NEW BOOK";
        MongoException exception = new MongoException("Error");
        when(libraryService.findBookByName(BOOK_NAME)).thenReturn(Flux.error(exception));

        client.get()
                .uri(uriBuilder -> uriBuilder.path("/api/books")
                        .queryParam("name", BOOK_NAME)
                        .build())
                .exchange()
                .expectStatus()
                .is5xxServerError();

        verify(libraryService, times(1)).findBookByName(BOOK_NAME);
    }

    @Test
    public void testFindBooksByID() {
        Book book = new Book("ID1", "NEW BOOK", 1990, new Author(), new Genre());
        when(libraryService.findBookByISBN(book.getIsbn())).thenReturn(Mono.just(book));

        client.get()
                .uri("/api/books/" + book.getIsbn())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(BookDTO.class).isEqualTo(BookDtoConverter.convert(book));

        verify(libraryService, times(1)).findBookByISBN(book.getIsbn());
    }


    @Test
    public void testFindBooksByIDFailed() {
        final String ISBN = "ID1";
        MongoException exception = new MongoException("Error");
        when(libraryService.findBookByISBN(ISBN)).thenReturn(Mono.error(exception));

        client.get()
                .uri("/api/books/" + ISBN)
                .exchange()
                .expectStatus()
                .is5xxServerError();

        verify(libraryService, times(1)).findBookByISBN(ISBN);
    }

    @Test
    public void testDeleteBook() {
        final String ISBN = "ID1";
        when(libraryService.deleteBook(ISBN)).thenReturn(Mono.empty());

        client.delete()
                .uri("/api/books/" + ISBN)
                .exchange()
                .expectStatus()
                .isOk();

        verify(libraryService, times(1)).deleteBook(ISBN);
    }


    @Test
    public void testDeleteBookFailed() {
        final String ISBN = "ID1";
        MongoException exception = new MongoException("Error");
        when(libraryService.deleteBook(ISBN)).thenReturn(Mono.error(exception));

        client.delete()
                .uri("/api/books/" + ISBN)
                .exchange()
                .expectStatus()
                .is5xxServerError();

        verify(libraryService, times(1)).deleteBook(ISBN);
    }

    @Test
    public void testAddBook() {
        Book book = new Book("ID1", "NEW BOOK", 1990, new Author(), new Genre());
        BookDTO bookDTO = BookDtoConverter.convert(book);
        when(libraryService.addNewBook(bookDTO)).thenReturn(Mono.just(book));

        client.post()
                .uri("/api/books/")
                .bodyValue(bookDTO)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(BookDTO.class).isEqualTo(BookDtoConverter.convert(book));

        verify(libraryService, times(1)).addNewBook(bookDTO);
    }


    @Test
    public void testAddBookFailed() {
        BookDTO bookDTO = new BookDTO();
        MongoException exception = new MongoException("Error");
        when(libraryService.addNewBook(bookDTO)).thenReturn(Mono.error(exception));

        client.post()
                .uri("/api/books/")
                .bodyValue(bookDTO)
                .exchange()
                .expectStatus()
                .is5xxServerError();

        verify(libraryService, times(1)).addNewBook(bookDTO);
    }

}
