package com.avalieva.otus.project.cookbook.repository;

import com.avalieva.otus.project.cookbook.domain.neo4j.RecipeTypeNeo4j;
import com.avalieva.otus.project.cookbook.repository.neo.RecipeTypeNeo4jRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class RecipeTypeNeo4jRepositoryTest {
    @Autowired
    private RecipeTypeNeo4jRepository repository;

    @BeforeEach
    public void clearDb() {
        repository.deleteAll();
    }

    @DisplayName("find by name test")
    @Test
    public void findByNameTest() {
        RecipeTypeNeo4j recipeType = new RecipeTypeNeo4j(null, "RecipeType");
        repository.save(recipeType);

        Optional<RecipeTypeNeo4j> findRecipeType = repository.findByName(recipeType.getName());
        Assertions.assertTrue(findRecipeType.isPresent());
        Assertions.assertEquals(recipeType.getName(), findRecipeType.get().getName());

        repository.delete(findRecipeType.get());
    }
}
