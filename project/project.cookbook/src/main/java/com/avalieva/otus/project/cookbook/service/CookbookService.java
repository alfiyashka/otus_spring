package com.avalieva.otus.project.cookbook.service;

import com.avalieva.otus.project.cookbook.domain.mongo.Recipe;
import com.avalieva.otus.project.cookbook.dto.RecipeDtoJson;
import cookbook.common.dto.IngredientDto;
import cookbook.common.dto.NutrientDto;
import cookbook.common.dto.RecipeRequest;
import cookbook.common.dto.RecipeTypeDto;

import java.util.List;

public interface CookbookService {
    void addRecipe(Recipe recipe,
                   List<IngredientDto> ingredientDtos,
                   List<NutrientDto> nutrientDtos,
                   RecipeTypeDto recipeTypeDto);
    void addRecipe(RecipeDtoJson recipe);
    void clearNeo4jData();

    List<String> getIngredients();
    List<String> getRecipeTypes();
    List<RecipeDtoJson> getRecipesByType(String recipeType);
    List<RecipeDtoJson> findRecipes(RecipeRequest recipeRequest);
    List<RecipeDtoJson> findRecipesNotHaveIngredients(RecipeRequest recipeRequest);

}
