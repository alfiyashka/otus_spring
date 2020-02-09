package ru.avalieva.otus.library_hw09_mvc.service;

import ru.avalieva.otus.library_hw09_mvc.domain.Author;
import ru.avalieva.otus.library_hw09_mvc.domain.Book;
import ru.avalieva.otus.library_hw09_mvc.domain.Comment;
import ru.avalieva.otus.library_hw09_mvc.domain.Genre;

import java.util.List;

public interface LibraryService {
    List<Genre> allGenres();
    List<Book> allBooks();
    List<Author> allAuthors();

    Book findBookByISBN(long isbn);
    Author findAuthorByID(long id);
    Genre findGenreByID(long id);

    List<Book> findBookByName(String name);

    void addNewBook(Book book);

    void deleteBook(long isbn);

    void addBookComment(long isbn, String comment);
    List<Comment> findCommentByBookId(long isbn);
}