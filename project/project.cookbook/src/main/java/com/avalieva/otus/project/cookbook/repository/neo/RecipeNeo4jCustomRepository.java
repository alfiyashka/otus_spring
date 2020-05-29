package com.avalieva.otus.project.cookbook.repository.neo;

import com.avalieva.otus.project.cookbook.domain.neo4j.RecipeNeo4j;
import cookbook.common.dto.RecipeRequest;

import java.util.List;

public interface RecipeNeo4jCustomRepository {
    List<RecipeNeo4j> findRecipes(RecipeRequest recipeRequest);
    List<RecipeNeo4j> findRecipesNotHaveIngredients(RecipeRequest ingredients);
}
