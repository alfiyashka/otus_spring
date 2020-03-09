package ru.avalieva.otus.hw14SpringBatch.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "authors")
public class AuthorMongo {
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
