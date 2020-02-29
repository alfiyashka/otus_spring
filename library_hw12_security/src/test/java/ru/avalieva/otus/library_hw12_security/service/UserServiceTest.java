package ru.avalieva.otus.library_hw12_security.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import ru.avalieva.otus.library_hw12_security.domain.User;
import ru.avalieva.otus.library_hw12_security.repository.*;
import ru.avalieva.otus.library_hw12_security.service.impl.LibraryException;
import ru.avalieva.otus.library_hw12_security.service.impl.UserServiceImpl;

import java.sql.SQLException;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ContextConfiguration(classes = UserServiceImpl.class)
@SpringBootTest
public class UserServiceTest {
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @MockBean
    private BCryptPasswordEncoder encoder;

    @Test
    public void getLoginInfoTest() {
        User user = new User(0,  "admin", "password");
        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(user));

        var result = userService.getUser(user.getLogin());
        Assertions.assertEquals(user, result);

        verify(userRepository, times(1)).findByLogin(user.getLogin());
    }

    @Test
    public void getLoginInfoTestFailed() {
        final String USERNAME = "admin";
        when(userRepository.findByLogin(USERNAME)).thenReturn(Optional.ofNullable(null));
        when(messageService.getMessage("security.unknown.login", USERNAME)).thenReturn("Error");

        var error = Assertions.assertThrows(LibraryException.class, () -> { userService.getUser(USERNAME); });
        Assertions.assertEquals("Error", error.getMessage());

        verify(userRepository, times(1)).findByLogin(USERNAME);
        verify(messageService, times(1)).getMessage("security.unknown.login", USERNAME);
    }

    @Test
    public void addLoginTest() {
        User user = new User(0,  "admin", "password");
        User userEncoded = new User(0,  "admin", "$2a$10$z7a5d2Lx6Q26dMBND5GiSe3Gassdyn/GmnqSPCabKhoPoNk5AaJGG");
        when(userRepository.save(userEncoded)).thenReturn(userEncoded);
        when(encoder.encode(user.getPassword())).thenReturn(userEncoded.getPassword());

        userService.addUser(user);

        verify(userRepository, times(1)).save(userEncoded);
        verify(encoder, times(1)).encode(user.getPassword());
    }

    @Test
    public void addLoginTestFailed() {
        User user = new User(0,  "admin", "password");
        User userEncoded = new User(0,  "admin", "$2a$10$Z9uPKQnoPNcN0hSiKSFJeOaU8vF95SXztU36GyR5RSdDYfJtTcv.K");
        BadSqlGrammarException exception = new BadSqlGrammarException("error", "sql", new SQLException("error"));
        when(userRepository.save(userEncoded)).thenThrow(exception);
        when(messageService.getMessage("user.add.error",
                user.getLogin(), exception.getMessage())).thenReturn("Cannot add");
        when(encoder.encode(user.getPassword())).thenReturn(userEncoded.getPassword());


        var error = Assertions.assertThrows(LibraryException.class, () -> { userService.addUser(user); });
        Assertions.assertEquals("Cannot add", error.getMessage());

        verify(userRepository, times(1)).save(userEncoded);
        verify(messageService, times(1)).getMessage("user.add.error",
                user.getLogin(), exception.getMessage());

        verify(encoder, times(1)).encode(user.getPassword());

    }
}
