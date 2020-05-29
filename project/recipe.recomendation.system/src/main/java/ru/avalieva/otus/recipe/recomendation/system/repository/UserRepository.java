package ru.avalieva.otus.recipe.recomendation.system.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.avalieva.otus.recipe.recomendation.system.domain.User;

import java.util.Optional;

@Transactional
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByLogin(String login);
}
