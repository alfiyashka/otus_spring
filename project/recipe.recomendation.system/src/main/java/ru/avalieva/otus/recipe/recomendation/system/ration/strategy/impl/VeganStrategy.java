package ru.avalieva.otus.recipe.recomendation.system.ration.strategy.impl;

import cookbook.common.dto.RecipeDtoJson;
import cookbook.common.dto.RecipeRequest;
import cookbook.common.model.ERecipeType;
import ru.avalieva.otus.recipe.recomendation.system.feing.CookbookFeignController;

import java.util.ArrayList;
import java.util.List;

public class VeganStrategy extends AbstractStrategy {

    private final CookbookFeignController feignController;
    private final List<String> ingredients = new ArrayList<>();


    public VeganStrategy(CookbookFeignController feignController) {
        this.feignController = feignController;
        ingredients.add("МОЛОКО");
        ingredients.add("ГОВЯДИНА");
        ingredients.add("СВИНИНА");
        ingredients.add("КУРИЦА");
        ingredients.add("КУРИНЫЕ ЯЙЦА");
        ingredients.add("МЯСНОЙ ФАРШ");
        ingredients.add("ФАРШ");
        ingredients.add("КОЛБАСА");
        ingredients.add("КРАСНАЯ РЫБА");
        ingredients.add("РЫБА");
    }

    @Override
    public RecipeDtoJson getBreakfast() {
        RecipeRequest recipeRequest = new RecipeRequest(null, ERecipeType.BREAKFAST.toString(), ingredients);
        List<RecipeDtoJson> recipes = feignController.findRecipe(recipeRequest, false);
        return getRandomRecipe(recipes);
    }

    @Override
    public List<RecipeDtoJson> getLunch() {
        RecipeRequest recipeRequestSoup = new RecipeRequest(null, ERecipeType.SOUP.toString(), ingredients);
        List<RecipeDtoJson> recipesSoup = feignController.findRecipe(recipeRequestSoup, false);
        RecipeDtoJson soup = getRandomRecipe(recipesSoup);

        RecipeRequest recipeRequestMainCourse = new RecipeRequest(null, ERecipeType.MAIN_COURSE.toString(), ingredients);
        List<RecipeDtoJson> recipesMainCourse = feignController.findRecipe(recipeRequestMainCourse, false);
        RecipeDtoJson mainCourse = getRandomRecipe(recipesMainCourse);

        RecipeRequest recipeRequestSideDishes = new RecipeRequest(null, ERecipeType.SIDE_DISHES.toString(), ingredients);
        List<RecipeDtoJson> recipesSideDishes = feignController.findRecipe(recipeRequestSideDishes, false);
        RecipeDtoJson sideDish = getRandomRecipe(recipesSideDishes);

        RecipeRequest recipeRequestDesserts = new RecipeRequest(null, ERecipeType.DESSERTS.toString(), ingredients);
        List<RecipeDtoJson> recipesDesserts = feignController.findRecipe(recipeRequestDesserts, false);
        RecipeDtoJson dessert = getRandomRecipe(recipesDesserts);

        RecipeRequest recipeRequestSalad = new RecipeRequest(null, ERecipeType.SALAD.toString(), ingredients);
        List<RecipeDtoJson> recipesSalad = feignController.findRecipe(recipeRequestSalad, false);
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
        RecipeRequest recipeRequestMainCourse = new RecipeRequest(null, ERecipeType.MAIN_COURSE.toString(), ingredients);
        List<RecipeDtoJson> recipesMainCourse = feignController.findRecipe(recipeRequestMainCourse, false);
        RecipeDtoJson mainCourse = getRandomRecipe(recipesMainCourse);

        RecipeRequest recipeRequestSideDishes = new RecipeRequest(null, ERecipeType.SIDE_DISHES.toString(), ingredients);
        List<RecipeDtoJson> recipesSideDishes = feignController.findRecipe(recipeRequestSideDishes, false);
        RecipeDtoJson sideDish = getRandomRecipe(recipesSideDishes);

        RecipeRequest recipeRequestDesserts = new RecipeRequest(null, ERecipeType.DESSERTS.toString(), ingredients);
        List<RecipeDtoJson> recipesDesserts = feignController.findRecipe(recipeRequestDesserts, false);
        RecipeDtoJson dessert = getRandomRecipe(recipesDesserts);

        List<RecipeDtoJson> result = new ArrayList<>();
        result.add(mainCourse);
        result.add(sideDish);
        result.add(dessert);
        return result;
    }
}
