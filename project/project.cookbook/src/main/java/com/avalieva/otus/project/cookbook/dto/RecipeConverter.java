package com.avalieva.otus.project.cookbook.dto;

import com.avalieva.otus.project.cookbook.domain.mongo.Recipe;
import cookbook.common.dto.IngredientDto;
import cookbook.common.dto.NutrientDto;

import java.util.List;

public class RecipeConverter {
    public static RecipeDtoJson convert(Recipe recipe, String recipeType, List<IngredientDto> ingredients, List<NutrientDto> nutrientDtos) {
        return new RecipeDtoJson(recipe.getName(), recipeType, recipe.getTotalCalorie(), recipe.getDescription(), ingredients, nutrientDtos);
    }

}
