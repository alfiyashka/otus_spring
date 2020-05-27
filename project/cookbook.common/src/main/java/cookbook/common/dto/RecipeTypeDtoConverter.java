package cookbook.common.dto;

import cookbook.common.config.EnumProperties;
import cookbook.common.model.ERecipeType;

public class RecipeTypeDtoConverter {
    public static RecipeTypeDto convert(ERecipeType recipeType) {
        String value = EnumProperties.getProperties().getProperty(String.format("ERecipeType.%s", recipeType.name()));
        return new RecipeTypeDto(recipeType.name(), value);
    }
}
