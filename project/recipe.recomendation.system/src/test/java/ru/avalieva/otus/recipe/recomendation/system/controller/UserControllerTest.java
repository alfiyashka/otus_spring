package ru.avalieva.otus.recipe.recomendation.system.controller;


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
import ru.avalieva.otus.recipe.recomendation.system.domain.User;
import ru.avalieva.otus.recipe.recomendation.system.security.UserDetailsServiceImpl;
import ru.avalieva.otus.recipe.recomendation.system.service.MessageService;
import ru.avalieva.otus.recipe.recomendation.system.service.UserService;
import ru.avalieva.otus.recipe.recomendation.system.model.RecipeManagerException;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
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
        User user = new User(null,  "admin", "password");
        doNothing().when(userService).addUser(user);

        this.mvc.perform(post("/api/user")
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).addUser(user);
    }

    @Test
    public void addNewUserTestFailed() throws Exception {
        User user = new User(null,  "admin", "password");
        RecipeManagerException exception = new RecipeManagerException("error");
        doThrow(exception).when(userService).addUser(user);

        MvcResult result = this.mvc.perform(post("/api/user")
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals(exception.getMessage(), result.getResponse().getContentAsString());

        verify(userService, times(1)).addUser(user);
    }
}
