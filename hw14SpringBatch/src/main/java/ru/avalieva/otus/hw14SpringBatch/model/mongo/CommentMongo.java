package ru.avalieva.otus.hw14SpringBatch.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "comments")
public class CommentMongo {

    @MongoId(value = FieldType.OBJECT_ID)
    private String id;

    @Field(name = "comment")
    private String comment;

    @DBRef
    private BookMongo book;
}
