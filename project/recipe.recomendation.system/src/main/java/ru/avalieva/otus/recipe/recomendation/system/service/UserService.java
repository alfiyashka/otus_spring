package ru.avalieva.otus.recipe.recomendation.system.service;

import ru.avalieva.otus.recipe.recomendation.system.domain.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getUser(String login);
    void addUser(User user);
}
