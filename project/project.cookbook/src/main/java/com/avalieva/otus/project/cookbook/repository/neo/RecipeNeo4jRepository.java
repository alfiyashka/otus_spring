package com.avalieva.otus.project.cookbook.repository.neo;

import com.avalieva.otus.project.cookbook.domain.neo4j.RecipeNeo4j;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface RecipeNeo4jRepository extends Neo4jRepository<RecipeNeo4j, Long>, RecipeNeo4jCustomRepository {
    @Query("MATCH (nutrient:NutrientNeo4j)<-[r1:RECIPE2NUTRIENT]-(recipe:RecipeNeo4j)-[r:RECIPE2INGREDIENT]->(ingredient:IngredientNeo4j) " +
            " WITH recipe, r, r1, nutrient " +
            " MATCH (recipe:RecipeNeo4j)-[r2:RECIPE_TYPE]->(recipeType:RecipeTypeNeo4j{name: $name}) RETURN recipe, r, r1, r2, recipeType, nutrient ")
    List<RecipeNeo4j> findByRecipeType(@Param("name") String name);

}
