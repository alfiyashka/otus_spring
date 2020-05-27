package ru.avalieva.otus.recipe.recomendation.system.ration.strategy;


import cookbook.common.dto.RecipeDtoJson;
import cookbook.common.dto.RecipeForRationDto;

import java.util.List;

public interface RationStrategy {
    RecipeDtoJson getBreakfast();
    List<RecipeDtoJson> getLunch();
    List<RecipeDtoJson> getDinner();
    List<RecipeForRationDto> getAll();
}
