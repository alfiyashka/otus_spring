package com.avalieva.otus.project.cookbook.repository.neo.impl;

import com.avalieva.otus.project.cookbook.domain.neo4j.RecipeNeo4j;
import com.avalieva.otus.project.cookbook.repository.neo.RecipeNeo4jCustomRepository;
import com.google.common.collect.Streams;
import cookbook.common.dto.RecipeRequest;
import lombok.AllArgsConstructor;
import org.neo4j.ogm.session.Session;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class RecipeNeo4jCustomRepositoryImpl implements RecipeNeo4jCustomRepository {

    private final Session session;

    private void resetQueryIfNeeded(StringBuilder query) {
        if (query.length() > 0) {
            String changed = query.toString().replaceAll("RETURN", "WITH");
            query.setLength(0);
            query.append(changed);
        }
    }

    private void addCollectionQueryIfNeeded(String collection, Map<String, Object> params, StringBuilder query) {
        if (collection != null && !collection.isEmpty()) {
            resetQueryIfNeeded(query);
            query.append("MATCH (recipe:RecipeNeo4j)-[r1:RECIPE_TYPE]->(recipeType:RecipeTypeNeo4j{name: $recipeTypeName}) RETURN recipe ");
            params.put("recipeTypeName", collection);
        }
    }

    private void addNameQueryIfNeeded(String name, Map<String, Object> params, StringBuilder query) {
        if (name != null && !name.isEmpty()) {
            resetQueryIfNeeded(query);
            query.append("MATCH (recipe:RecipeNeo4j) where toLower(recipe.name) contains toLower($name) RETURN recipe ");
            params.put("name", name);
        }
    }

    private void addIngredientsQueryIfNeeded(List<String> ingredients, Map<String, Object> params, StringBuilder query) {
        if (ingredients != null && !ingredients.isEmpty()) {
            resetQueryIfNeeded(query);
            StringBuilder ingredientsList = new StringBuilder();
            for (int i = 0; i < ingredients.size(); i++) {
                String paramValue = String.format("ingredientName_%s", i);
                if (i > 0 && i < ingredients.size()) {
                    ingredientsList.append(" OR ");
                }
                ingredientsList.append(String.format("ingredient.name = $%s", paramValue));
                params.put(paramValue, ingredients.get(i).trim());
            }
            query.append(
                    String.format("MATCH (recipe:RecipeNeo4j)-[r2:RECIPE2INGREDIENT]->(ingredient:IngredientNeo4j) where %s RETURN recipe ",
                            ingredientsList.toString()));
        }
    }

    private void addAllDataQuery(StringBuilder query) {
        resetQueryIfNeeded(query);
        query.append(" MATCH (nutrient:NutrientNeo4j)<-[r1:RECIPE2NUTRIENT]-(recipe:RecipeNeo4j)-[r:RECIPE2INGREDIENT]->(ingredient:IngredientNeo4j) " +
                " WITH recipe, r, r1, nutrient "  +
                " MATCH (recipe:RecipeNeo4j)-[r2:RECIPE_TYPE]->(recipeType:RecipeTypeNeo4j) RETURN recipe, r, r1, r2, recipeType, nutrient ");


    }

    @Override
    public List<RecipeNeo4j> findRecipes(RecipeRequest recipeRequest) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();

        addCollectionQueryIfNeeded(recipeRequest.getCollection(), params, query);
        addNameQueryIfNeeded(recipeRequest.getName(), params, query);
        addIngredientsQueryIfNeeded(recipeRequest.getIngredients(), params, query);
        addAllDataQuery(query);

        return Streams.stream(session.query(RecipeNeo4j.class, query.toString(), params))
                .collect(Collectors.toList());
    }

    private void addNotHaveIngredients(List<String> ingredients, Map<String, Object> params, StringBuilder query) {
        if (ingredients != null && !ingredients.isEmpty()) {
            resetQueryIfNeeded(query);
            StringBuilder ingredientsList = new StringBuilder();
            for (int i = 0; i < ingredients.size(); i++) {
                String paramValue = String.format("ingredientName_%s", i);
                if (i > 0 && i < ingredients.size()) {
                    ingredientsList.append(" OR ");
                }
                ingredientsList.append(String.format("ingredient.name CONTAINS $%s", paramValue));
                params.put(paramValue, ingredients.get(i).trim());
            }
            query.append(
                    String.format("MATCH (recipe1:RecipeNeo4j)-[r2:RECIPE2INGREDIENT]->(ingredient:IngredientNeo4j) where %s " +
                                    " WITH collect(distinct recipe1) as recipes\n" +
                                    "  MATCH (recipe:RecipeNeo4j)\n" +
                                    "  WHERE NOT recipe IN recipes RETURN recipe ",
                            ingredientsList.toString()));
        }

    }

    @Override
    public List<RecipeNeo4j> findRecipesNotHaveIngredients(RecipeRequest recipeRequest) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder subQuery = new StringBuilder();

        addNotHaveIngredients(recipeRequest.getIngredients(), params, subQuery);
        addCollectionQueryIfNeeded(recipeRequest.getCollection(), params, subQuery);
        addNameQueryIfNeeded(recipeRequest.getName(), params, subQuery);
        addAllDataQuery(subQuery);

        return Streams.stream(session.query(RecipeNeo4j.class, subQuery.toString(), params))
                .collect(Collectors.toList());
    }
}
