package ru.otus.avaliva.hw17.library.docker.service;

import ru.otus.avaliva.hw17.library.docker.domain.Author;
import ru.otus.avaliva.hw17.library.docker.domain.Book;
import ru.otus.avaliva.hw17.library.docker.domain.Comment;
import ru.otus.avaliva.hw17.library.docker.domain.Genre;

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