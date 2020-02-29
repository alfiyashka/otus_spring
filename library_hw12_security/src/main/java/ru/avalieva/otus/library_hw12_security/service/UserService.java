package ru.avalieva.otus.library_hw12_security.service;

import ru.avalieva.otus.library_hw12_security.domain.User;

public interface UserService {
    User getUser(String login);
    void addUser(User user);
}
