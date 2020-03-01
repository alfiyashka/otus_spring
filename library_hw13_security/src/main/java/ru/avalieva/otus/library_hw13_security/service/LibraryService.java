package ru.avalieva.otus.library_hw13_security.service;


import ru.avalieva.otus.library_hw13_security.domain.Author;
import ru.avalieva.otus.library_hw13_security.domain.Book;
import ru.avalieva.otus.library_hw13_security.domain.Comment;
import ru.avalieva.otus.library_hw13_security.domain.Genre;

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
    void deleteComment(long commentId);

    List<Comment> findCommentByBookId(long isbn);
}