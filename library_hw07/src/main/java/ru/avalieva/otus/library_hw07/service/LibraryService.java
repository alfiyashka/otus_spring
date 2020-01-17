package ru.avalieva.otus.library_hw07.service;

import ru.avalieva.otus.library_hw07.domain.Author;
import ru.avalieva.otus.library_hw07.domain.Book;
import ru.avalieva.otus.library_hw07.domain.Genre;

public interface LibraryService {
    void printAllGenres();
    void printAllBooks();
    void printAllAuthors();

    void findBookByISBNAndPrint(long isbn);
    void findBookByNameAndPrint(String name);
    void findBookByAuthorAndPrint(String firstName, String lastName);
    void findBookByGenreAndPrint(String genre);

    void addNewBook(Book book);
    void addNewAuthor(Author author);
    void addNewGenre(Genre genre);

    void deleteBook(long isbn);
    void deleteAuthor(long id);
    void deleteGenre(long id);

    void addBookComment(long isbn, String comment);
    void findCommentByBookIdAndPrint(long isbn);
}
