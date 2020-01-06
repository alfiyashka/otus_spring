package com.otus.avalieva.library.service;

import com.otus.avalieva.library.domain.Author;
import com.otus.avalieva.library.domain.Book;
import com.otus.avalieva.library.domain.Genre;

public interface LibraryService {
    void printAllGenres();
    void printAllBooks();
    void printAllAuthors();

    void findBookByISBNAndPrint(long isbn);
    void findBookByNameAndPrint(String name);
    void findBookByAuthorAndPrint(String firstName, String lastName);

    void addNewBook(Book book);
    void addNewAuthor(Author author);
    void addNewGenre(Genre genre);

    void deleteBook(long isbn);
    void deleteAuthor(long id);
    void deleteGenre(long id);

}
