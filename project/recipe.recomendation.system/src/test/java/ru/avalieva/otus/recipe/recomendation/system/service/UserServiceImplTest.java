package ru.avalieva.otus.recipe.recomendation.system.service;


import com.mongodb.MongoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import ru.avalieva.otus.recipe.recomendation.system.domain.User;
import ru.avalieva.otus.recipe.recomendation.system.repository.UserRepository;
import ru.avalieva.otus.recipe.recomendation.system.model.RecipeManagerException;
import ru.avalieva.otus.recipe.recomendation.system.service.impl.UserServiceImpl;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ContextConfiguration(classes = UserServiceImpl.class)
@SpringBootTest
public class UserServiceImplTest {
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MessageService messageService;

    @Autowired
    private UserService userService;


    @Test
    public void getLoginInfoTest() {
        User user = new User(null,  "admin", "password");
        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUser(user.getLogin());
        Assertions.assertEquals(Optional.of(user), result);

        verify(userRepository, times(1)).findByLogin(user.getLogin());
    }

    @Test
    public void getLoginInfoTestFailed() {
        final String USERNAME = "admin";
        MongoException exception = new MongoException("error");
        when(userRepository.findByLogin(USERNAME)).thenThrow(exception);

        MongoException error = Assertions.assertThrows(MongoException.class, () -> { userService.getUser(USERNAME); });
        Assertions.assertEquals(exception.getMessage(), error.getMessage());

        verify(userRepository, times(1)).findByLogin(USERNAME);
    }

    @Test
    public void addLoginTest() {
        User user = new User(null,  "admin", "password");
        userService.addUser(user);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void addLoginTestFailed() {
        User user = new User(null,  "admin", "password");
        MongoException exception = new MongoException("error");
        when(userRepository.save(any(User.class))).thenThrow(exception);
        when(messageService.getMessage("user.add.error",
                user.getLogin(), exception.getMessage())).thenReturn("Cannot add");


        RecipeManagerException error = Assertions.assertThrows(RecipeManagerException.class, () -> { userService.addUser(user); });
        Assertions.assertEquals("Cannot add", error.getMessage());

        verify(messageService, times(1)).getMessage("user.add.error",
                user.getLogin(), exception.getMessage());

    }
}

