package com.avalieva.otus.project.cookbook.repository;

import com.avalieva.otus.project.cookbook.domain.neo4j.NutrientNeo4j;
import com.avalieva.otus.project.cookbook.repository.neo.NutrientsNeo4jRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class NutrientsNeo4jRepositoryIT {
    @Autowired
    private NutrientsNeo4jRepository repository;

    @BeforeEach
    public void clearDb() {
        repository.deleteAll();
    }

    @DisplayName("find by name test")
    @Test
    public void findByNameTest() {
        NutrientNeo4j nutrientNeo4j = new NutrientNeo4j(null, "NurtientName");
        repository.save(nutrientNeo4j);

        Optional<NutrientNeo4j> findNutrient = repository.findByName(nutrientNeo4j.getName());
        Assertions.assertTrue(findNutrient.isPresent());
        Assertions.assertEquals(nutrientNeo4j.getName(), findNutrient.get().getName());

        repository.delete(findNutrient.get());
    }
}
