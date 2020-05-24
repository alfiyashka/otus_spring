package ru.avalieva.otus.recipe.recomendation.system.ration.strategy.impl;

import ru.avalieva.otus.recipe.recomendation.system.dto.RecipeDtoJson;
import ru.avalieva.otus.recipe.recomendation.system.dto.RecipeRequest;
import ru.avalieva.otus.recipe.recomendation.system.feing.CookbookFeignController;
import ru.avalieva.otus.recipe.recomendation.system.model.ERecipeType;

import java.util.ArrayList;
import java.util.List;

public class SoupDietStrategy extends AbstractStrategy {

    private final CookbookFeignController feignController;
    private final List<String> ingredients = new ArrayList<>();

    public SoupDietStrategy(CookbookFeignController feignController) {
        this.feignController = feignController;
        ingredients.add("РЫБА");
        ingredients.add("КОРТОФЕЛЬ");
        ingredients.add("ФАСОЛЬ");
        ingredients.add("ГОРОХ");
        ingredients.add("ГОВЯДИНА");
        ingredients.add("СВИНИНА");
        ingredients.add("КУРИЦА");
        ingredients.add("МЯСНОЙ ФАРШ");
        ingredients.add("ФАРШ");
        ingredients.add("КОЛБАСА");
    }

    @Override
    public RecipeDtoJson getBreakfast() {
        RecipeRequest recipeRequest = new RecipeRequest(null, ERecipeType.SOUP.toString(), ingredients);
        List<RecipeDtoJson> recipes = feignController.getRecipesHasNotIngedient(recipeRequest);
        return getRandomRecipe(recipes);
    }

    @Override
    public List<RecipeDtoJson> getLunch() {
        RecipeRequest recipeRequestSoup = new RecipeRequest(null, ERecipeType.SOUP.toString(), ingredients);
        List<RecipeDtoJson> recipesSoup = feignController.getRecipesHasNotIngedient(recipeRequestSoup);
        RecipeDtoJson soup = getRandomRecipe(recipesSoup);

        RecipeRequest recipeRequestSalad = new RecipeRequest(null, ERecipeType.SALAD.toString(), ingredients);
        List<RecipeDtoJson> recipesSalad = feignController.getRecipesHasNotIngedient(recipeRequestSalad);
        RecipeDtoJson salad = getRandomRecipe(recipesSalad);

        List<RecipeDtoJson> result = new ArrayList<>();
        result.add(soup);
        result.add(salad);
        return result;
    }

    @Override
    public List<RecipeDtoJson> getDinner() {
        RecipeRequest recipeRequestMainCourse = new RecipeRequest(null, ERecipeType.SOUP.toString(), ingredients);
        List<RecipeDtoJson> recipesMainCourse = feignController.getRecipesHasNotIngedient(recipeRequestMainCourse);
        RecipeDtoJson mainCourse = getRandomRecipe(recipesMainCourse);

        List<RecipeDtoJson> result = new ArrayList<>();
        result.add(mainCourse);
        return result;
    }
}
