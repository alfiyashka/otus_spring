package ru.avalieva.otus.hw14SpringBatch.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "books")
public class BookMongo {
    @MongoId(value = FieldType.OBJECT_ID)
    private String isbn;

    @Field
    private String name;

    @Field(name = "publishYear")
    private int publishingYear;

    @DBRef
    private AuthorMongo author;

    @DBRef
    private GenreMongo genre;
}
