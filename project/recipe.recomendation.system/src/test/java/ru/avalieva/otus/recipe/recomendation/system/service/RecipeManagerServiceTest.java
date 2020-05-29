package ru.avalieva.otus.recipe.recomendation.system.service;

import com.mongodb.MongoException;
import cookbook.common.dto.*;
import cookbook.common.model.ERationStrategy;
import cookbook.common.model.ERecipeType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import ru.avalieva.otus.recipe.recomendation.system.domain.Diet;
import ru.avalieva.otus.recipe.recomendation.system.feing.CookbookFeignController;
import ru.avalieva.otus.recipe.recomendation.system.ration.strategy.RationStrategy;
import ru.avalieva.otus.recipe.recomendation.system.repository.DietRepository;
import ru.avalieva.otus.recipe.recomendation.system.model.RecipeManagerException;
import ru.avalieva.otus.recipe.recomendation.system.service.impl.RecipeManagerServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;


@ContextConfiguration(classes = RecipeManagerServiceImpl.class)
@SpringBootTest
public class RecipeManagerServiceTest {

    @MockBean
    private MessageService messageService;

    @Autowired
    private RecipeManagerService recipeManager;

    @MockBean
    private CookbookFeignController feignController;

    @MockBean
    private RationStrategy rationStrategy;

    @MockBean
    private DietRepository dietRepository;

    @Test
    public void getStrategyListTest() {
        List<RationStrategyDto> strategyDtos = recipeManager.getStrategyList();
        Assertions.assertEquals(ERationStrategy.values().length, strategyDtos.size());
    }

    @Test
    public void getAllIngredientsTest() {
        List<String> ingredients = new ArrayList<>();
        ingredients.add("Ingredient");
        when(feignController.getIngredients()).thenReturn(ingredients);
        List<String> result = recipeManager.getAllIngredients();
        Assertions.assertEquals(ingredients, result);

        verify(feignController).getIngredients();
    }

    @Test
    public void getAllIngredientsTestFailed() {
        when(feignController.getIngredients()).thenThrow(new RuntimeException("Error"));
        when(messageService.getMessage("get.ingredients.error","Error")).thenReturn("Error");

        RecipeManagerException error = Assertions.assertThrows(RecipeManagerException.class,
                () -> { recipeManager.getAllIngredients(); });
        Assertions.assertEquals("Error", error.getMessage());

        verify(feignController).getIngredients();
        verify(messageService, times(1)).
                getMessage("get.ingredients.error","Error");
    }

    @Test
    public void addRecipeTest() {
        RecipeDtoJson recipe = new RecipeDtoJson();
        doNothing().when(feignController).addRecipe(recipe);
        recipeManager.addRecipe(recipe);

        verify(feignController).addRecipe(recipe);
    }

    @Test
    public void addRecipeTestFailed() {
        RecipeDtoJson recipe = new RecipeDtoJson();
        doThrow(new RuntimeException("Error")).when(feignController).addRecipe(recipe);
        when(messageService.getMessage("add.recipe.error","Error")).thenReturn("Error");

        RecipeManagerException error = Assertions.assertThrows(RecipeManagerException.class,
                () -> { recipeManager.addRecipe(recipe); });
        Assertions.assertEquals("Error", error.getMessage());

        verify(feignController).addRecipe(recipe);
        verify(messageService, times(1)).
                getMessage("add.recipe.error","Error");
    }

    @Test
    public void findRecipeTest() {
        RecipeDtoJson recipe = new RecipeDtoJson("Neme", "Type", 12, "description", null, null);
        List<RecipeDtoJson> recipes = new ArrayList<>();
        recipes.add(recipe);
        RecipeRequest request = new RecipeRequest();
        when(feignController.findRecipe(request, true)).thenReturn(recipes);
        List<RecipeDtoJsonFull> result = recipeManager.findRecipe(request, true);
        Assertions.assertEquals(recipes.stream().map(RecipeDtoJsonFullConvertor::convert).collect(Collectors.toList()), result);

        verify(feignController).findRecipe(request,true);
    }

    @Test
    public void findRecipeTestFailed() {
        RecipeRequest request = new RecipeRequest();
        when(feignController.findRecipe(request, true)).thenThrow(new RuntimeException("Error"));
        when(messageService.getMessage("find.recipe.error","Error")).thenReturn("Error");

        RecipeManagerException error = Assertions.assertThrows(RecipeManagerException.class,
                () -> { recipeManager.findRecipe(request, true); });
        Assertions.assertEquals("Error", error.getMessage());

        verify(feignController).findRecipe(request, true);
        verify(messageService, times(1)).
                getMessage("find.recipe.error","Error");
    }


    @Test
    public void getRecipeTypesTest() {
        RecipeTypeDto recipeType = RecipeTypeDtoConverter.convert(ERecipeType.SALAD);
        List<RecipeTypeDto> expectedRecipeTypes = new ArrayList<>();
        expectedRecipeTypes.add(recipeType);
        List<String> recipeTypes = new ArrayList<>();
        recipeTypes.add(ERecipeType.SALAD.name());
        when(feignController.getRecipeTypes()).thenReturn(recipeTypes);
        List<RecipeTypeDto> result = recipeManager.getRecipeTypes();
        Assertions.assertEquals(expectedRecipeTypes, result);

        verify(feignController).getRecipeTypes();
    }

    @Test
    public void getRecipeTypesTestFailed() {
        when(feignController.getRecipeTypes()).thenThrow(new RuntimeException("Error"));
        when(messageService.getMessage("get.recipe.collection.error","Error")).thenReturn("Error");

        RecipeManagerException error = Assertions.assertThrows(RecipeManagerException.class,
                () -> { recipeManager.getRecipeTypes(); });
        Assertions.assertEquals("Error", error.getMessage());

        verify(feignController).getRecipeTypes();
        verify(messageService, times(1)).
                getMessage("get.recipe.collection.error","Error");
    }

    @Test
    public void getRecipesByTypeTest() {
        RecipeDtoJson recipe = new RecipeDtoJson();
        List<RecipeDtoJson> recipes = new ArrayList<>();
        recipes.add(recipe);
        when(feignController.getRecipesByType(ERecipeType.SOUP.name())).thenReturn(recipes);
        List<RecipeDtoJsonFull> result = recipeManager.getRecipesByType(ERecipeType.SOUP.name());
        Assertions.assertEquals(recipes.stream().map(RecipeDtoJsonFullConvertor::convert).collect(Collectors.toList()), result);

        verify(feignController).getRecipesByType(ERecipeType.SOUP.name());
    }

    @Test
    public void getRecipesByTypeTestFailed() {
        when(feignController.getRecipesByType(ERecipeType.SOUP.name())).thenThrow(new RuntimeException("Error"));
        when(messageService.getMessage("get.recipes.by.collection.error",ERecipeType.SOUP.name(), "Error")).thenReturn("Error");

        RecipeManagerException error = Assertions.assertThrows(RecipeManagerException.class,
                () -> { recipeManager.getRecipesByType(ERecipeType.SOUP.name()); });
        Assertions.assertEquals("Error", error.getMessage());

        verify(feignController).getRecipesByType(ERecipeType.SOUP.name());
        verify(messageService, times(1)).
                getMessage("get.recipes.by.collection.error", ERecipeType.SOUP.name(), "Error");
    }

    @Test
    public void getRationDescriptionTest() {
        Diet diet = new Diet();
        diet.setDescription("Description");
        when(dietRepository.findByStrategy(ERationStrategy.DIET_SOUP.name())).thenReturn(Optional.of(diet));
        String result = recipeManager.getRationDescription(ERationStrategy.DIET_SOUP.name());
        Assertions.assertEquals(diet.getDescription(), result);

        verify(dietRepository).findByStrategy(ERationStrategy.DIET_SOUP.name());
    }

    @Test
    public void getRationDescriptionTestFailed() {
        when(dietRepository.findByStrategy(ERationStrategy.DIET_SOUP.name())).thenThrow(new MongoException("Error"));
        when(messageService.getMessage("get.diet.description.error",ERationStrategy.DIET_SOUP.name(), "Error")).thenReturn("Error");

        RecipeManagerException error = Assertions.assertThrows(RecipeManagerException.class,
                () -> { recipeManager.getRationDescription(ERationStrategy.DIET_SOUP.name()); });
        Assertions.assertEquals("Error", error.getMessage());

        verify(dietRepository).findByStrategy(ERationStrategy.DIET_SOUP.name());
        verify(messageService, times(1)).
                getMessage("get.diet.description.error", ERationStrategy.DIET_SOUP.name(), "Error");
    }

    @Test
    public void getRationDescriptionTestFailedEmpty() {
        when(dietRepository.findByStrategy(ERationStrategy.DIET_SOUP.name())).thenReturn(Optional.ofNullable(null));
        when(messageService.getMessage("diet.not.found.error",ERationStrategy.DIET_SOUP.name())).thenReturn("Error");
        when(messageService.getMessage("get.diet.description.error",ERationStrategy.DIET_SOUP.name(), "Error")).thenReturn("Error");

        RecipeManagerException error = Assertions.assertThrows(RecipeManagerException.class,
                () -> { recipeManager.getRationDescription(ERationStrategy.DIET_SOUP.name()); });
        Assertions.assertEquals("Error", error.getMessage());

        verify(dietRepository).findByStrategy(ERationStrategy.DIET_SOUP.name());
        verify(messageService, times(1)).getMessage("diet.not.found.error",ERationStrategy.DIET_SOUP.name());

        verify(messageService, times(1)).
                getMessage("get.diet.description.error", ERationStrategy.DIET_SOUP.name(), "Error");

    }
}
