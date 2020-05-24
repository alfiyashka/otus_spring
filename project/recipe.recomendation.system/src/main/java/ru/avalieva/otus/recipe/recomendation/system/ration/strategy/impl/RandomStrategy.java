package ru.avalieva.otus.recipe.recomendation.system.ration.strategy.impl;

import lombok.AllArgsConstructor;
import ru.avalieva.otus.recipe.recomendation.system.dto.RecipeDtoJson;
import ru.avalieva.otus.recipe.recomendation.system.feing.CookbookFeignController;
import ru.avalieva.otus.recipe.recomendation.system.model.ERecipeType;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class RandomStrategy extends AbstractStrategy  {

    private final CookbookFeignController feignController;

    @Override
    public RecipeDtoJson getBreakfast() {
        List<RecipeDtoJson> recipes = feignController.getRecipesByType(ERecipeType.BREAKFAST.toString());
        return getRandomRecipe(recipes);
    }

    @Override
    public List<RecipeDtoJson> getLunch() {
        List<RecipeDtoJson> recipesSoup = feignController.getRecipesByType(ERecipeType.SOUP.toString());
        RecipeDtoJson soup = getRandomRecipe(recipesSoup);

        List<RecipeDtoJson> recipesMainCourse = feignController.getRecipesByType(ERecipeType.MAIN_COURSE.toString());
        RecipeDtoJson mainCourse = getRandomRecipe(recipesMainCourse);

        List<RecipeDtoJson> recipesSideDishes = feignController.getRecipesByType(ERecipeType.SIDE_DISHES.toString());
        RecipeDtoJson sideDish = getRandomRecipe(recipesSideDishes);

        List<RecipeDtoJson> recipesDesserts = feignController.getRecipesByType(ERecipeType.DESSERTS.toString());
        RecipeDtoJson dessert = getRandomRecipe(recipesDesserts);

        List<RecipeDtoJson> recipesSalad = feignController.getRecipesByType(ERecipeType.SALAD.toString());
        RecipeDtoJson salad = getRandomRecipe(recipesSalad);

        List<RecipeDtoJson> result = new ArrayList<>();
        result.add(soup);
        result.add(mainCourse);
        result.add(sideDish);
        result.add(dessert);
        result.add(salad);
        return result;
    }

    @Override
    public List<RecipeDtoJson> getDinner() {
        List<RecipeDtoJson> recipesMainCourse = feignController.getRecipesByType(ERecipeType.MAIN_COURSE.toString());
        RecipeDtoJson mainCourse = getRandomRecipe(recipesMainCourse);

        List<RecipeDtoJson> recipesSideDishes = feignController.getRecipesByType(ERecipeType.SIDE_DISHES.toString());
        RecipeDtoJson sideDish = getRandomRecipe(recipesSideDishes);

        List<RecipeDtoJson> recipesDesserts = feignController.getRecipesByType(ERecipeType.DESSERTS.toString());
        RecipeDtoJson dessert = getRandomRecipe(recipesDesserts);

        List<RecipeDtoJson> result = new ArrayList<>();
        result.add(mainCourse);
        result.add(sideDish);
        result.add(dessert);
        return result;
    }
}
