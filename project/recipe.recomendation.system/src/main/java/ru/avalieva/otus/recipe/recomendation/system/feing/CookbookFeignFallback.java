package ru.avalieva.otus.recipe.recomendation.system.feing;

import cookbook.common.dto.RecipeDtoJson;
import cookbook.common.dto.RecipeRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.avalieva.otus.recipe.recomendation.system.model.RecipeManagerException;
import ru.avalieva.otus.recipe.recomendation.system.service.MessageService;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class CookbookFeignFallback implements CookbookFeignController {

    private final MessageService messageService;

    @Override
    public List<String> getIngredients() {
        return new ArrayList<>();
    }

    @Override
    public List<String> getRecipeTypes() {
        return new ArrayList<>();
    }

    @Override
    public List<RecipeDtoJson> getRecipesByType(String recipeType) {
        return new ArrayList<>();
    }

    @Override
    public void addRecipe(RecipeDtoJson recipeDtoJson) {
        throw new RecipeManagerException(messageService.getMessage("save.recipe.error", recipeDtoJson.getName()));
    }

    @Override
    public List<RecipeDtoJson> findRecipe(RecipeRequest recipeRequest, boolean findHasIngredient) {
        return new ArrayList<>();
    }

}
