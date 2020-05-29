package com.avalieva.otus.project.cookbook.service.impl;

import com.avalieva.otus.project.cookbook.domain.mongo.Recipe;
import com.avalieva.otus.project.cookbook.domain.neo4j.IngredientNeo4j;
import com.avalieva.otus.project.cookbook.domain.neo4j.NutrientNeo4j;
import com.avalieva.otus.project.cookbook.domain.neo4j.RecipeNeo4j;
import com.avalieva.otus.project.cookbook.domain.neo4j.RecipeTypeNeo4j;
import com.avalieva.otus.project.cookbook.dto.RecipeConverter;
import com.avalieva.otus.project.cookbook.model.CookbookException;
import com.avalieva.otus.project.cookbook.dto.RecipeDtoJson;
import com.avalieva.otus.project.cookbook.repository.mongo.RecipeMongoRepository;
import com.avalieva.otus.project.cookbook.repository.neo.IngredientNeo4jRepository;
import com.avalieva.otus.project.cookbook.repository.neo.NutrientsNeo4jRepository;
import com.avalieva.otus.project.cookbook.repository.neo.RecipeNeo4jRepository;
import com.avalieva.otus.project.cookbook.repository.neo.RecipeTypeNeo4jRepository;
import com.avalieva.otus.project.cookbook.service.CookbookService;
import com.avalieva.otus.project.cookbook.service.MessageService;
import com.google.common.collect.Streams;
import cookbook.common.dto.IngredientDto;
import cookbook.common.dto.NutrientDto;
import cookbook.common.dto.RecipeRequest;
import cookbook.common.dto.RecipeTypeDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CookbookServiceImpl implements CookbookService {

    private final RecipeMongoRepository recipeMongoRepository;

    private final IngredientNeo4jRepository ingredientNeo4jRepository;
    private final RecipeNeo4jRepository recipeNeo4jRepository;
    private final NutrientsNeo4jRepository nutrientsNeo4jRepository;
    private final RecipeTypeNeo4jRepository recipeTypeNeo4jRepository;

    private final MessageService messageService;

    private void addIngredient(RecipeNeo4j recipeNeo4j, IngredientNeo4j ingredientNeo4j, float weight) {
        recipeNeo4j.addIngredient(ingredientNeo4j, weight);

        recipeNeo4jRepository.save(recipeNeo4j);
        ingredientNeo4jRepository.save(ingredientNeo4j);
    }

    private void addNutrient(RecipeNeo4j recipeNeo4j, NutrientNeo4j nutrientNeo4j, float weight) {
        recipeNeo4j.addNutrient(nutrientNeo4j, weight);

        recipeNeo4jRepository.save(recipeNeo4j);
        nutrientsNeo4jRepository.save(nutrientNeo4j);
    }

    private void addRecipeType(RecipeNeo4j recipeNeo4j, RecipeTypeNeo4j recipeTypeNeo4j) {
        recipeNeo4j.addRecipeType(recipeTypeNeo4j);

        recipeNeo4jRepository.save(recipeNeo4j);
        recipeTypeNeo4jRepository.save(recipeTypeNeo4j);
    }

    private RecipeNeo4j createRecipe(String name, String mongoId) {
        return recipeNeo4jRepository.save(new RecipeNeo4j(name, mongoId));
    }

    private IngredientNeo4j createIngredient(String name) {
        return ingredientNeo4jRepository.save(new IngredientNeo4j(name));
    }

    private NutrientNeo4j createNutrient(String name) {
        return nutrientsNeo4jRepository.save(new NutrientNeo4j(name));
    }

    private RecipeTypeNeo4j createRecipeType(RecipeTypeDto recipeTypeDto) {
        return recipeTypeNeo4jRepository.save(new RecipeTypeNeo4j(recipeTypeDto.getRecipeType()));
    }

    @Override
    public void addRecipe(Recipe recipe,
                          List<IngredientDto> ingredientDtos,
                          List<NutrientDto> nutrientDtos,
                          RecipeTypeDto recipeTypeDto) {
        try {
            recipeMongoRepository.save(recipe);
            RecipeNeo4j recipeNeo4j = createRecipe(recipe.getName(), recipe.getId());

            for (IngredientDto ingredientDto : ingredientDtos) {
                IngredientNeo4j ingredientNeo4j = ingredientNeo4jRepository.findByName(ingredientDto.getIngredientName())
                        .orElseGet(() -> createIngredient(ingredientDto.getIngredientName()));
                addIngredient(recipeNeo4j, ingredientNeo4j, ingredientDto.getWeight());
            }

            if (nutrientDtos != null) {
                for (NutrientDto nutrientDto : nutrientDtos) {
                    NutrientNeo4j nutrientNeo4j = nutrientsNeo4jRepository.findByName(nutrientDto.getNutrient().name())
                            .orElseGet(() -> createNutrient(nutrientDto.getNutrient().name()));
                    addNutrient(recipeNeo4j, nutrientNeo4j, nutrientDto.getWeight());
                }
            }
            RecipeTypeNeo4j recipeType = recipeTypeNeo4jRepository.findByName(recipeTypeDto.getRecipeType())
                    .orElseGet(() -> createRecipeType(recipeTypeDto));
            addRecipeType(recipeNeo4j, recipeType);
        }
        catch (Exception e) {
            throw new CookbookException(messageService.getMessage("add.recipe.error", e.getMessage()));
        }
    }

    @Override
    public void addRecipe(RecipeDtoJson recipeDto) {
        addRecipe(recipeDto.getRecipe(), recipeDto.getIngredients(), recipeDto.getNutrients(), recipeDto.getRecipeType());
    }

    @Override
    public void clearNeo4jData() {
        try {
            ingredientNeo4jRepository.deleteAll();
            recipeNeo4jRepository.deleteAll();
            nutrientsNeo4jRepository.deleteAll();
            recipeTypeNeo4jRepository.deleteAll();
        }
        catch (Exception e) {
            throw new CookbookException(messageService.getMessage("clear.database.error", e.getMessage()));
        }
    }

    @Override
    public List<String> getIngredients() {
        try {
            return Streams.stream(ingredientNeo4jRepository.findAll())
                    .map(IngredientNeo4j::getName)
                    .collect(Collectors.toList());
        }
        catch (Exception e) {
            throw new CookbookException(messageService.getMessage("get.ingredients.error", e.getMessage()));
        }
    }

    @Override
    public List<String> getRecipeTypes() {
        try {
            return Streams.stream(recipeTypeNeo4jRepository.findAll())
                    .map(RecipeTypeNeo4j::getName)
                    .collect(Collectors.toList());
        }
        catch (Exception e) {
            throw new CookbookException(messageService.getMessage("get.recipe.types.error", e.getMessage()));
        }
    }

    @Override
    public List<RecipeDtoJson> getRecipesByType(String recipeType) {
        try {
            return recipeNeo4jRepository.findByRecipeType(recipeType)
                    .stream()
                    .map(it -> {
                                return RecipeConverter.convert(
                                        recipeMongoRepository.findById(it.getMongoId())
                                                .orElseThrow(() -> new RuntimeException(messageService.getMessage("get.mongo.recipe.error", it.getMongoId()))),
                                        recipeType, it.getIngredients(), it.getNutrients());
                            }
                    )
                    .collect(Collectors.toList());
        }
        catch (Exception e) {
            throw new CookbookException(messageService.getMessage("get.recipes.by.types.error", recipeType, e.getMessage()));
        }
    }

    @Override
    public List<RecipeDtoJson> findRecipes(RecipeRequest recipeRequest) {
        try {
            return recipeNeo4jRepository.findRecipes(recipeRequest)
                    .stream()
                    .map(it -> {
                                return RecipeConverter.convert(
                                        recipeMongoRepository.findById(it.getMongoId()).orElseThrow(
                                                () -> new RuntimeException(messageService.getMessage("get.mongo.recipe.error", it.getMongoId()))),
                                        it.getRecipeType(), it.getIngredients(), it.getNutrients());
                            }
                    )
                    .collect(Collectors.toList());
        }
        catch (Exception e) {
            throw new CookbookException(messageService.getMessage("find.recipes.error", e.getMessage()));
        }
    }

    @Override
    public List<RecipeDtoJson> findRecipesNotHaveIngredients(RecipeRequest recipeRequest) {
        try {
            return recipeNeo4jRepository.findRecipesNotHaveIngredients(recipeRequest)
                    .stream()
                    .map(it -> {
                                return RecipeConverter.convert(
                                        recipeMongoRepository.findById(it.getMongoId()).orElseThrow(
                                                () -> new RuntimeException(messageService.getMessage("get.mongo.recipe.error", it.getMongoId()))),
                                        it.getRecipeType(), it.getIngredients(), it.getNutrients());
                            }
                    )
                    .collect(Collectors.toList());
        }
        catch (Exception e) {
            throw new CookbookException(messageService.getMessage("find.recipes.error", e.getMessage()));
        }
    }
}
