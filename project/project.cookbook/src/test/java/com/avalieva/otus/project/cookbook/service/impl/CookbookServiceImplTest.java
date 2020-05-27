package com.avalieva.otus.project.cookbook.service.impl;

import com.avalieva.otus.project.cookbook.domain.mongo.Recipe;
import com.avalieva.otus.project.cookbook.domain.neo4j.IngredientNeo4j;
import com.avalieva.otus.project.cookbook.domain.neo4j.NutrientNeo4j;
import com.avalieva.otus.project.cookbook.domain.neo4j.RecipeNeo4j;
import com.avalieva.otus.project.cookbook.domain.neo4j.RecipeTypeNeo4j;
import com.avalieva.otus.project.cookbook.dto.RecipeDtoJson;
import com.avalieva.otus.project.cookbook.model.CookbookException;
import com.avalieva.otus.project.cookbook.repository.mongo.RecipeMongoRepository;
import com.avalieva.otus.project.cookbook.repository.neo.IngredientNeo4jRepository;
import com.avalieva.otus.project.cookbook.repository.neo.NutrientsNeo4jRepository;
import com.avalieva.otus.project.cookbook.repository.neo.RecipeNeo4jRepository;
import com.avalieva.otus.project.cookbook.repository.neo.RecipeTypeNeo4jRepository;
import com.avalieva.otus.project.cookbook.service.CookbookService;
import com.avalieva.otus.project.cookbook.service.MessageService;
import com.mongodb.MongoException;
import cookbook.common.dto.*;
import cookbook.common.model.ENutrient;
import cookbook.common.model.ERecipeType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;


@ContextConfiguration(classes = CookbookServiceImpl.class)
@SpringBootTest
public class CookbookServiceImplTest {

    @MockBean
    public RecipeMongoRepository recipeMongoRepository;

    @MockBean
    public IngredientNeo4jRepository ingredientNeo4jRepository;

    @MockBean
    public RecipeNeo4jRepository recipeNeo4jRepository;

    @MockBean
    public NutrientsNeo4jRepository nutrientsNeo4jRepository;

    @MockBean
    public RecipeTypeNeo4jRepository recipeTypeNeo4jRepository;

    @MockBean
    private MessageService messageService;

    @Autowired
    private CookbookService cookbookService;

    @Test
    public void addRecipeTest() {
        Recipe recipe = new Recipe(null, "Salad Name", 110, "description");
        List<IngredientDto> ingredientDtos = new ArrayList<>();
        ingredientDtos.add(new IngredientDto("Apple", 100));
        List<NutrientDto> nutrientDtos = new ArrayList<>();
        nutrientDtos.add(new NutrientDto(ENutrient.PROTEIN, 12));
        RecipeTypeDto recipeTypeDto = RecipeTypeDtoConverter.convert(ERecipeType.SALAD);

        when(recipeMongoRepository.save(recipe)).thenReturn(recipe);
        when(recipeNeo4jRepository.save(any())).thenReturn(new RecipeNeo4j("RecipeName", "MongoId"));
        IngredientNeo4j ingredient = new IngredientNeo4j(1L, "Apple");
        when(ingredientNeo4jRepository.findByName(any()))
                .thenReturn(Optional.of(ingredient));
        when(ingredientNeo4jRepository.save(any())).thenReturn(ingredient);

        NutrientNeo4j nutrient = new NutrientNeo4j(1L, ENutrient.FAT.name());
        when(nutrientsNeo4jRepository.findByName(any()))
                .thenReturn(Optional.of(nutrient));
        when(nutrientsNeo4jRepository.save(any())).thenReturn(nutrient);

        RecipeTypeNeo4j recipeType = new RecipeTypeNeo4j(1L, ERecipeType.SALAD.name());
        when(recipeTypeNeo4jRepository.findByName(any())).thenReturn(Optional.of(recipeType));
        when(recipeTypeNeo4jRepository.save(any())).thenReturn(recipeType);

        cookbookService.addRecipe(recipe, ingredientDtos, nutrientDtos, recipeTypeDto);

        verify(recipeMongoRepository).save(recipe);
        verify(recipeNeo4jRepository, times(4)).save(any());
        verify(ingredientNeo4jRepository).findByName(any());
        verify(ingredientNeo4jRepository).save(any());
        verify(nutrientsNeo4jRepository).findByName(any());
        verify(nutrientsNeo4jRepository).save(any());
        verify(recipeTypeNeo4jRepository).findByName(any());
        verify(recipeTypeNeo4jRepository).save(any());
    }

    @Test
    public void addRecipeTestFailed() {
        Recipe recipe = new Recipe(null, "Salad Name", 110, "description");
        List<IngredientDto> ingredientDtos = new ArrayList<>();
        ingredientDtos.add(new IngredientDto("Apple", 100));
        List<NutrientDto> nutrientDtos = new ArrayList<>();
        nutrientDtos.add(new NutrientDto(ENutrient.PROTEIN, 12));
        RecipeTypeDto recipeTypeDto = RecipeTypeDtoConverter.convert(ERecipeType.SALAD);

        when(recipeMongoRepository.save(recipe)).thenThrow(new MongoException("Error"));
        when(messageService.getMessage("add.recipe.error", "Error")).thenReturn("Error");

        CookbookException exception = Assertions.assertThrows(CookbookException.class, () -> {
            cookbookService.addRecipe(recipe, ingredientDtos, nutrientDtos, recipeTypeDto);
                });
        Assertions.assertEquals("Error", exception.getMessage());

        verify(messageService).getMessage("add.recipe.error", "Error");
        verify(recipeMongoRepository).save(recipe);
        verify(recipeNeo4jRepository, never()).save(any());
        verify(ingredientNeo4jRepository, never()).findByName(any());
        verify(ingredientNeo4jRepository, never()).save(any());
        verify(nutrientsNeo4jRepository, never()).findByName(any());
        verify(nutrientsNeo4jRepository, never()).save(any());
        verify(recipeTypeNeo4jRepository, never()).findByName(any());
        verify(recipeTypeNeo4jRepository, never()).save(any());
    }

    @Test
    public void clearDatabaseTest() {
        doNothing().when(ingredientNeo4jRepository).deleteAll();
        doNothing().when(recipeNeo4jRepository).deleteAll();
        doNothing().when(nutrientsNeo4jRepository).deleteAll();

        cookbookService.clearNeo4jData();

        verify(ingredientNeo4jRepository).deleteAll();
        verify(recipeNeo4jRepository).deleteAll();
        verify(nutrientsNeo4jRepository).deleteAll();
    }

    @Test
    public void clearDatabaseTestFailed() {
        doThrow(new MongoException("Error")).when(ingredientNeo4jRepository).deleteAll();
        when(messageService.getMessage("clear.database.error", "Error")).thenReturn("Error");

        CookbookException exception = Assertions.assertThrows(CookbookException.class, () -> {
            cookbookService.clearNeo4jData();  });
        Assertions.assertEquals("Error", exception.getMessage());

        verify(messageService).getMessage("clear.database.error", "Error");
        verify(ingredientNeo4jRepository).deleteAll();
        verify(recipeNeo4jRepository, never()).deleteAll();
        verify(nutrientsNeo4jRepository, never()).deleteAll();
    }

    @Test
    public void getIngredientsTest() {
        IngredientNeo4j ingredient = new IngredientNeo4j(1L, "Apple");
        List<IngredientNeo4j> ingredientNeo4js = new ArrayList<>();
        ingredientNeo4js.add(ingredient);

        when(ingredientNeo4jRepository.findAll()).thenReturn(ingredientNeo4js);

        List<String> result = cookbookService.getIngredients();
        Assertions.assertEquals(ingredientNeo4js.get(0).getName(), result.get(0));

        verify(ingredientNeo4jRepository).findAll();
    }

    @Test
    public void getIngredientsTestFailed() {
        doThrow(new MongoException("Error")).when(ingredientNeo4jRepository).findAll();
        when(messageService.getMessage("get.ingredients.error", "Error")).thenReturn("Error");

        CookbookException exception = Assertions.assertThrows(CookbookException.class, () -> {
            cookbookService.getIngredients();
        });
        Assertions.assertEquals("Error", exception.getMessage());

        verify(messageService).getMessage("get.ingredients.error", "Error");
        verify(ingredientNeo4jRepository).findAll();
    }

    @Test
    public void getRecipeTypesTest() {
        RecipeTypeNeo4j recipeType = new RecipeTypeNeo4j(1L, ERecipeType.SALAD.name());
        List<RecipeTypeNeo4j> recipeTypes = new ArrayList<>();
        recipeTypes.add(recipeType);

        when(recipeTypeNeo4jRepository.findAll()).thenReturn(recipeTypes);

        List<String> result = cookbookService.getRecipeTypes();
        Assertions.assertEquals(recipeTypes.get(0).getName(), result.get(0));

        verify(recipeTypeNeo4jRepository).findAll();
    }

    @Test
    public void getRecipeTypesTestFailed() {
        doThrow(new MongoException("Error")).when(recipeTypeNeo4jRepository).findAll();
        when(messageService.getMessage("get.recipe.types.error", "Error")).thenReturn("Error");

        CookbookException exception = Assertions.assertThrows(CookbookException.class, () -> {
            cookbookService.getRecipeTypes();
        });
        Assertions.assertEquals("Error", exception.getMessage());

        verify(messageService).getMessage("get.recipe.types.error", "Error");
        verify(recipeTypeNeo4jRepository).findAll();
    }

    @Test
    public void getRecipesByTypeTest() {
        RecipeNeo4j recipe = new RecipeNeo4j("Recipe", "MongoId");
        recipe.addIngredient(new IngredientNeo4j(1L, "Apple"), 1.9F);
        recipe.addRecipeType(new RecipeTypeNeo4j(1L, ERecipeType.SALAD.name()));
        recipe.addNutrient(new NutrientNeo4j(1L, ENutrient.FAT.toString()), 1.6F);
        List<RecipeNeo4j> recipes = new ArrayList<>();
        recipes.add(recipe);

        when(recipeNeo4jRepository.findByRecipeType(ERecipeType.SALAD.name())).thenReturn(recipes);
        when(recipeMongoRepository.findById(recipe.getMongoId())).thenReturn(Optional.of(new Recipe()));

        List<RecipeDtoJson> result = cookbookService.getRecipesByType(ERecipeType.SALAD.name());
        Assertions.assertEquals(recipes.size(), result.size());

        verify(recipeNeo4jRepository).findByRecipeType(ERecipeType.SALAD.name());
        verify(recipeMongoRepository).findById(recipe.getMongoId());

    }

    @Test
    public void getRecipesByTypeTestFailed() {
        RecipeNeo4j recipe = new RecipeNeo4j("Recipe", "MongoId");
        recipe.addIngredient(new IngredientNeo4j(1L, "Apple"), 1.9F);
        recipe.addRecipeType(new RecipeTypeNeo4j(1L, ERecipeType.SALAD.name()));
        recipe.addNutrient(new NutrientNeo4j(1L, ENutrient.FAT.toString()), 1.6F);
        List<RecipeNeo4j> recipes = new ArrayList<>();
        recipes.add(recipe);

        when(recipeNeo4jRepository.findByRecipeType(ERecipeType.SALAD.name())).thenThrow(new RuntimeException("Error"));
        when(messageService.getMessage("get.recipes.by.types.error", ERecipeType.SALAD.name(), "Error")).thenReturn("Error");

        CookbookException exception = Assertions.assertThrows(CookbookException.class, () -> {
            cookbookService.getRecipesByType(ERecipeType.SALAD.name());
        });
        Assertions.assertEquals("Error", exception.getMessage());

        verify(recipeMongoRepository, never()).findById(any());
        verify(recipeNeo4jRepository).findByRecipeType(ERecipeType.SALAD.name());
        verify(messageService).getMessage("get.recipes.by.types.error", ERecipeType.SALAD.name(), "Error");
    }

    @Test
    public void getRecipesByTypeTestFailedEmptyMongoRecipe() {
        RecipeNeo4j recipe = new RecipeNeo4j("Recipe", "MongoId");
        recipe.addIngredient(new IngredientNeo4j(1L, "Apple"), 1.9F);
        recipe.addRecipeType(new RecipeTypeNeo4j(1L, ERecipeType.SALAD.name()));
        recipe.addNutrient(new NutrientNeo4j(1L, ENutrient.FAT.toString()), 1.6F);
        List<RecipeNeo4j> recipes = new ArrayList<>();
        recipes.add(recipe);

        when(recipeNeo4jRepository.findByRecipeType(ERecipeType.SALAD.name())).thenReturn(recipes);
        when(recipeMongoRepository.findById(recipe.getMongoId())).thenReturn(Optional.ofNullable(null));

        when(messageService.getMessage("get.mongo.recipe.error", recipe.getMongoId())).thenReturn("Error");
        when(messageService.getMessage("get.recipes.by.types.error", ERecipeType.SALAD.name(), "Error")).thenReturn("Error");

        CookbookException exception = Assertions.assertThrows(CookbookException.class, () -> {
            cookbookService.getRecipesByType(ERecipeType.SALAD.name());
        });
        Assertions.assertEquals("Error", exception.getMessage());

        verify(recipeNeo4jRepository).findByRecipeType(ERecipeType.SALAD.name());
        verify(recipeMongoRepository).findById(recipe.getMongoId());

        verify(messageService).getMessage("get.mongo.recipe.error", recipe.getMongoId());
        verify(messageService).getMessage("get.recipes.by.types.error", ERecipeType.SALAD.name(), "Error");
    }

    @Test
    public void findRecipesTest() {
        RecipeNeo4j recipe = new RecipeNeo4j("Recipe", "MongoId");
        IngredientNeo4j ingredient = new IngredientNeo4j(1L, "Apple");
        recipe.addIngredient(ingredient, 1.9F);
        recipe.addRecipeType(new RecipeTypeNeo4j(1L, ERecipeType.SALAD.name()));
        recipe.addNutrient(new NutrientNeo4j(1L, ENutrient.FAT.toString()), 1.6F);
        List<RecipeNeo4j> recipes = new ArrayList<>();
        recipes.add(recipe);
        List<String> ingredients = new ArrayList<>();
        ingredients.add(ingredient.getName());
        RecipeRequest recipeRequest = new RecipeRequest("Recipe Name", ERecipeType.SALAD.name(), ingredients);

        when(recipeNeo4jRepository.findRecipes(recipeRequest)).thenReturn(recipes);
        when(recipeMongoRepository.findById(recipe.getMongoId())).thenReturn(Optional.of(new Recipe()));

        List<RecipeDtoJson> result = cookbookService.findRecipes(recipeRequest);
        Assertions.assertEquals(recipes.size(), result.size());

        verify(recipeNeo4jRepository).findRecipes(recipeRequest);
        verify(recipeMongoRepository).findById(recipe.getMongoId());
    }

    @Test
    public void findRecipesTestFailed() {
        RecipeNeo4j recipe = new RecipeNeo4j("Recipe", "MongoId");
        IngredientNeo4j ingredient = new IngredientNeo4j(1L, "Apple");
        recipe.addIngredient(ingredient, 1.9F);
        recipe.addRecipeType(new RecipeTypeNeo4j(1L, ERecipeType.SALAD.name()));
        recipe.addNutrient(new NutrientNeo4j(1L, ENutrient.FAT.toString()), 1.6F);
        List<RecipeNeo4j> recipes = new ArrayList<>();
        recipes.add(recipe);
        List<String> ingredients = new ArrayList<>();
        ingredients.add(ingredient.getName());
        RecipeRequest recipeRequest = new RecipeRequest("Recipe Name", ERecipeType.SALAD.name(), ingredients);

        when(recipeNeo4jRepository.findRecipes(recipeRequest)).thenThrow(new RuntimeException("Error"));
        when(messageService.getMessage("find.recipes.error", "Error")).thenReturn("Error");

        CookbookException exception = Assertions.assertThrows(CookbookException.class, () -> {
            cookbookService.findRecipes(recipeRequest);
        });
        Assertions.assertEquals("Error", exception.getMessage());

        verify(recipeNeo4jRepository).findRecipes(recipeRequest);
        verify(recipeMongoRepository, never()).findById(any());
        verify(messageService).getMessage("find.recipes.error", "Error");
    }

    @Test
    public void findRecipesTestFailedEmptyMongoRecipe() {
        RecipeNeo4j recipe = new RecipeNeo4j("Recipe", "MongoId");
        IngredientNeo4j ingredient = new IngredientNeo4j(1L, "Apple");
        recipe.addIngredient(ingredient, 1.9F);
        recipe.addRecipeType(new RecipeTypeNeo4j(1L, ERecipeType.SALAD.name()));
        recipe.addNutrient(new NutrientNeo4j(1L, ENutrient.FAT.toString()), 1.6F);
        List<RecipeNeo4j> recipes = new ArrayList<>();
        recipes.add(recipe);
        List<String> ingredients = new ArrayList<>();
        ingredients.add(ingredient.getName());
        RecipeRequest recipeRequest = new RecipeRequest("Recipe Name", ERecipeType.SALAD.name(), ingredients);

        when(recipeNeo4jRepository.findRecipes(recipeRequest)).thenReturn(recipes);
        when(recipeMongoRepository.findById(recipe.getMongoId())).thenReturn(Optional.ofNullable(null));

        when(messageService.getMessage("get.mongo.recipe.error", recipe.getMongoId())).thenReturn("Error");
        when(messageService.getMessage("find.recipes.error", "Error")).thenReturn("Error");

        CookbookException exception = Assertions.assertThrows(CookbookException.class, () -> {
            cookbookService.findRecipes(recipeRequest);
        });
        Assertions.assertEquals("Error", exception.getMessage());

        verify(recipeNeo4jRepository).findRecipes(recipeRequest);
        verify(recipeMongoRepository).findById(recipe.getMongoId());

        verify(messageService).getMessage("get.mongo.recipe.error", recipe.getMongoId());
        verify(messageService).getMessage("find.recipes.error", "Error");
    }

    @Test
    public void findRecipesNotHaveIngredientsTest() {
        RecipeNeo4j recipe = new RecipeNeo4j("Recipe", "MongoId");
        IngredientNeo4j ingredient = new IngredientNeo4j(1L, "Apple");
        recipe.addIngredient(ingredient, 1.9F);
        recipe.addRecipeType(new RecipeTypeNeo4j(1L, ERecipeType.SALAD.name()));
        recipe.addNutrient(new NutrientNeo4j(1L, ENutrient.FAT.toString()), 1.6F);
        List<RecipeNeo4j> recipes = new ArrayList<>();
        recipes.add(recipe);
        List<String> ingredients = new ArrayList<>();
        ingredients.add("Lemon");
        RecipeRequest recipeRequest = new RecipeRequest("Recipe Name", ERecipeType.SALAD.name(), ingredients);

        when(recipeNeo4jRepository.findRecipesNotHaveIngredients(recipeRequest)).thenReturn(recipes);
        when(recipeMongoRepository.findById(recipe.getMongoId())).thenReturn(Optional.of(new Recipe()));

        List<RecipeDtoJson> result = cookbookService.findRecipesNotHaveIngredients(recipeRequest);
        Assertions.assertEquals(recipes.size(), result.size());

        verify(recipeNeo4jRepository).findRecipesNotHaveIngredients(recipeRequest);
        verify(recipeMongoRepository).findById(recipe.getMongoId());
    }

    @Test
    public void findRecipesNotHaveIngredientsTestFailed() {
        RecipeNeo4j recipe = new RecipeNeo4j("Recipe", "MongoId");
        IngredientNeo4j ingredient = new IngredientNeo4j(1L, "Apple");
        recipe.addIngredient(ingredient, 1.9F);
        recipe.addRecipeType(new RecipeTypeNeo4j(1L, ERecipeType.SALAD.name()));
        recipe.addNutrient(new NutrientNeo4j(1L, ENutrient.FAT.toString()), 1.6F);
        List<RecipeNeo4j> recipes = new ArrayList<>();
        recipes.add(recipe);
        List<String> ingredients = new ArrayList<>();
        ingredients.add("Lemon");
        RecipeRequest recipeRequest = new RecipeRequest("Recipe Name", ERecipeType.SALAD.name(), ingredients);

        when(recipeNeo4jRepository.findRecipesNotHaveIngredients(recipeRequest)).thenThrow(new RuntimeException("Error"));
        when(messageService.getMessage("find.recipes.error", "Error")).thenReturn("Error");

        CookbookException exception = Assertions.assertThrows(CookbookException.class, () -> {
            cookbookService.findRecipesNotHaveIngredients(recipeRequest);
        });
        Assertions.assertEquals("Error", exception.getMessage());

        verify(recipeNeo4jRepository).findRecipesNotHaveIngredients(recipeRequest);
        verify(recipeMongoRepository, never()).findById(any());
        verify(messageService).getMessage("find.recipes.error", "Error");
    }

    @Test
    public void findRecipesNotHaveIngredientsTestFailedEmptyMongoRecipe() {
        RecipeNeo4j recipe = new RecipeNeo4j("Recipe", "MongoId");
        IngredientNeo4j ingredient = new IngredientNeo4j(1L, "Apple");
        recipe.addIngredient(ingredient, 1.9F);
        recipe.addRecipeType(new RecipeTypeNeo4j(1L, ERecipeType.SALAD.name()));
        recipe.addNutrient(new NutrientNeo4j(1L, ENutrient.FAT.toString()), 1.6F);
        List<RecipeNeo4j> recipes = new ArrayList<>();
        recipes.add(recipe);
        List<String> ingredients = new ArrayList<>();
        ingredients.add("Lemon");
        RecipeRequest recipeRequest = new RecipeRequest("Recipe Name", ERecipeType.SALAD.name(), ingredients);

        when(recipeNeo4jRepository.findRecipesNotHaveIngredients(recipeRequest)).thenReturn(recipes);
        when(recipeMongoRepository.findById(recipe.getMongoId())).thenReturn(Optional.ofNullable(null));

        when(messageService.getMessage("get.mongo.recipe.error", recipe.getMongoId())).thenReturn("Error");
        when(messageService.getMessage("find.recipes.error", "Error")).thenReturn("Error");

        CookbookException exception = Assertions.assertThrows(CookbookException.class, () -> {
            cookbookService.findRecipesNotHaveIngredients(recipeRequest);
        });
        Assertions.assertEquals("Error", exception.getMessage());

        verify(recipeNeo4jRepository).findRecipesNotHaveIngredients(recipeRequest);
        verify(recipeMongoRepository).findById(recipe.getMongoId());

        verify(messageService).getMessage("get.mongo.recipe.error", recipe.getMongoId());
        verify(messageService).getMessage("find.recipes.error", "Error");
    }

}
