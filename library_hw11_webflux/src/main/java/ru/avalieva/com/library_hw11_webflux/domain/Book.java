package ru.avalieva.com.library_hw11_webflux.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "books")
public class Book {
    @MongoId(value = FieldType.OBJECT_ID)
    private String isbn;

    @Field
    private String name;

    @Field(name = "publishYear")
    private int publishingYear;

    @DBRef
    private Author author;

    @DBRef
    private Genre genre;

    public String genreName() {
        return genre.getGenreName();
    }

    public String authorFullName() {
        return author.getFirstName() + " " + author.getLastName();
    }

}

