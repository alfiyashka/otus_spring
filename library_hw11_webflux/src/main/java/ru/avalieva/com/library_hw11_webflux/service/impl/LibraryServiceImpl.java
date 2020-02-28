package ru.avalieva.com.library_hw11_webflux.service.impl;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;
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
import ru.avalieva.com.library_hw11_webflux.service.LibraryService;
import ru.avalieva.com.library_hw11_webflux.service.MessageService;


@Service
public class LibraryServiceImpl implements LibraryService {

    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;
    private final MessageService messageService;

    public LibraryServiceImpl(AuthorRepository authorRepository,
                              GenreRepository genreRepository,
                              BookRepository bookRepository,
                              CommentRepository commentRepository,
                              MessageService messageService) {
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
        this.messageService = messageService;
        this.commentRepository = commentRepository;
    }

    @Override
    public Flux<Genre> allGenres() {
        return genreRepository.findAll()
                .onErrorResume(e -> Flux.error(new LibraryException(
                        messageService.getMessage("genre.info.error", e.getMessage()), e)));
    }

    @Override
    public Flux<Book> allBooks() {
        return bookRepository.findAll()
                .onErrorResume(e -> Flux.error(new LibraryException(messageService.getMessage("book.info.error",
                        e.getMessage()), e)));
    }

    @Override
    public Flux<Author> allAuthors() {
        return authorRepository.findAll()
                .onErrorResume(e -> Flux.error(new LibraryException(messageService.getMessage("author.info.error",
                        e.getMessage()), e)));
    }

    @Override
    public Mono<Book> findBookByISBN(String isbn) {
        return bookRepository.findById(isbn)
                .onErrorResume(e -> Mono.error(new LibraryException(messageService.getMessage("book.id.not.found.error.cause",
                        isbn, e.getMessage()), e)));
    }

    @Override
    public Mono<Author> findAuthorByID(String id) {
        return authorRepository.findById(id)
                .onErrorResume(e -> Mono.error(new LibraryException(messageService.getMessage("author.id.not.found.error.cause",
                        id, e.getMessage()), e)));
    }

    @Override
    public Mono<Genre> findGenreByID(String id) {
        return genreRepository.findById(id)
                .onErrorResume(e -> Mono.error(new LibraryException(messageService.getMessage("genre.id.not.found.error.cause",
                        id, e.getMessage()), e)));
    }

    @Override
    public Flux<Book> findBookByName(String name) {
        return bookRepository.findByNameLike(name)
                .onErrorResume(e -> Flux.error(new LibraryException(messageService.getMessage("book.name.not.found.error.reason",
                        name, e.getMessage()), e)));
    }

    private Book generateBook(Tuple3<Author, Genre, Book> tuple3)
    {
        Author author = tuple3.getT1();
        Genre genre = tuple3.getT2();
        Book book = tuple3.getT3();

        book.setAuthor(author);
        book.setGenre(genre);
        return book;
    }

    @Override
    public Mono<Book> addNewBook(BookDTO bookDTO) {
        Mono<Author> authorMono = authorRepository.findById(bookDTO.getAuthorId())
                .subscribeOn(Schedulers.elastic());
        Mono<Genre> genreMono = genreRepository.findById(bookDTO.getGenreId())
                .subscribeOn(Schedulers.elastic());

        Book book = BookDtoConverter.convert(bookDTO);
        Mono<Book> bookMono = Mono.just(book).subscribeOn(Schedulers.elastic());
        Mono<Book> resultMono = Mono.zip(authorMono, genreMono, bookMono)
                .map(this::generateBook);
        return resultMono.flatMap(bookRepository::save)
                .onErrorResume(e -> Mono.error(new LibraryException(messageService.getMessage("book.add.error",
                        bookDTO.getName(), e.getMessage()), e)));
    }

    @Override
    public Mono<Void> deleteBook(String isbn) {
        return bookRepository.deleteById(isbn)
                .onErrorResume(e -> Mono.error(new LibraryException(messageService.getMessage("book.deleted.error",
                        isbn, e.getMessage()), e)));
    }

    private Comment generateComment(Tuple2<Book, Comment> tuple2)
    {
        Book book = tuple2.getT1();
        Comment comment = tuple2.getT2();

        comment.setBook(book);
        return comment;
    }

    @Override
    public Mono<Comment> addBookComment(String commentValue, String isbn) {
        Comment comment = new Comment();
        comment.setComment(commentValue);
        Mono<Comment> commentMono = Mono.just(comment).subscribeOn(Schedulers.elastic());
        Mono<Book> bookMono = bookRepository.findById(isbn).subscribeOn(Schedulers.elastic());
        Mono<Comment> resultMono = Mono.zip(bookMono, commentMono)
                .map(this::generateComment);
        return resultMono.flatMap(commentRepository::save)
                .onErrorResume(e -> Mono.error(new LibraryException(messageService.getMessage("add.comment.error",
                        isbn, e.getMessage()), e)));
    }

    @Override
    public Mono<Void> deleteComment(String commentId) {
        return commentRepository.deleteById(commentId)
                .onErrorResume(e -> Mono.error(new LibraryException(messageService.getMessage("drop.comment.error",
                        commentId, e.getMessage()), e)));
    }

    @Override
    public Flux<Comment> findCommentByBookId(String isbn) {
        return commentRepository.getCommentsOfBook(isbn)
                .onErrorResume(e -> Flux.error(new LibraryException(messageService.getMessage("book.comment.not.found.error.reason",
                        isbn, e.getMessage()), e)));
    }
}

