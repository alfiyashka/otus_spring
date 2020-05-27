package ru.avalieva.otus.recipe.recomendation.system.feing;

import cookbook.common.dto.RecipeDtoJson;
import cookbook.common.dto.RecipeRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "cookbook", fallback = CookbookFeignFallback.class)
public interface CookbookFeignController {

    @GetMapping("/api/ingredient")
    List<String> getIngredients();

    @GetMapping("/api/recipeType")
    List<String> getRecipeTypes();

    @GetMapping("/api/{recipeType}/recipe")
    List<RecipeDtoJson> getRecipesByType(@PathVariable String recipeType);

    @PostMapping("/api/recipe")
    void addRecipe(@RequestBody RecipeDtoJson recipeDtoJson);

    @PostMapping("/api/recipe/")
    List<RecipeDtoJson> findRecipe(@RequestBody RecipeRequest recipeRequest,
                                   @RequestParam boolean findHasIngredient);

}
