package com.avalieva.otus.project.cookbook.repository.neo;

import com.avalieva.otus.project.cookbook.domain.neo4j.RecipeTypeNeo4j;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface RecipeTypeNeo4jRepository  extends Neo4jRepository<RecipeTypeNeo4j, Long> {

    @Query("MATCH (recipeType:RecipeTypeNeo4j{name: $name}) RETURN recipeType")
    Optional<RecipeTypeNeo4j> findByName(@Param("name") String name);



}
