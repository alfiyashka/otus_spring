package ru.avalieva.otus.recipe.recomendation.system.controller;

import cookbook.common.dto.*;
import cookbook.common.model.ENutrient;
import cookbook.common.model.ERationStrategy;
import cookbook.common.model.ERecipeType;
import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import ru.avalieva.otus.recipe.recomendation.system.service.RecipeManagerService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/recipeSystem/")
public class RecipesManagerController {
    private final RecipeManagerService recipeManagerService;

    @GetMapping("/api/ingredient")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public List<String> getAllIngredients() {
        return recipeManagerService.getAllIngredients();
    }


    @PostMapping("/api/recipe")
    public void addRecipe(@RequestBody RecipeDtoJson recipeDtoJson) {
        recipeManagerService.addRecipe(recipeDtoJson);
    }

    @PostMapping("/api/recipe/")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public List<RecipeDtoJsonFull> findRecipe(@RequestBody RecipeRequest recipeRequest,
                                          @RequestParam boolean findHasIngredient) {
        return recipeManagerService.findRecipe(recipeRequest, findHasIngredient);
    }

    @GetMapping("/api/recipeType")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public List<RecipeTypeDto> getRecipeTypes() {
        return recipeManagerService.getRecipeTypes();
    }

    @GetMapping("/api/recipeType/{recipeType}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String getRecipeTypeValue(@PathVariable String recipeType) {
        return RecipeTypeDtoConverter.convert(ERecipeType.valueOf(recipeType)).getRecipeTypeValue();
    }

    @GetMapping("/api/strategy")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public List<RationStrategyDto> getСriterionList() {
        return recipeManagerService.getStrategyList();
    }

    @GetMapping("/api/recipeType/{recipeType}/recipe")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public List<RecipeDtoJsonFull> getRecipesByType(@PathVariable String recipeType) {
        return recipeManagerService.getRecipesByType(recipeType);
    }

    @GetMapping("/api/recipeRation/{rationStrategy}/recipe")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public List<RecipeForRationDto> getRecipesByRationStrategy(@PathVariable String rationStrategy) {
        return recipeManagerService.getRecipes(ERationStrategy.valueOf(rationStrategy));
    }

    @GetMapping("/api/diet/{dietStrategy}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String getDietDescription(@PathVariable String dietStrategy) {
        return recipeManagerService.getRationDescription(dietStrategy);
    }
}
