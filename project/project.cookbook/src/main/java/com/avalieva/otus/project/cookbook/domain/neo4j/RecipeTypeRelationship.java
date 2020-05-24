package com.avalieva.otus.project.cookbook.domain.neo4j;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.neo4j.ogm.annotation.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@RelationshipEntity(type = RelationshipTypes.RECIPE_TYPE)
public class RecipeTypeRelationship {
    @Id
    @GeneratedValue
    private Long graphId;

    @StartNode
    private RecipeNeo4j recipe;

    @EndNode
    private RecipeTypeNeo4j recipeType;

    public RecipeTypeRelationship(RecipeNeo4j recipeNeo4j, RecipeTypeNeo4j recipeType)
    {
        this.recipe = recipeNeo4j;
        this.recipeType = recipeType;
    }
}

