package com.otus.avalieva.library.service.impl;

import com.otus.avalieva.library.dao.AuthorDao;
import com.otus.avalieva.library.dao.BookDao;
import com.otus.avalieva.library.dao.GenreDao;
import com.otus.avalieva.library.domain.Author;
import com.otus.avalieva.library.domain.Book;
import com.otus.avalieva.library.domain.Genre;
import com.otus.avalieva.library.service.IOService;
import com.otus.avalieva.library.service.LibraryService;
import com.otus.avalieva.library.service.MessageService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibraryServiceImpl implements LibraryService {

    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final BookDao bookDao;
    private final IOService ioService;
    private final MessageService messageService;

    public LibraryServiceImpl(AuthorDao authorDao,
                              GenreDao genreDao,
                              BookDao bookDao,
                              IOService ioService,
                              MessageService messageService) {
        this.authorDao = authorDao;
        this.genreDao = genreDao;
        this.bookDao = bookDao;
        this.ioService = ioService;
        this.messageService = messageService;
    }

    @Override
    public void printAllGenres() {
        try {
            genreDao.allGenres().forEach(it -> ioService.outputData(
                    messageService.getMessage("genre.info", it.getId(), it.getGenreName())
            ));
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("genre.info.error",  e.getMessage()), e);
        }
    }

    private void printBooks(List<Book> books) {
        books.forEach(it -> ioService.outputData(
                messageService.getMessage("book.info",
                        it.getIsbn(), it.getName(), it.authorFullName(), it.genreName())));
    }

    @Override
    public void printAllBooks() {
        try {
            printBooks(bookDao.allBooks());
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("book.info.error", e.getMessage()), e);
        }
    }

    @Override
    public void printAllAuthors() {
        try {
            authorDao.allAuthors().forEach(it -> ioService.outputData(
                    messageService.getMessage("author.info",
                            it.getId(), it.fullName(), it.getEmail(), it.getPhoneNumber())));
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("author.info.error", e.getMessage()), e);
        }
    }

    @Override
    public void findBookByISBNAndPrint(long isbn) {
        try {
            Book book = bookDao.findBookByIsbn(isbn);
            ioService.outputData(
                    messageService.getMessage("book.info",
                            book.getIsbn(), book.getName(), book.authorFullName(), book.genreName())
            );
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("book.id.not.found.error",
                    isbn, e.getMessage()), e);
        }
    }

    @Override
    public void findBookByNameAndPrint(String name) {
        try {
            List<Book> books = bookDao.findBookByName(name);
            if (books.isEmpty()) {
                ioService.outputData(messageService.getMessage("book.name.not.found",  name));
            }
            else {
                printBooks(books);
            }
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("book.name.not.found.error.reason",
                    name, e.getMessage()), e);
        }
    }

    @Override
    public void findBookByAuthorAndPrint(String firstName, String lastName) {
        try {
            List<Book> books = bookDao.findBookByAuthor(firstName, lastName);
            if (books.isEmpty()) {
                ioService.outputData(messageService.getMessage("book.author.not.found",
                        firstName, lastName));
            }
            else {
                printBooks(books);
            }
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("book.author.not.found.error.reason",
                    firstName, lastName, e.getMessage()), e);
        }
    }

    private void checkGenreExistence(long id) {
        try {
            genreDao.findGenreById(id);
        }
        catch(EmptyResultDataAccessException e){
            throw new LibraryException(messageService.getMessage("genre.not.exist", id), e);
        }
    }

    private void checkAuthorExistence(long id) {
        try {
            authorDao.findAuthorById(id);
        }
        catch(EmptyResultDataAccessException e){
            throw new LibraryException(messageService.getMessage("author.not.exist",id), e);
        }
    }

    @Override
    public void addNewBook(Book book) {
        try {
            checkGenreExistence(book.getGenre().getId());
            checkAuthorExistence(book.getAuthor().getId());
            bookDao.addBook(book);
            ioService.outputData(messageService.getMessage("book.added",book.getIsbn()));
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("book.add.error",
                    book.getIsbn(), e.getMessage()), e);
        }
    }

    @Override
    public void addNewAuthor(Author author) {
        try {
            authorDao.addAuthor(author);
            ioService.outputData(messageService.getMessage("author.added", author.fullName()));
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("author.added.error",
                    author.fullName(), e.getMessage()), e);
        }
    }

    @Override
    public void addNewGenre(Genre genre) {
        try {
            genreDao.addGenre(genre);
            ioService.outputData(messageService.getMessage("genre.added", genre.getGenreName()));
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("genre.added.error",
                    genre.getGenreName(), e.getMessage()), e);
        }
    }

    @Override
    public void deleteBook(long isbn) {
        try {
            bookDao.deleteBookById(isbn);
            ioService.outputData(messageService.getMessage("book.deleted", isbn));
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("book.deleted.error",
                    isbn, e.getMessage()), e);
        }
    }

    private Author getAuthor(long authorId) {
        try {
            return authorDao.findAuthorById(authorId);
        }
        catch(EmptyResultDataAccessException e){
            throw new LibraryException(messageService.getMessage("author.not.exist", authorId), e);
        }
    }

    private void checkBookAbsenceForAuthor(long authorId) {
        Author author = getAuthor(authorId);
        List<Book> books = bookDao.findBookByAuthor(author.getFirstName(), author.getLastName());
        if (!books.isEmpty()) {
            throw new LibraryException(messageService.getMessage("book.for.author.exist",
                    author.fullName(), books.stream().map(Book::getName).collect(Collectors.joining(", "))));
        }
    }

    @Override
    public void deleteAuthor(long id) {
        try {
            checkBookAbsenceForAuthor(id);
            authorDao.deleteAuthorById(id);
            ioService.outputData(messageService.getMessage("author.deleted", id));
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("author.deleted.error",
                    id, e.getMessage()), e);
        }
    }

    private Genre getGenre(long genreId) {
        try {
            return genreDao.findGenreById(genreId);
        }
        catch(EmptyResultDataAccessException e){
            throw new LibraryException(messageService.getMessage("genre.not.exist", genreId), e);
        }
    }

    private void checkBookAbsenceForGenre(long genreId) {
        Genre genre = getGenre(genreId);
        List<Book> books = bookDao.findBookByGenre(genre.getGenreName());
        if (!books.isEmpty()) {
            throw new LibraryException(messageService.getMessage("book.for.genre.exist",
                    genre.getGenreName(), books.stream().map(Book::getName).collect(Collectors.joining(", "))));
        }
    }

    @Override
    public void deleteGenre(long id) {
        try {
            checkBookAbsenceForGenre(id);
            genreDao.deleteGenre(id);
            ioService.outputData(messageService.getMessage("genre.deleted", id));
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("genre.deleted.error",
                    id, e.getMessage()), e);
        }
    }
}
