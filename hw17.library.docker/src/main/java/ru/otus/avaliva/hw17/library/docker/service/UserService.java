package ru.otus.avaliva.hw17.library.docker.service;


import ru.otus.avaliva.hw17.library.docker.domain.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getUser(String login);
    void addUser(User user);
}