package ru.avalieva.com.library_hw11_webflux.domain;

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
    private Book book;

    public static Comment create(String commentValue, Book book) {
        Comment comment = new Comment();
        comment.setComment(commentValue);
        comment.setBook(book);
        return comment;
    }
}
