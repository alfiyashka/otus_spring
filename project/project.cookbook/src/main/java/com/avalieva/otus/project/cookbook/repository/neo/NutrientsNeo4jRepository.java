package com.avalieva.otus.project.cookbook.repository.neo;

import com.avalieva.otus.project.cookbook.domain.neo4j.NutrientNeo4j;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface NutrientsNeo4jRepository extends Neo4jRepository<NutrientNeo4j, Long> {

    @Query("MATCH (nutrient:NutrientNeo4j{name: $name}) RETURN nutrient")
    Optional<NutrientNeo4j> findByName(@Param("name") String name);


}
