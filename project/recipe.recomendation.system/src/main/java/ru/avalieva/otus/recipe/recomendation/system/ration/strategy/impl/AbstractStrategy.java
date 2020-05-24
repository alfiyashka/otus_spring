package ru.avalieva.otus.recipe.recomendation.system.ration.strategy.impl;

import ru.avalieva.otus.recipe.recomendation.system.dto.RationDto;
import ru.avalieva.otus.recipe.recomendation.system.dto.RecipeDtoJson;
import ru.avalieva.otus.recipe.recomendation.system.dto.RecipeForRationDto;
import ru.avalieva.otus.recipe.recomendation.system.model.ERation;
import ru.avalieva.otus.recipe.recomendation.system.ration.strategy.RationStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public abstract class AbstractStrategy implements RationStrategy {
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
            recipes.add(new RecipeForRationDto(new RationDto(ERation.BREAKFAST.name(), ERation.BREAKFAST.getValue()),
                    recipeDtoList));
        }
        List<RecipeDtoJson> lunch = getLunch();
        if (lunch != null) {
            recipes.add(new RecipeForRationDto(new RationDto(ERation.LUNCH.name(), ERation.LUNCH.getValue()),
                    lunch.stream().filter(it -> it != null).collect(Collectors.toList())));
        }
        List<RecipeDtoJson> dinner = getDinner();
        if (dinner != null) {
            recipes.add(new RecipeForRationDto(new RationDto(ERation.DINNER.name(), ERation.DINNER.getValue()),
                    dinner.stream().filter(it -> it != null).collect(Collectors.toList())));
        }
        return recipes;
    }
}
