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
@Document(collection = "genres")
public class GenreMongo {
    @MongoId(value = FieldType.OBJECT_ID)
    private String id;

    @Field(name = "genre_name")
    private String genreName;
}

