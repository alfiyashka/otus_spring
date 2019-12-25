package com.otus.avalieva.library.dao;


import com.otus.avalieva.library.domain.Book;

import java.util.List;

public interface BookDao {
    List<Book> allBooks();
    void addBook(Book book);
    Book findBookByIsbn(long id);
    List<Book> findBookByName(String name);
    List<Book> findBookByGenre(String genre);
    List<Book> findBookByAuthor(String firstName, String lastName);
    void deleteBookById(long id);
}
