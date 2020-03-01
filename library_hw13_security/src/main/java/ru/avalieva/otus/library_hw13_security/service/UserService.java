package ru.avalieva.otus.library_hw13_security.service;

import ru.avalieva.otus.library_hw13_security.domain.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getUser(String login);
    void addUser(User user);
}

