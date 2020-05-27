package com.avalieva.otus.project.cookbook.domain.neo4j;


import cookbook.common.dto.IngredientDto;
import cookbook.common.dto.NutrientDto;
import cookbook.common.model.ENutrient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NodeEntity
@Getter
@NoArgsConstructor
public class RecipeNeo4j {
    @Id
    @GeneratedValue
    private Long recipeId;

    private String name;

    private String mongoId;


    @Relationship(type = RelationshipTypes.RECIPE2INGREDIENT)
    private Set<Recipe2IngredientRelationship> recipe2IngredientRelationship = new HashSet<>();


    public List<IngredientDto> getIngredients() {
        return recipe2IngredientRelationship
                .stream()
                .map(it -> new IngredientDto(it.getIngredient().getName(), it.getWeight()))
                .collect(Collectors.toList());
    }

    public void addIngredient(IngredientNeo4j ingredientNeo4j, float weight)
    {
        Recipe2IngredientRelationship recommendRelationship = new Recipe2IngredientRelationship(this, ingredientNeo4j, weight);

        if (!this.recipe2IngredientRelationship.contains(recommendRelationship))
        {
            recipe2IngredientRelationship.add(recommendRelationship);
        }
    }

    @Relationship(type = RelationshipTypes.RECIPE2NUTRIENT)
    private Set<Recipe2NutrientRelationShip> ingredient2NutrientsRelationships = new HashSet<>();

    public void addNutrient(NutrientNeo4j nutrientNeo4j, float weight)
    {
        Recipe2NutrientRelationShip recommendRelationship =
                new Recipe2NutrientRelationShip(this, nutrientNeo4j, weight);

        if (!this.ingredient2NutrientsRelationships.contains(recommendRelationship))
        {
            ingredient2NutrientsRelationships.add(recommendRelationship);
        }
    }

    public List<NutrientDto> getNutrients() {
        return ingredient2NutrientsRelationships
                .stream()
                .map(it -> new NutrientDto(ENutrient.valueOf(it.getNutrient().getName()), it.getWeight()))
                .collect(Collectors.toList());
    }

    @Relationship(type = RelationshipTypes.RECIPE_TYPE)
    private Set<RecipeTypeRelationship> recipeTypeRelationships = new HashSet<>();

    public void addRecipeType(RecipeTypeNeo4j recipeTypeNeo4j)
    {
        RecipeTypeRelationship recommendRelationship =
                new RecipeTypeRelationship(this, recipeTypeNeo4j);

        if (!this.recipeTypeRelationships.contains(recommendRelationship))
        {
            recipeTypeRelationships.add(recommendRelationship);
        }
    }

    public String getRecipeType() {
        return recipeTypeRelationships
                .stream()
                .map(it -> it.getRecipeType().getName())
                .collect(Collectors.toList())
                .get(0);
    }


    public RecipeNeo4j(String name, String mongoId) {
        this.name = name;
        this.mongoId = mongoId;
    }

}
