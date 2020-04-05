package ru.otus.avaliva.hw17.library.docker.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.avaliva.hw17.library.docker.domain.User;
import ru.otus.avaliva.hw17.library.docker.service.UserService;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping({"/api/users"})
    public void addNewBook(@RequestBody User user) {
        userService.addUser(user);
    }
}

