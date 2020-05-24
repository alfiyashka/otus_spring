package ru.avalieva.otus.recipe.recomendation.system.domain;

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
@Document(collection = "diet")
public class Diet {
    @MongoId(value = FieldType.OBJECT_ID)
    private String id;

    @Field(name = "name")
    private String name;

    @Field(name = "strategy")
    private String strategy;

    @Field(name = "description")
    private String description;
}
