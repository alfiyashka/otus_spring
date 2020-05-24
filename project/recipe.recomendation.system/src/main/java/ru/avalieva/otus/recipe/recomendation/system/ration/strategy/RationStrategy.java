package ru.avalieva.otus.recipe.recomendation.system.ration.strategy;


import ru.avalieva.otus.recipe.recomendation.system.dto.RecipeDtoJson;
import ru.avalieva.otus.recipe.recomendation.system.dto.RecipeForRationDto;

import java.util.List;

public interface RationStrategy {
    RecipeDtoJson getBreakfast();
    List<RecipeDtoJson> getLunch();
    List<RecipeDtoJson> getDinner();
    List<RecipeForRationDto> getAll();
}
