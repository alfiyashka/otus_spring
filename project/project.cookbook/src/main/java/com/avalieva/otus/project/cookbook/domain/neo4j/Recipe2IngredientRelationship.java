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
@RelationshipEntity(type = RelationshipTypes.RECIPE2INGREDIENT)
public class Recipe2IngredientRelationship {
    @Id
    @GeneratedValue
    private Long graphId;

    @StartNode
    private RecipeNeo4j recipe;

    @EndNode
    private IngredientNeo4j ingredient;

    @Property
    private float weight = 0;


    public Recipe2IngredientRelationship(RecipeNeo4j recipe, IngredientNeo4j ingredient, float weight)
    {
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.weight = weight;
    }
}
