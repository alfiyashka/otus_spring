package ru.avalieva.otus.recipe.recomendation.system.service;

import ru.avalieva.otus.recipe.recomendation.system.dto.*;
import ru.avalieva.otus.recipe.recomendation.system.model.ERationStrategy;

import java.util.List;

public interface RecipeManagerService {
    List<RationStrategyDto> getStrategyList();
    List<RecipeForRationDto> getRecipes(ERationStrategy rationStrategy);
    List<RecipeTypeDto> getRecipeTypes();
    List<String> getAllIngredients();
    List<RecipeDtoJson> getRecipesByType(String recipeType);
    List<RecipeDtoJson> findRecipe(RecipeRequest recipeRequest);
    void addRecipe(RecipeDtoJson recipeDtoJson);
    String getRationDescription(String strategy);
}
