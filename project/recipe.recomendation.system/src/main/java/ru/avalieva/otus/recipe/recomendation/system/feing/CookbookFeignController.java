package ru.avalieva.otus.recipe.recomendation.system.feing;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.avalieva.otus.recipe.recomendation.system.dto.RecipeDtoJson;
import ru.avalieva.otus.recipe.recomendation.system.dto.RecipeRequest;

import java.util.List;

@FeignClient(name = "cookbook")
public interface CookbookFeignController {

    @GetMapping("/api/ingredient")
    List<String> getIngredients();

    @GetMapping("/api/recipeType")
    List<String> getRecipeTypes();

    @GetMapping("/api/{recipeType}/recipe")
    List<RecipeDtoJson> getRecipesByType(@PathVariable String recipeType);

    @PostMapping("/api/recipe")
    void addRecipe(@RequestBody RecipeDtoJson recipeDtoJson);

    @PostMapping("/api/recipe/find")
    List<RecipeDtoJson> findRecipe(@RequestBody RecipeRequest recipeRequest);

    @GetMapping("/api/recipe/notHave")
    List<RecipeDtoJson> getRecipesHasNotIngedient(@RequestBody RecipeRequest ingredients);
}
