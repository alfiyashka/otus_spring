package com.avalieva.otus.project.cookbook.repository;

import com.avalieva.otus.project.cookbook.domain.neo4j.IngredientNeo4j;
import com.avalieva.otus.project.cookbook.domain.neo4j.NutrientNeo4j;
import com.avalieva.otus.project.cookbook.domain.neo4j.RecipeNeo4j;
import com.avalieva.otus.project.cookbook.domain.neo4j.RecipeTypeNeo4j;
import com.avalieva.otus.project.cookbook.model.RecipeRequest;
import com.avalieva.otus.project.cookbook.repository.neo.IngredientNeo4jRepository;
import com.avalieva.otus.project.cookbook.repository.neo.NutrientsNeo4jRepository;
import com.avalieva.otus.project.cookbook.repository.neo.RecipeNeo4jRepository;
import com.avalieva.otus.project.cookbook.repository.neo.RecipeTypeNeo4jRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
public class RecipeNeo4jRepositoryTest {
    @Autowired
    private RecipeNeo4jRepository recipeNeo4jRepository;

    @Autowired
    private RecipeTypeNeo4jRepository recipeTypeNeo4jRepository;

    @Autowired
    private NutrientsNeo4jRepository nutrientsNeo4jRepository;

    @Autowired
    private IngredientNeo4jRepository ingredientRepository;


    @BeforeEach
    public void clearDb() {
        ingredientRepository.deleteAll();
        recipeNeo4jRepository.deleteAll();
        recipeTypeNeo4jRepository.deleteAll();
        nutrientsNeo4jRepository.deleteAll();
    }

    @Test
    public void findByRecipeTypeTest() {
        RecipeNeo4j recipe = new RecipeNeo4j("RecipeName", "MongoId");
        recipe = recipeNeo4jRepository.save(recipe);

        RecipeTypeNeo4j recipeType = new RecipeTypeNeo4j(null, "RecipeType");
        recipeType = recipeTypeNeo4jRepository.save(recipeType);

        recipe.addRecipeType(recipeType);
        recipe = recipeNeo4jRepository.save(recipe);

        NutrientNeo4j nutrientNeo4j = new NutrientNeo4j(null, "NurtientName");
        nutrientNeo4j = nutrientsNeo4jRepository.save(nutrientNeo4j);
        recipe.addNutrient(nutrientNeo4j, 100);
        recipe = recipeNeo4jRepository.save(recipe);

        IngredientNeo4j ingredient = new IngredientNeo4j(null, "IngredientName");
        ingredient = ingredientRepository.save(ingredient);
        recipe.addIngredient(ingredient, 100);
        recipe = recipeNeo4jRepository.save(recipe);

        List<RecipeNeo4j> findRecipes = recipeNeo4jRepository.findByRecipeType(recipeType.getName());
        Assertions.assertFalse(findRecipes.isEmpty());
        Assertions.assertEquals(recipe.getName(), findRecipes.get(0).getName());

        recipeNeo4jRepository.delete(recipe);
        recipeTypeNeo4jRepository.delete(recipeType);
        nutrientsNeo4jRepository.delete(nutrientNeo4j);
        ingredientRepository.delete(ingredient);
    }

    private static RecipeRequest getTotalRequest() {
        List<String> ingredients = new ArrayList<>();
        ingredients.add("IngredientName");
        return new RecipeRequest("RecipeName", "RecipeType", ingredients);
    }

    private static RecipeRequest getOnlyIngredientsRequest() {
        List<String> ingredients = new ArrayList<>();
        ingredients.add("IngredientName");
        return new RecipeRequest(null, null, ingredients);
    }

    private static Stream<Arguments> recipeRequests() {
        return Stream.of(
                Arguments.of(getTotalRequest()),
                Arguments.of(new RecipeRequest("RecipeName", "RecipeType", null)),
                Arguments.of(new RecipeRequest("RecipeName", null, null)),
                Arguments.of(getOnlyIngredientsRequest())
        );
    }

    @ParameterizedTest
    @MethodSource("recipeRequests")
    public void findByTotalRecipeRequestTest(RecipeRequest recipeRequest) {
        RecipeNeo4j recipe = new RecipeNeo4j("RecipeName", "MongoId");
        recipe = recipeNeo4jRepository.save(recipe);

        RecipeTypeNeo4j recipeType = new RecipeTypeNeo4j(null, "RecipeType");
        recipeType = recipeTypeNeo4jRepository.save(recipeType);

        recipe.addRecipeType(recipeType);
        recipe = recipeNeo4jRepository.save(recipe);

        NutrientNeo4j nutrientNeo4j = new NutrientNeo4j(null, "NurtientName");
        nutrientNeo4j = nutrientsNeo4jRepository.save(nutrientNeo4j);
        recipe.addNutrient(nutrientNeo4j, 100);
        recipe = recipeNeo4jRepository.save(recipe);

        IngredientNeo4j ingredient = new IngredientNeo4j(null, "IngredientName");
        ingredient = ingredientRepository.save(ingredient);
        recipe.addIngredient(ingredient, 100);
        recipe = recipeNeo4jRepository.save(recipe);

        List<RecipeNeo4j> findRecipes = recipeNeo4jRepository.findRecipes(recipeRequest);
        Assertions.assertFalse(findRecipes.isEmpty());
        Assertions.assertEquals(recipe.getName(), findRecipes.get(0).getName());

        recipeNeo4jRepository.delete(recipe);
        recipeTypeNeo4jRepository.delete(recipeType);
        nutrientsNeo4jRepository.delete(nutrientNeo4j);
        ingredientRepository.delete(ingredient);
    }

    @ParameterizedTest
    @MethodSource("recipeRequests")
    public void findNoHaveIngredientsRecipeRequestTest(RecipeRequest recipeRequest) {
        RecipeNeo4j recipe = new RecipeNeo4j("RecipeName", "MongoId");
        recipe = recipeNeo4jRepository.save(recipe);

        RecipeTypeNeo4j recipeType = new RecipeTypeNeo4j(null, "RecipeType");
        recipeType = recipeTypeNeo4jRepository.save(recipeType);

        recipe.addRecipeType(recipeType);
        recipe = recipeNeo4jRepository.save(recipe);

        NutrientNeo4j nutrientNeo4j = new NutrientNeo4j(null, "NurtientName");
        nutrientNeo4j = nutrientsNeo4jRepository.save(nutrientNeo4j);
        recipe.addNutrient(nutrientNeo4j, 100);
        recipe = recipeNeo4jRepository.save(recipe);

        IngredientNeo4j ingredient = new IngredientNeo4j(null, "IngredientName");
        ingredient = ingredientRepository.save(ingredient);
        recipe.addIngredient(ingredient, 100);
        recipe = recipeNeo4jRepository.save(recipe);

        List<RecipeNeo4j> findRecipes = recipeNeo4jRepository.findRecipesNotHaveIngredients(recipeRequest);
        if (recipeRequest.getIngredients() == null) {
            Assertions.assertFalse(findRecipes.isEmpty());
            Assertions.assertEquals(recipe.getName(), findRecipes.get(0).getName());
        }
        else {
            Assertions.assertTrue(findRecipes.isEmpty());
        }
        recipeNeo4jRepository.delete(recipe);
        recipeTypeNeo4jRepository.delete(recipeType);
        nutrientsNeo4jRepository.delete(nutrientNeo4j);
        ingredientRepository.delete(ingredient);
    }
}
