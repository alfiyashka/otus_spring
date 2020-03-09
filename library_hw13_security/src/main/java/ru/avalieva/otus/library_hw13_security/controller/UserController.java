package ru.avalieva.otus.library_hw13_security.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.avalieva.otus.library_hw13_security.domain.User;
import ru.avalieva.otus.library_hw13_security.service.UserService;

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

