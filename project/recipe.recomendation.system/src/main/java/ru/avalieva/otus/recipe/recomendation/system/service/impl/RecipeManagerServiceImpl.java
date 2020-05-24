package ru.avalieva.otus.recipe.recomendation.system.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.avalieva.otus.recipe.recomendation.system.domain.Diet;
import ru.avalieva.otus.recipe.recomendation.system.dto.*;
import ru.avalieva.otus.recipe.recomendation.system.feing.CookbookFeignController;
import ru.avalieva.otus.recipe.recomendation.system.model.ERecipeType;
import ru.avalieva.otus.recipe.recomendation.system.model.RecipeManagerException;
import ru.avalieva.otus.recipe.recomendation.system.ration.strategy.RationStrategy;
import ru.avalieva.otus.recipe.recomendation.system.model.ERationStrategy;
import ru.avalieva.otus.recipe.recomendation.system.ration.strategy.impl.RationStrategyFactory;
import ru.avalieva.otus.recipe.recomendation.system.repository.DietRepository;
import ru.avalieva.otus.recipe.recomendation.system.service.MessageService;
import ru.avalieva.otus.recipe.recomendation.system.service.RecipeManagerService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RecipeManagerServiceImpl implements RecipeManagerService {

    private final CookbookFeignController feignController;

    private final DietRepository dietRepository;

    private final MessageService messageService;

    @Override
    public List<RationStrategyDto> getStrategyList() {
        return Arrays.asList(ERationStrategy.values())
                .stream()
                .map(RationStrategyDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeForRationDto> getRecipes(ERationStrategy rationStrategy) {
        try {
            RationStrategy strategy = RationStrategyFactory.getStrategy(rationStrategy, feignController);
            return strategy.getAll();
        }
        catch (Exception e) {
            throw new RecipeManagerException(messageService.getMessage("get.recipes.error", e.getMessage()), e);
        }
    }

    @Override
    public List<String> getAllIngredients() {
        try {
            return feignController.getIngredients();
        }
        catch (Exception e) {
            throw new RecipeManagerException(messageService.getMessage("get.ingredients.error", e.getMessage()), e);
        }
    }

    @Override
    public void addRecipe(RecipeDtoJson recipeDtoJson) {
        try {
            feignController.addRecipe(recipeDtoJson);
        }
        catch (Exception e) {
            throw new RecipeManagerException(messageService.getMessage("add.recipe.error", e.getMessage()), e);
        }
    }

    @Override
    public List<RecipeDtoJson> findRecipe(RecipeRequest recipeRequest) {
        try {
            return feignController.findRecipe(recipeRequest);
        }
        catch (Exception e) {
            throw new RecipeManagerException(messageService.getMessage("find.recipe.error", e.getMessage()), e);
        }
    }

    @Override
    public List<RecipeTypeDto> getRecipeTypes() {
        try {
            return feignController.getRecipeTypes()
                    .stream()
                    .map(it -> ERecipeType.valueOf(it))
                    .map(RecipeTypeDtoConverter::convert)
                    .collect(Collectors.toList());
        }
        catch (Exception e) {
            throw new RecipeManagerException(messageService.getMessage("get.recipe.collection.error", e.getMessage()), e);
        }
    }

    @Override
    public List<RecipeDtoJson> getRecipesByType(String recipeType) {
        try {
            return feignController.getRecipesByType(recipeType);
        }
        catch (Exception e) {
            throw new RecipeManagerException(messageService.getMessage("get.recipes.by.collection.error", recipeType, e.getMessage()), e);
        }
    }

    @Override
    public String getRationDescription(String strategy) {
        try {
            Diet diet = dietRepository.findByStrategy(strategy)
                    .orElseThrow(() -> new RecipeManagerException(messageService.getMessage("diet.not.found.error", strategy)));
            return diet.getDescription();
        }
        catch (Exception e) {
            throw new RecipeManagerException(messageService.getMessage("get.diet.description.error", strategy, e.getMessage()), e);
        }
    }

}
