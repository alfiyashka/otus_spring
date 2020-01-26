package ru.avalieva.otus.libraryMongoDB_hw08.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "authors")
public class Author {
    @MongoId(value = FieldType.OBJECT_ID)
    private String id;

    @Field(name = "firstName")
    private String firstName;

    @Field(name = "lastName")
    private String lastName;

    @Field(name = "phoneNumber")
    private String phoneNumber;

    @Field(name = "email")
    private String email;

    public String fullName() {
        return String.format("%s %s", firstName, lastName);
    }

}