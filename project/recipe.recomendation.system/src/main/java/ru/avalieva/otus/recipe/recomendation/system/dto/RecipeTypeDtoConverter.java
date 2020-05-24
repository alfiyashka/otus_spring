package ru.avalieva.otus.recipe.recomendation.system.dto;

import ru.avalieva.otus.recipe.recomendation.system.model.ERecipeType;

public class RecipeTypeDtoConverter {
    public static RecipeTypeDto convert(ERecipeType recipeType) {
        return new RecipeTypeDto(recipeType.name(), recipeType.getValue());
    }
}
