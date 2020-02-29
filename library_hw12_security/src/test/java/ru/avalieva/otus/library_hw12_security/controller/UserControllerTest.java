package ru.avalieva.otus.library_hw12_security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.avalieva.otus.library_hw12_security.domain.User;
import ru.avalieva.otus.library_hw12_security.security.UserDetailsServiceImpl;
import ru.avalieva.otus.library_hw12_security.service.MessageService;
import ru.avalieva.otus.library_hw12_security.service.UserService;
import ru.avalieva.otus.library_hw12_security.service.impl.LibraryException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@WebMvcTest({UserController.class, UserDetailsServiceImpl.class})
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private MessageService messageService;


    @Test
    public void addNewUserTest() throws Exception {
        User user = new User(0,  "admin", "password");
        doNothing().when(userService).addUser(user);

        this.mvc.perform(post("/api/users")
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).addUser(user);
    }

    @Test
    public void addNewUserTestFailed() throws Exception {
        User user = new User(0,  "admin", "password");
        LibraryException exception = new LibraryException("error");
        doThrow(exception).when(userService).addUser(user);

        var result = this.mvc.perform(post("/api/users")
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals(exception.getMessage(), result.getResponse().getContentAsString());

        verify(userService, times(1)).addUser(user);
    }
}
