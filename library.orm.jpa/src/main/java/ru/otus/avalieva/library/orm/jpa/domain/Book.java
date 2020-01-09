package ru.otus.avalieva.library.orm.jpa.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long isbn;

    @Column
    private String name;

    @Column(name = "publishing_year")
    private int publishingYear;

    @OneToOne(targetEntity = Author.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "author_id")
    private Author author;

    @OneToOne(targetEntity = Genre.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "genre_id")
    private Genre genre;

    public Book(long isbn, String name, int publishingYear, long authorId, long genreId) {
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

