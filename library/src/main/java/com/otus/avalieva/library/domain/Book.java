package com.otus.avalieva.library.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Book {
    private long isbn;
    private String name;
    private int publishingYear;
    private Author author;
    private Genre genre;

    public Book(Long isbn, String name, int publishingYear, Long authorId, Long genreId) {
        this.isbn = isbn;
        this.name = name;
        this.publishingYear = publishingYear;
        Author author = new Author();
        author.setId(authorId);
        this.author = author;
        Genre genre = new Genre();
        genre.setId(genreId);
        this.genre = genre;
    }

    public String genreName() {
        return genre.getGenreName();
    }

    public String authorFullName() {
        return author.getFirstName() + " " + author.getLastName();
    }
}
