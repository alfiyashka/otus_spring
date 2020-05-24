package com.avalieva.otus.project.cookbook.repository.neo;

import com.avalieva.otus.project.cookbook.domain.neo4j.IngredientNeo4j;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface IngredientNeo4jRepository extends Neo4jRepository<IngredientNeo4j, Long> {

    @Query("MATCH (ingredient:IngredientNeo4j{name: $name}) RETURN ingredient ")
    Optional<IngredientNeo4j> findByName(@Param("name")String name);

}
