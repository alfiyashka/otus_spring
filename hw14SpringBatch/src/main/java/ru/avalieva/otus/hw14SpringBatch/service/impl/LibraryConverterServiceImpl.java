package ru.avalieva.otus.hw14SpringBatch.service.impl;

import org.springframework.stereotype.Service;
import ru.avalieva.otus.hw14SpringBatch.model.jpa.Author;
import ru.avalieva.otus.hw14SpringBatch.model.jpa.Book;
import ru.avalieva.otus.hw14SpringBatch.model.jpa.Comment;
import ru.avalieva.otus.hw14SpringBatch.model.jpa.Genre;
import ru.avalieva.otus.hw14SpringBatch.model.mongo.AuthorMongo;
import ru.avalieva.otus.hw14SpringBatch.model.mongo.BookMongo;
import ru.avalieva.otus.hw14SpringBatch.model.mongo.CommentMongo;
import ru.avalieva.otus.hw14SpringBatch.model.mongo.GenreMongo;
import ru.avalieva.otus.hw14SpringBatch.repository.AuthorRepository;
import ru.avalieva.otus.hw14SpringBatch.repository.BookRepository;
import ru.avalieva.otus.hw14SpringBatch.repository.GenreRepository;
import ru.avalieva.otus.hw14SpringBatch.service.LibraryConverterException;
import ru.avalieva.otus.hw14SpringBatch.service.LibraryConverterService;

@Service
public class LibraryConverterServiceImpl implements LibraryConverterService {
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;

    public LibraryConverterServiceImpl(AuthorRepository authorRepository,
                                   GenreRepository genreRepository,
                                   BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public AuthorMongo convertToMongoAuthor(Author author){
        return new AuthorMongo(null, author.getFirstName(),
                author.getLastName(), author.getPhoneNumber(), author.getEmail());
    }

    @Override
    public GenreMongo convertToMongoGenre(Genre genre){
        return new GenreMongo(null, genre.getGenreName());
    }

    private AuthorMongo getAuthorMongo(Book book) {
        Author author = book.getAuthor();

        return authorRepository.findByFirstNameAndLastName(author.getFirstName(),
                author.getLastName()).orElseThrow(() -> new LibraryConverterException(
                String.format("Cannot find author with name %s %s of book with name %s",
                        author.getFirstName(), author.getLastName(), book.getName())));
    }

    private GenreMongo getGenreMongo(Book book) {
        Genre genre = book.getGenre();
        return genreRepository.findByGenreName(genre.getGenreName())
                .orElseThrow(() -> new LibraryConverterException(
                        String.format("Cannot find genre with name %s for book with name %s",
                                genre.getGenreName(), book.getName())));
    }

    @Override
    public BookMongo convertToMongoBook(Book book){
        var authorMongo = getAuthorMongo(book);
        var genreMongo = getGenreMongo(book);

        return new BookMongo(null, book.getName(),
                book.getPublishingYear(), authorMongo, genreMongo);
    }

    @Override
    public CommentMongo convertToMongoComment(Comment comment){
        Book book = comment.getBook();

        var authorMongo = getAuthorMongo(book);
        var genreMongo = getGenreMongo(book);

        BookMongo bookMongo = bookRepository.getBookByNameAuthorGenre(book.getName(),
                authorMongo.getId(), genreMongo.getId())
                .orElseThrow(() -> new LibraryConverterException(
                        String.format("Cannot find book with name %s", book.getName())));
        return new CommentMongo(null, comment.getComment(), bookMongo);
    }
}
