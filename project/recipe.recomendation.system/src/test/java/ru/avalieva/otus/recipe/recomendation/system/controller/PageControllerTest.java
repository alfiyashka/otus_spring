package ru.avalieva.otus.recipe.recomendation.system.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.avalieva.otus.recipe.recomendation.system.domain.User;
import ru.avalieva.otus.recipe.recomendation.system.security.RecipeManagerUserDetails;
import ru.avalieva.otus.recipe.recomendation.system.security.UserDetailsServiceImpl;
import ru.avalieva.otus.recipe.recomendation.system.security.UserRole;
import ru.avalieva.otus.recipe.recomendation.system.service.MessageService;
import ru.avalieva.otus.recipe.recomendation.system.service.UserService;

import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@WebMvcTest({PageController.class, UserDetailsServiceImpl.class})
public class PageControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private MessageService messageService;

    private static Stream<Arguments> getArguments() {
        return Stream.of(
                Arguments.of("/recipe_info",
                        new RecipeManagerUserDetails(new User(null,"admin", "password"),
                                UserRole.ADMIN)),
                Arguments.of("/recipe_info",
                        new RecipeManagerUserDetails(new User(null,"user1", "password"),
                                UserRole.USER)),
                Arguments.of("/addRecipe",
                        new RecipeManagerUserDetails(new User(null,"admin", "password"),
                                UserRole.ADMIN)),
                Arguments.of("/addRecipe",
                        new RecipeManagerUserDetails(new User(null,"user1", "password"),
                                UserRole.USER)),
                Arguments.of("/findRecipe",
                        new RecipeManagerUserDetails(new User(null,"admin", "password"),
                                UserRole.ADMIN)),
                Arguments.of("/findRecipe",
                        new RecipeManagerUserDetails(new User(null,"user1", "password"),
                                UserRole.USER)),
                Arguments.of("/ration",
                        new RecipeManagerUserDetails(new User(null,"admin", "password"),
                                UserRole.ADMIN)),
                Arguments.of("/ration",
                        new RecipeManagerUserDetails(new User(null,"user1", "password"),
                                UserRole.USER)),
                Arguments.of("/recipeSystem/recipeType/recipe",
                        new RecipeManagerUserDetails(new User(null,"admin", "password"),
                                UserRole.ADMIN)),
                Arguments.of("/recipeSystem/recipeType/recipe",
                        new RecipeManagerUserDetails(new User(null,"user1", "password"),
                                UserRole.USER)),
                Arguments.of("/",null),
                Arguments.of("/newUser", null)
        );
    }

    @ParameterizedTest
    @MethodSource("getArguments")
    public void pageTestSuccess(String url, UserDetails user) throws Exception {
        MockHttpServletRequestBuilder getRequest = get(url);
        if (user != null) {
            getRequest.with(user(user));
        }
        this.mvc.perform(getRequest)
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {"/recipe_info", "/addRecipe", "/findRecipe", "/ration", "/recipeSystem/recipeType/recipe"})
    public void pageTestFailNotAllowed (String url) throws Exception {
        this.mvc.perform(get(url))
                .andExpect(status().isFound());
    }




}
