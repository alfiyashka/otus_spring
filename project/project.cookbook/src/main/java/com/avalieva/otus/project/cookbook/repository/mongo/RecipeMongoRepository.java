package com.avalieva.otus.project.cookbook.repository.mongo;

import com.avalieva.otus.project.cookbook.domain.mongo.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface RecipeMongoRepository extends MongoRepository<Recipe, String> {

}
