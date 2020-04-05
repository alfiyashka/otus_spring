package ru.otus.avaliva.hw17.library.docker.controller;

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
import org.springframework.test.web.servlet.MvcResult;
import ru.otus.avaliva.hw17.library.docker.domain.User;
import ru.otus.avaliva.hw17.library.docker.security.UserDetailsServiceImpl;
import ru.otus.avaliva.hw17.library.docker.service.MessageService;
import ru.otus.avaliva.hw17.library.docker.service.UserService;
import ru.otus.avaliva.hw17.library.docker.service.impl.LibraryException;

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

        MvcResult result = this.mvc.perform(post("/api/users")
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals(exception.getMessage(), result.getResponse().getContentAsString());

        verify(userService, times(1)).addUser(user);
    }
}

