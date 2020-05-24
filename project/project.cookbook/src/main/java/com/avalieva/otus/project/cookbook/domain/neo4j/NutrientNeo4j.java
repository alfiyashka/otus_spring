package com.avalieva.otus.project.cookbook.domain.neo4j;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NutrientNeo4j {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public NutrientNeo4j(String name) {
        this.name = name;
    }
}
