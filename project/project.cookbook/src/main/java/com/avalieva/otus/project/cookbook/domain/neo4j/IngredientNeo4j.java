package com.avalieva.otus.project.cookbook.domain.neo4j;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.*;


@NodeEntity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IngredientNeo4j {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public IngredientNeo4j(String name) {
        this.name = name;
    }

}
