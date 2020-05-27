package com.avalieva.otus.project.cookbook.repository;

import com.avalieva.otus.project.cookbook.domain.neo4j.IngredientNeo4j;
import com.avalieva.otus.project.cookbook.repository.neo.IngredientNeo4jRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class IngredientNeo4jRepositoryIT {

    @Autowired
    private IngredientNeo4jRepository ingredientRepository;


    @DisplayName("find by name test")
    @Test
    public void findByNameTest() {
        IngredientNeo4j ingredient = new IngredientNeo4j(null, "IngredientName");
        ingredientRepository.save(ingredient);

        Optional<IngredientNeo4j> findIngredient = ingredientRepository.findByName(ingredient.getName());
        Assertions.assertTrue(findIngredient.isPresent());
        Assertions.assertEquals(ingredient.getName(), findIngredient.get().getName());

        ingredientRepository.delete(findIngredient.get());
    }
}

