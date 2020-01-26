package ru.avalieva.otus.libraryMongoDB_hw08.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "comments")
public class Comment {
    @MongoId(value = FieldType.OBJECT_ID)
    private String id;

    @Field(name = "comment")
    private String comment;

    @DBRef
    Book book;
}
