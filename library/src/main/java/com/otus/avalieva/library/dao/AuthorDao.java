package com.otus.avalieva.library.dao;

import com.otus.avalieva.library.domain.Author;

import java.util.List;

public interface AuthorDao {
    List<Author> allAuthors();
    void addAuthor(Author author);
    Author findAuthorById(long id);
    Author findAuthorByName(String firstName, String lastName);
    void deleteAuthorById(long id);
}
