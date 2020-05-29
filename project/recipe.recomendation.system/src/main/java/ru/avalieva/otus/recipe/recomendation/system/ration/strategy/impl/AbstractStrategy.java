package ru.avalieva.otus.recipe.recomendation.system.ration.strategy.impl;

import cookbook.common.dto.*;
import cookbook.common.model.ERation;
import ru.avalieva.otus.recipe.recomendation.system.ration.strategy.RationStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public abstract class AbstractStrategy implements RationStrategy {
    private final RationDtoConverter rationDtoConverter = new RationDtoConverter();

    protected RecipeDtoJson getRandomRecipe(List<RecipeDtoJson> recipes) {
        if (recipes.size() == 0) {
            return null;
        }
        Random random = new Random();
        int recipesCount = recipes.size();
        int randomNum = random.nextInt((recipesCount - 1) + 1) + 1;
        return recipes.get(randomNum - 1);
    }

    @Override
    public List<RecipeForRationDto> getAll() {
        List<RecipeForRationDto> recipes = new ArrayList<>();
        RecipeDtoJson breakfast = getBreakfast();
        if (breakfast != null) {
            List<RecipeDtoJson> recipeDtoList = new ArrayList<>();
            recipeDtoList.add(breakfast);
            recipes.add(new RecipeForRationDto(rationDtoConverter.convert(ERation.BREAKFAST),
                    recipeDtoList.stream().map(RecipeDtoJsonFullConvertor::convert).collect(Collectors.toList())));
        }
        List<RecipeDtoJson> lunch = getLunch();
        if (lunch != null) {
            recipes.add(new RecipeForRationDto(rationDtoConverter.convert(ERation.LUNCH),
                    lunch.stream()
                            .filter(it -> it != null)
                            .map(RecipeDtoJsonFullConvertor::convert)
                            .collect(Collectors.toList())));
        }
        List<RecipeDtoJson> dinner = getDinner();
        if (dinner != null) {
            recipes.add(new RecipeForRationDto(rationDtoConverter.convert(ERation.DINNER),
                    dinner.stream()
                            .filter(it -> it != null)
                            .map(RecipeDtoJsonFullConvertor::convert)
                            .collect(Collectors.toList())));
        }
        return recipes;
    }
}
