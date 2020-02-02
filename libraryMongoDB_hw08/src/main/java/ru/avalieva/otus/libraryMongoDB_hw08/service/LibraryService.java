package ru.avalieva.otus.libraryMongoDB_hw08.service;

import ru.avalieva.otus.libraryMongoDB_hw08.domain.Author;
import ru.avalieva.otus.libraryMongoDB_hw08.domain.Book;
import ru.avalieva.otus.libraryMongoDB_hw08.domain.Genre;

public interface LibraryService {
    void printAllGenres();
    void printAllBooks();
    void printAllAuthors();

    void findBookByISBNAndPrint(String isbn);
    void findBookByNameAndPrint(String name);
    void findBookByAuthorAndPrint(String firstName, String lastName);
    void findBookByGenreAndPrint(String genre);

    void addNewBook(Book book);
    void addNewAuthor(Author author);
    void addNewGenre(Genre genre);

    void deleteBook(String isbn);
    void deleteAuthor(String id);
    void deleteGenre(String id);

    void addBookComment(String isbn, String comment);
    void findCommentByBookIdAndPrint(String isbn);

    Author findAuthorById(String id);
    Genre findGenreById(String id);
}
