package ru.avalieva.otus.recipe.recomendation.system.service;

import cookbook.common.dto.*;
import cookbook.common.model.ERationStrategy;

import java.util.List;

public interface RecipeManagerService {
    List<RationStrategyDto> getStrategyList();
    List<RecipeForRationDto> getRecipes(ERationStrategy rationStrategy);
    List<RecipeTypeDto> getRecipeTypes();
    List<String> getAllIngredients();
    List<RecipeDtoJsonFull> getRecipesByType(String recipeType);
    List<RecipeDtoJsonFull> findRecipe(RecipeRequest recipeRequest, boolean findHasIngredient);
    void addRecipe(RecipeDtoJson recipeDtoJson);
    String getRationDescription(String strategy);
}
