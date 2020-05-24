package ru.avalieva.otus.recipe.recomendation.system.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.avalieva.otus.recipe.recomendation.system.domain.Diet;

import java.util.Optional;


@Transactional
public interface DietRepository extends MongoRepository<Diet, String> {
    Optional<Diet> findByStrategy(String strategy);
}
