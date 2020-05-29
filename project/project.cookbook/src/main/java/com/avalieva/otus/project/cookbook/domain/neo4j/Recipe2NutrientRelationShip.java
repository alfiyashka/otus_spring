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
@RelationshipEntity(type = RelationshipTypes.RECIPE2NUTRIENT)
public class Recipe2NutrientRelationShip {

    @Id
    @GeneratedValue
    private Long graphId;

    @StartNode
    private RecipeNeo4j recipeNeo4j;

    @EndNode
    private NutrientNeo4j nutrient;

    @Property
    private float weight = 0;

    public Recipe2NutrientRelationShip(RecipeNeo4j recipeNeo4j, NutrientNeo4j nutrient, float weight)
    {
        this.nutrient = nutrient;
        this.recipeNeo4j = recipeNeo4j;
        this.weight = weight;
    }
}
