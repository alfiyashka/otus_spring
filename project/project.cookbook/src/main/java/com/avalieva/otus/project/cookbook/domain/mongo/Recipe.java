package com.avalieva.otus.project.cookbook.domain.mongo;

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
@Document(collection = "recipe")
public class Recipe {
    @MongoId(value = FieldType.OBJECT_ID)
    private String id;

    @Field(name = "name")
    private String name;

    @Field(name = "total_calorie")
    private int totalCalorie;

    @Field(name = "description")
    private String description;
}
