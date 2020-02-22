package ru.avalieva.com.library_hw11_webflux.service;

import com.mongodb.MongoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.avalieva.com.library_hw11_webflux.domain.Author;
import ru.avalieva.com.library_hw11_webflux.domain.Book;
import ru.avalieva.com.library_hw11_webflux.domain.Comment;
import ru.avalieva.com.library_hw11_webflux.domain.Genre;
import ru.avalieva.com.library_hw11_webflux.dto.BookDTO;
import ru.avalieva.com.library_hw11_webflux.dto.BookDtoConverter;
import ru.avalieva.com.library_hw11_webflux.repository.AuthorRepository;
import ru.avalieva.com.library_hw11_webflux.repository.BookRepository;
import ru.avalieva.com.library_hw11_webflux.repository.CommentRepository;
import ru.avalieva.com.library_hw11_webflux.repository.GenreRepository;
import ru.avalieva.com.library_hw11_webflux.service.impl.LibraryException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration")
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@ComponentScan({"ru.avalieva.com.library_hw11_webflux.repository",
        "ru.avalieva.com.library_hw11_webflux.service"})
@ExtendWith({SpringExtension.class})
public class LibraryServiceTest {
    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private MessageService messageService;

    @Autowired
    private LibraryService libraryService;


    @DisplayName("get all genres test")
    @Test
    public void getAllGenresTest() {
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre("id1", "GENRE"));
        genres.add(new Genre("id2", "GENRE2"));
        Flux<Genre> genreFlux = Flux.fromIterable(genres);
        when(genreRepository.findAll()).thenReturn(genreFlux);

        StepVerifier.create(libraryService.allGenres())
                .expectNext(genres.get(0), genres.get(1))
                .expectComplete()
                .verify();

        verify(genreRepository, times(1)).findAll();
    }

    @DisplayName("get all genres test failed")
    @Test
    public void getAllGenresTestFailed() {
        MongoException exception = new MongoException("Error");
        when(genreRepository.findAll()).thenReturn(Flux.error(exception));
        when(messageService.getMessage("genre.info.error",  exception.getMessage()))
                .thenReturn("Cannot get");

        StepVerifier.create(libraryService.allGenres())
                .verifyError(LibraryException.class);


        verify(genreRepository, times(1)).findAll();
        verify(messageService, times(1)).getMessage("genre.info.error",  exception.getMessage());
    }

    @DisplayName("get all books test")
    @Test
    public void getAllBooksTest() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("ID2", "Book 1", 2019, new Author(), new Genre()));
        books.add(new Book("ID1", "Book 2", 2019, new Author(), new Genre()));
        Flux<Book> bookFlux = Flux.fromIterable(books);
        when(bookRepository.findAll()).thenReturn(bookFlux);

        StepVerifier.create(libraryService.allBooks())
                .expectNext(books.get(0), books.get(1))
                .expectComplete()
                .verify();

        verify(bookRepository, times(1)).findAll();
    }

    @DisplayName("get all books test failed")
    @Test
    public void getAllBooksTestFailed() {
        MongoException exception = new MongoException("Error");
        when(bookRepository.findAll()).thenReturn(Flux.error(exception));
        when(messageService.getMessage("book.info.error",  exception.getMessage()))
                .thenReturn("Cannot get");

        StepVerifier.create(libraryService.allBooks())
                .verifyError(LibraryException.class);

        verify(bookRepository, times(1)).findAll();
        verify(messageService, times(1)).getMessage("book.info.error",  exception.getMessage());
    }

    @DisplayName("get all authors test")
    @Test
    public void getAllAuthorTest() {
        List<Author> authors = new ArrayList<>();
        authors.add(new Author("ID1", "Mary", "Panak", "23432435", "mary@mail.com"));
        authors.add(new Author("ID2", "Mark", "Smith", "11111", "mark@mail.com"));
        Flux<Author> authorFlux = Flux.fromIterable(authors);
        when(authorRepository.findAll()).thenReturn(authorFlux);

        StepVerifier.create(libraryService.allAuthors())
                .expectNext(authors.get(0), authors.get(1))
                .verifyComplete();

        verify(authorRepository, times(1)).findAll();
    }

    @DisplayName("get all authors test failed")
    @Test
    public void getAllAuthorTestFailed() {
        MongoException exception = new MongoException("Error");
        when(authorRepository.findAll()).thenReturn(Flux.error(exception));
        when(messageService.getMessage("author.info.error",  exception.getMessage()))
                .thenReturn("Cannot get");

        StepVerifier.create(libraryService.allAuthors())
                .verifyError(LibraryException.class);

        verify(authorRepository, times(1)).findAll();
        verify(messageService, times(1)).getMessage("author.info.error",  exception.getMessage());
    }

    @DisplayName("find book by isbn test")
    @Test
    public void findBookByISBNTest() {
        Book book = new Book("ID1", "Book 1", 2019, new Author(), new Genre());
        Mono<Book> bookMono = Mono.just(book);
        when(bookRepository.findById(book.getIsbn())).thenReturn(bookMono);

        StepVerifier.create(libraryService.findBookByISBN(book.getIsbn()))
                .expectNext(book)
                .verifyComplete();

        verify(bookRepository, times(1)).findById(book.getIsbn());
    }

    @DisplayName("find book by isbn test failed")
    @Test
    public void findBookByISBNTestFailed() {
        final String ISBN = "ID1";
        MongoException exception = new MongoException("Error");
        when(bookRepository.findById("ID1")).thenReturn(Mono.error(exception));
        when(messageService.getMessage("book.id.not.found.error.cause",
                ISBN, exception.getMessage())).thenReturn("Cannot find");

        StepVerifier.create(libraryService.findBookByISBN(ISBN))
                .verifyError(LibraryException.class);

        verify(bookRepository, times(1)).findById(ISBN);
        verify(messageService, times(1)).getMessage("book.id.not.found.error.cause",
                ISBN, exception.getMessage());

    }

    @DisplayName("find author by id test")
    @Test
    public void findAuthorByIDTest() {
        Author author = new Author("ID1", "Mary",  "Fox", "234355", "mail");
        Mono<Author> authorMono = Mono.just(author);
        when(authorRepository.findById(author.getId())).thenReturn(authorMono);

        StepVerifier.create(libraryService.findAuthorByID(author.getId()))
                .expectNext(author)
                .verifyComplete();

        verify(authorRepository, times(1)).findById(author.getId());
    }

    @DisplayName("find author by id test failed")
    @Test
    public void findAuthorByIDTestFailed() {
        final String AUTHOR_ID = "ID1";
        MongoException exception = new MongoException("Error");
        when(authorRepository.findById(AUTHOR_ID)).thenReturn(Mono.error(exception));
        when(messageService.getMessage("author.id.not.found.error.cause",
                AUTHOR_ID, exception.getMessage())).thenReturn("Cannot find");

        StepVerifier.create(libraryService.findAuthorByID(AUTHOR_ID))
                .verifyError(LibraryException.class);

        verify(authorRepository, times(1)).findById(AUTHOR_ID);
        verify(messageService, times(1)).getMessage("author.id.not.found.error.cause",
                AUTHOR_ID, exception.getMessage());
    }


    @DisplayName("find genre by id test")
    @Test
    public void findGenreByIDTest() {
        Genre genre = new Genre("ID1", "GENRE");
        Mono<Genre> genreMono = Mono.just(genre);
        when(genreRepository.findById(genre.getId())).thenReturn(genreMono);

        StepVerifier.create(libraryService.findGenreByID(genre.getId()))
                .expectNext(genre)
                .verifyComplete();

        verify(genreRepository, times(1)).findById(genre.getId());
    }

    @DisplayName("find genre by id test failed")
    @Test
    public void findGenreByIDTestFailed() {
        final String GENRE_ID = "ID1";
        MongoException exception = new MongoException("Error");
        when(genreRepository.findById(GENRE_ID)).thenReturn(Mono.error(exception));
        when(messageService.getMessage("genre.id.not.found.error.cause",
                GENRE_ID, exception.getMessage())).thenReturn("Cannot find");

        StepVerifier.create(libraryService.findGenreByID(GENRE_ID))
                .verifyError(LibraryException.class);

        verify(genreRepository, times(1)).findById(GENRE_ID);
        verify(messageService, times(1)).getMessage("genre.id.not.found.error.cause",
                GENRE_ID, exception.getMessage());
    }


    @DisplayName("find book by name test")
    @Test
    public void findBookByNameTest() {
        String bookName = "BOOK 1";
        Book book = new Book("ID1", bookName, 2019, new Author(), new Genre());
        List<Book> books = new ArrayList<>();
        books.add(book);
        Flux<Book> bookFlux = Flux.fromIterable(books);
        when(bookRepository.findByNameLike(bookName)).thenReturn(bookFlux);

        StepVerifier.create(libraryService.findBookByName(bookName))
                .expectNext(books.get(0))
                .verifyComplete();

        verify(bookRepository, times(1)).findByNameLike(bookName);
    }

    @DisplayName("find book by name test failed")
    @Test
    public void findBookByNameTestFailed() {
        final String BOOK_NAME = "BOOK 1";
        MongoException exception = new MongoException("Error");
        when(bookRepository.findByNameLike(BOOK_NAME)).thenReturn(Flux.error(exception));
        when(messageService.getMessage("book.name.not.found.error.reason",
                BOOK_NAME, exception.getMessage())).thenReturn("Cannot find");

        StepVerifier.create(libraryService.findBookByName(BOOK_NAME))
                .verifyError(LibraryException.class);

        verify(bookRepository, times(1)).findByNameLike(BOOK_NAME);
        verify(messageService, times(1)).getMessage("book.name.not.found.error.reason",
                BOOK_NAME, exception.getMessage());
    }

    @DisplayName("add book test")
    @Test
    public void addNewBookTest() {
        Author author = new Author("ID1", "Mary", "Fox", "234355", "mail");
        Book book = new Book("ID1", "BOOK 1", 2019, author, new Genre("ID1", "Genre"));
        BookDTO bookDTO = BookDtoConverter.convert(book);
        Mono<Book> bookMono = Mono.just(book);
        when(authorRepository.findById(bookDTO.getAuthorId())).thenReturn(Mono.just(book.getAuthor()));
        when(genreRepository.findById(bookDTO.getGenreId())).thenReturn(Mono.just(book.getGenre()));
        when(bookRepository.save(book)).thenReturn(bookMono);

        StepVerifier.create(libraryService.addNewBook(bookDTO))
                .expectNextCount(1)
                .expectComplete()
                .verify();

        verify(bookRepository, times(1)).save(book);
        verify(authorRepository, times(1)).findById(bookDTO.getAuthorId());
        verify(genreRepository, times(1)).findById(bookDTO.getGenreId());
    }

    @DisplayName("add book test failed")
    @Test
    public void addNewBookTestFailed() {
        Author author = new Author("ID1", "Mary", "Fox", "234355", "mail");
        Book book = new Book("ID1", "BOOK 1", 2019, author, new Genre("ID1", "Genre"));
        BookDTO bookDTO = BookDtoConverter.convert(book);
        MongoException exception = new MongoException("Error");
        when(authorRepository.findById(bookDTO.getAuthorId())).thenReturn(Mono.error(exception));
        when(genreRepository.findById(bookDTO.getGenreId())).thenReturn(Mono.just(book.getGenre()));
        when(messageService.getMessage("book.add.error",
                bookDTO.getName(), exception.getMessage())).thenReturn("Cannot add");

        StepVerifier.create(libraryService.addNewBook(bookDTO))
                .verifyError(LibraryException.class);

        verify(bookRepository, times(0)).save(any(Mono.class));
        verify(authorRepository, times(1)).findById(bookDTO.getAuthorId());
        verify(genreRepository, times(1)).findById(bookDTO.getGenreId());
        verify(messageService, times(1)).getMessage("book.add.error",
                bookDTO.getName(), exception.getMessage());
    }

    @DisplayName("delete book test")
    @Test
    public void deleteBookTest() {
        String isbn = "ID1";
        when(bookRepository.deleteById(isbn)).thenReturn(Mono.empty());

        StepVerifier.create(libraryService.deleteBook(isbn))
                .expectComplete()
                .verify();

        verify(bookRepository, times(1)).deleteById(isbn);
    }


    @DisplayName("delete book test failed")
    @Test
    public void deleteBookTestFailed() {
        final String ISBN = "ID1";
        MongoException exception = new MongoException("Error");
        when(bookRepository.deleteById(ISBN)).thenReturn(Mono.error(exception));
        when(messageService.getMessage("book.deleted.error",
                ISBN, exception.getMessage())).thenReturn("Cannot delete");

        StepVerifier.create(libraryService.deleteBook(ISBN))
                .verifyError(LibraryException.class);

        verify(bookRepository, times(1)).deleteById(ISBN);
        verify(messageService, times(1)).getMessage("book.deleted.error",
                ISBN, exception.getMessage());
    }

    @DisplayName("add comment test")
    @Test
    public void addCommentTest() {
        final String ISBN = "ID1";
        final String COMMENT = "Comment";
        Book book = new Book(ISBN, "Book 1", 2019, new Author(), new Genre());
        Mono<Book> bookMono = Mono.just(book);
        when(bookRepository.findById(ISBN)).thenReturn(bookMono);
        Comment comment = new Comment("ID1", COMMENT, book);
        when(commentRepository.save(any(Comment.class))).thenReturn(Mono.just(comment));

        StepVerifier.create(libraryService.addBookComment(COMMENT, ISBN))
                .expectNext(comment)
                .expectComplete()
                .verify();

        verify(bookRepository, times(1)).findById(ISBN);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @DisplayName("add comment test failed")
    @Test
    public void addCommentTestFailed() {
        final String ISBN = "ID1";
        final String COMMENT = "Comment";
        MongoException exception = new MongoException("Error");
        when(bookRepository.findById(ISBN)).thenReturn(Mono.error(exception));
        when(messageService.getMessage("add.comment.error",
                ISBN, exception.getMessage())).thenReturn("Cannot add");

        StepVerifier.create(libraryService.addBookComment(COMMENT, ISBN))
                .verifyError(LibraryException.class);

        verify(bookRepository, times(1)).findById(ISBN);
        verify(messageService, times(1)).getMessage("add.comment.error",
                ISBN, "Error");
        verify(commentRepository, times(0)).save(any(Comment.class));
    }

    @DisplayName("find comment by book isbn test")
    @Test
    public void findCommentByBookTest() {
        final String ISBN = "ID1";
        Book book = new Book(ISBN, "Book 1", 2019, new Author(), new Genre());
        Comment comment = new Comment("ID1", "Comment", book);
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        Flux<Comment> commentFlux = Flux.fromIterable(comments);
        when(commentRepository.getCommentsOfBook(ISBN)).thenReturn(commentFlux);
        StepVerifier.create(libraryService.findCommentByBookId(ISBN))
                .expectNext(comment)
                .expectComplete()
                .verify();

        verify(commentRepository, times(1)).getCommentsOfBook(ISBN);
    }


    @DisplayName("find comment by book isbn test failed")
    @Test
    public void findCommentByBookTestFailed() {
        final String ISBN = "ID1";
        MongoException exception = new MongoException("Error");
        when(commentRepository.getCommentsOfBook(ISBN)).thenReturn(Flux.error(exception));
        when(messageService.getMessage("book.comment.not.found.error.reason",
                ISBN, exception.getMessage())).thenReturn("Cannot find");

        StepVerifier.create(libraryService.findCommentByBookId(ISBN))
                .verifyError(LibraryException.class);

        verify(commentRepository, times(1)).getCommentsOfBook(ISBN);
        verify(messageService, times(1)).getMessage("book.comment.not.found.error.reason",
                ISBN, exception.getMessage());
    }


    @DisplayName("delete comment test")
    @Test
    public void deleteCommentTest() {
        final String ID = "ID1";
        when(commentRepository.deleteById(ID)).thenReturn(Mono.empty());

        StepVerifier.create(libraryService.deleteComment(ID))
                .expectComplete()
                .verify();

        verify(commentRepository, times(1)).deleteById(ID);
    }

    @DisplayName("delete comment test failed")
    @Test
    public void deleteCommentTestFailed() {
        final String ID = "ID1";
        MongoException exception = new MongoException("Error");
        when(commentRepository.deleteById(ID)).thenReturn(Mono.error(exception));
        when(messageService.getMessage("drop.comment.error",
                ID, exception.getMessage())).thenReturn("Cannot delete");

        StepVerifier.create(libraryService.deleteComment(ID))
                .verifyError(LibraryException.class);

        verify(commentRepository, times(1)).deleteById(ID);
        verify(messageService, times(1)).getMessage("drop.comment.error",
                ID, exception.getMessage());
    }

}