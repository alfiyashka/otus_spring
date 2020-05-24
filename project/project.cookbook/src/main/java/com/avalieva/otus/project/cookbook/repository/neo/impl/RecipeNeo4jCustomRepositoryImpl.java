package com.avalieva.otus.project.cookbook.repository.neo.impl;

import com.avalieva.otus.project.cookbook.domain.neo4j.RecipeNeo4j;
import com.avalieva.otus.project.cookbook.model.RecipeRequest;
import com.avalieva.otus.project.cookbook.repository.neo.RecipeNeo4jCustomRepository;
import com.google.common.collect.Streams;
import lombok.AllArgsConstructor;
import org.neo4j.ogm.session.Session;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@AllArgsConstructor
public class RecipeNeo4jCustomRepositoryImpl implements RecipeNeo4jCustomRepository {

    private final Session session;

    @Override
    public List<RecipeNeo4j> findRecipes(RecipeRequest recipeRequest) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder subQuery = new StringBuilder();
        final String collection = recipeRequest.getCollection();
        if (collection != null && !collection.isEmpty()) {
            subQuery.append("MATCH (recipe:RecipeNeo4j)-[r1:RECIPE_TYPE]->(recipeType:RecipeTypeNeo4j{name: $recipeTypeName}) RETURN recipe ");
            params.put("recipeTypeName", collection);
        }

        final String name = recipeRequest.getName();
        if (name != null && !name.isEmpty()) {
            if (subQuery.length() > 0) {
                String changed = subQuery.toString().replaceAll("RETURN", "WITH");
                subQuery.setLength(0);
                subQuery.append(changed);
            }
            subQuery.append("MATCH (recipe:RecipeNeo4j) where toLower(recipe.name) contains toLower($name) RETURN recipe ");
            params.put("name", name);
        }

        final List<String> ingredients = recipeRequest.getIngredients();
        if (ingredients != null && !ingredients.isEmpty()) {
            if (subQuery.length() > 0) {
                String changed = subQuery.toString().replaceAll("RETURN", "WITH");
                subQuery.setLength(0);
                subQuery.append(changed);
            }
            StringBuilder ingredientsList = new StringBuilder();
            for (int i = 0; i < ingredients.size(); i++) {
                String paramValue = String.format("ingredientName_%s", i);
                if (i > 0 && i < ingredients.size()) {
                    ingredientsList.append(" OR ");
                }
                ingredientsList.append(String.format("ingredient.name = $%s", paramValue));
                params.put(paramValue, ingredients.get(i).trim());
            }
            subQuery.append(
                    String.format("MATCH (recipe:RecipeNeo4j)-[r2:RECIPE2INGREDIENT]->(ingredient:IngredientNeo4j) where %s RETURN recipe ",
                            ingredientsList.toString()));
        }
        if (subQuery.length() > 0) {
            String changed = subQuery.toString().replaceAll("RETURN", "WITH");
            subQuery.setLength(0);
            subQuery.append(changed);
        }
        subQuery.append(" MATCH (nutrient:NutrientNeo4j)<-[r1:RECIPE2NUTRIENT]-(recipe:RecipeNeo4j)-[r:RECIPE2INGREDIENT]->(ingredient:IngredientNeo4j) " +
                           " WITH recipe, r, r1, nutrient "  +
                           " MATCH (recipe:RecipeNeo4j)-[r2:RECIPE_TYPE]->(recipeType:RecipeTypeNeo4j) RETURN recipe, r, r1, r2, recipeType, nutrient ");

        return Streams.stream(session.query(RecipeNeo4j.class, subQuery.toString(), params))
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeNeo4j> findRecipesNotHaveIngredients(RecipeRequest recipeRequest) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder subQuery = new StringBuilder();


        final List<String> ingredients = recipeRequest.getIngredients();
        if (ingredients != null && !ingredients.isEmpty()) {

            StringBuilder ingredientsList = new StringBuilder();
            for (int i = 0; i < ingredients.size(); i++) {
                String paramValue = String.format("ingredientName_%s", i);
                if (i > 0 && i < ingredients.size()) {
                    ingredientsList.append(" OR ");
                }
                ingredientsList.append(String.format("ingredient.name CONTAINS $%s", paramValue));
                params.put(paramValue, ingredients.get(i).trim());
            }
            subQuery.append(
                    String.format("MATCH (recipe1:RecipeNeo4j)-[r2:RECIPE2INGREDIENT]->(ingredient:IngredientNeo4j) where %s " +
                                    " WITH collect(distinct recipe1) as recipes\n" +
                                    "  MATCH (recipe:RecipeNeo4j)\n" +
                                    "  WHERE NOT recipe IN recipes RETURN recipe ",
                            ingredientsList.toString()));
        }

        final String collection = recipeRequest.getCollection();
        if (collection != null && !collection.isEmpty()) {
            if (subQuery.length() > 0) {
                String changed = subQuery.toString().replaceAll("RETURN", "WITH");
                subQuery.setLength(0);
                subQuery.append(changed);
            }
            subQuery.append("MATCH (recipe:RecipeNeo4j)-[r1:RECIPE_TYPE]->(recipeType:RecipeTypeNeo4j{name: $recipeTypeName}) RETURN recipe ");
            params.put("recipeTypeName", collection);
        }

        final String name = recipeRequest.getName();
        if (name != null && !name.isEmpty()) {
            if (subQuery.length() > 0) {
                String changed = subQuery.toString().replaceAll("RETURN", "WITH");
                subQuery.setLength(0);
                subQuery.append(changed);
            }
            subQuery.append("MATCH (recipe:RecipeNeo4j) where toLower(recipe.name) contains toLower($name) RETURN recipe ");
            params.put("name", name);
        }
        if (subQuery.length() > 0) {
            String changed = subQuery.toString().replaceAll("RETURN", "WITH");
            subQuery.setLength(0);
            subQuery.append(changed);
        }
        subQuery.append(" MATCH (nutrient:NutrientNeo4j)<-[r1:RECIPE2NUTRIENT]-(recipe:RecipeNeo4j)-[r:RECIPE2INGREDIENT]->(ingredient:IngredientNeo4j) " +
                " WITH recipe, r, r1, nutrient "  +
                " MATCH (recipe:RecipeNeo4j)-[r2:RECIPE_TYPE]->(recipeType:RecipeTypeNeo4j) RETURN recipe, r, r1, r2, recipeType, nutrient ");


        List<RecipeNeo4j> res = Streams.stream(session.query(RecipeNeo4j.class, subQuery.toString(), params))
                .collect(Collectors.toList());
        return res;
    }
}
