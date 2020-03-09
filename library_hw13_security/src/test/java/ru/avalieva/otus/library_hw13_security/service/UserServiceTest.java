package ru.avalieva.otus.library_hw13_security.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import ru.avalieva.otus.library_hw13_security.domain.User;
import ru.avalieva.otus.library_hw13_security.repository.UserRepository;
import ru.avalieva.otus.library_hw13_security.service.impl.LibraryException;
import ru.avalieva.otus.library_hw13_security.service.impl.UserServiceImpl;

import java.sql.SQLException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ContextConfiguration(classes = UserServiceImpl.class)
@SpringBootTest
public class UserServiceTest {
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MessageService messageService;

    @Autowired
    private UserService userService;


    @Test
    public void getLoginInfoTest() {
        User user = new User(0,  "admin", "password");
        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(user));

        var result = userService.getUser(user.getLogin());
        Assertions.assertEquals(Optional.of(user), result);

        verify(userRepository, times(1)).findByLogin(user.getLogin());
    }

    @Test
    public void getLoginInfoTestFailed() {
        final String USERNAME = "admin";
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("error"));
        when(userRepository.findByLogin(USERNAME)).thenThrow(exception);

        var error = Assertions.assertThrows(BadSqlGrammarException.class, () -> { userService.getUser(USERNAME); });
        Assertions.assertEquals(exception.getMessage(), error.getMessage());

        verify(userRepository, times(1)).findByLogin(USERNAME);
    }

    @Test
    public void addLoginTest() {
        User user = new User(0,  "admin", "password");
        userService.addUser(user);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void addLoginTestFailed() {
        User user = new User(0,  "admin", "password");
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("error"));
        when(userRepository.save(any(User.class))).thenThrow(exception);
        when(messageService.getMessage("user.add.error",
                user.getLogin(), exception.getMessage())).thenReturn("Cannot add");


        var error = Assertions.assertThrows(LibraryException.class, () -> { userService.addUser(user); });
        Assertions.assertEquals("Cannot add", error.getMessage());

        verify(messageService, times(1)).getMessage("user.add.error",
                user.getLogin(), exception.getMessage());

    }
}

