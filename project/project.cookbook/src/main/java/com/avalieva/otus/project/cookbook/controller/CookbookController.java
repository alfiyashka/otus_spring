package com.avalieva.otus.project.cookbook.controller;

import com.avalieva.otus.project.cookbook.dto.RecipeDtoJson;
import com.avalieva.otus.project.cookbook.service.CookbookService;
import cookbook.common.dto.RecipeDtoJsonFull;
import cookbook.common.dto.RecipeRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class CookbookController {

    private final CookbookService cookbookService;

    @GetMapping("/api/ingredient")
    public List<String> getAllIngredents() {
        return cookbookService.getIngredients();
    }

    @PostMapping("/api/recipe")
    public void addRecipe(@RequestBody RecipeDtoJson recipeDtoJson) {
        cookbookService.addRecipe(recipeDtoJson);
    }

    @GetMapping("/api/recipeType")
    public List<String> getAllRecipeTypes() {
        return cookbookService.getRecipeTypes();
    }

    @GetMapping("/api/{recipeType}/recipe")
    List<RecipeDtoJson> getRecipesByType(@PathVariable String recipeType) {
        return cookbookService.getRecipesByType(recipeType);
    }

    @PostMapping("/api/recipe/")
    List<RecipeDtoJson> findRecipes(@RequestBody RecipeRequest recipeRequest,
                                    @RequestParam boolean findHasIngredient) {
        return findHasIngredient
                ? cookbookService.findRecipes(recipeRequest)
                : cookbookService.findRecipesNotHaveIngredients(recipeRequest);
    }
}
