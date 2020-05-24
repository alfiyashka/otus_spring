package com.avalieva.otus.project.cookbook.controller;

import com.avalieva.otus.project.cookbook.dto.RecipeDtoJson;
import com.avalieva.otus.project.cookbook.model.CookbookException;
import com.avalieva.otus.project.cookbook.model.ERecipeType;
import com.avalieva.otus.project.cookbook.model.RecipeRequest;
import com.avalieva.otus.project.cookbook.service.CookbookService;
import com.avalieva.otus.project.cookbook.service.impl.CookbookServiceImpl;
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
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@WebMvcTest({CookbookController.class, CookbookServiceImpl.class})
public class CookbookControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CookbookService cookbookService;

    @Test
    public void getIngredientsTest() throws Exception {
        List<String> ingredients = new ArrayList<>();
        ingredients.add("ЛУК");
        when(cookbookService.getIngredients()).thenReturn(ingredients);

        MvcResult result = this.mvc.perform(get("/api/ingredient")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals(ingredients, objectMapper.readValue(result.getResponse().getContentAsString(UTF_8),
                objectMapper.getTypeFactory().constructCollectionType(List.class, String.class)));

        verify(cookbookService, times(1)).getIngredients();
    }

    @Test
    public void getIngredientsTestFailed() throws Exception {
        CookbookException exception = new CookbookException("error");
        when(cookbookService.getIngredients()).thenThrow(exception);

        MvcResult result = this.mvc.perform(get("/api/ingredient")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals(exception.getMessage(), result.getResponse().getContentAsString());

        verify(cookbookService, times(1)).getIngredients();
    }

    @Test
    public void addRecipeTest() throws Exception {
        RecipeDtoJson recipe = new RecipeDtoJson();
        doNothing().when(cookbookService).addRecipe(recipe);

        this.mvc.perform(post("/api/recipe")
                .content(objectMapper.writeValueAsString(recipe))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(cookbookService, times(1)).addRecipe(recipe);
    }

    @Test
    public void addRecipeTestFailed() throws Exception {
        RecipeDtoJson recipe = new RecipeDtoJson();
        CookbookException exception = new CookbookException("error");
        doThrow(exception).when(cookbookService).addRecipe(recipe);

        MvcResult result = this.mvc.perform(post("/api/recipe")
                .content(objectMapper.writeValueAsString(recipe))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals(exception.getMessage(), result.getResponse().getContentAsString());

        verify(cookbookService, times(1)).addRecipe(recipe);
    }

    @Test
    public void getRecipeTypeTest() throws Exception {
        List<String> recipeTypes = new ArrayList<>();
        recipeTypes.add(ERecipeType.MAIN_COURSE.name());
        when(cookbookService.getRecipeTypes()).thenReturn(recipeTypes);

        MvcResult result = this.mvc.perform(get("/api/recipeType")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals(recipeTypes, objectMapper.readValue(result.getResponse().getContentAsString(UTF_8),
                objectMapper.getTypeFactory().constructCollectionType(List.class, String.class)));

        verify(cookbookService, times(1)).getRecipeTypes();
    }

    @Test
    public void getRecipeTypeTestFailed() throws Exception {
        CookbookException exception = new CookbookException("error");
        when(cookbookService.getRecipeTypes()).thenThrow(exception);

        MvcResult result = this.mvc.perform(get("/api/recipeType")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals(exception.getMessage(), result.getResponse().getContentAsString());

        verify(cookbookService, times(1)).getRecipeTypes();
    }

    @Test
    public void getRecipesByTypeTest() throws Exception {
        List<RecipeDtoJson> recipes = new ArrayList<>();
        recipes.add(new RecipeDtoJson());
        ERecipeType recipeType = ERecipeType.MAIN_COURSE;
        when(cookbookService.getRecipesByType(recipeType.name())).thenReturn(recipes);

        MvcResult result = this.mvc.perform(get("/api/" + recipeType.name() + "/recipe")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals(recipes, objectMapper.readValue(result.getResponse().getContentAsString(UTF_8),
                objectMapper.getTypeFactory().constructCollectionType(List.class, RecipeDtoJson.class)));

        verify(cookbookService, times(1)).getRecipesByType(recipeType.name());
    }

    @Test
    public void getRecipesByTypeTestFailed() throws Exception {
        ERecipeType recipeType = ERecipeType.MAIN_COURSE;
        CookbookException exception = new CookbookException("error");
        when(cookbookService.getRecipesByType(recipeType.name())).thenThrow(exception);

        MvcResult result = this.mvc.perform(get("/api/" + recipeType.name() + "/recipe")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals(exception.getMessage(), result.getResponse().getContentAsString());

        verify(cookbookService, times(1)).getRecipesByType(recipeType.name());
    }

    @Test
    public void findRecipesByRequestTest() throws Exception {
        List<RecipeDtoJson> recipes = new ArrayList<>();
        recipes.add(new RecipeDtoJson());
        RecipeRequest recipeRequest = new RecipeRequest();
        when(cookbookService.findRecipes(recipeRequest)).thenReturn(recipes);

        MvcResult result = this.mvc.perform(post("/api/recipe/find")
                .content(objectMapper.writeValueAsString(recipeRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals(recipes, objectMapper.readValue(result.getResponse().getContentAsString(UTF_8),
                objectMapper.getTypeFactory().constructCollectionType(List.class, RecipeDtoJson.class)));

        verify(cookbookService, times(1)).findRecipes(recipeRequest);
    }

    @Test
    public void findRecipesByRequestTestFailed() throws Exception {
        RecipeRequest recipeRequest = new RecipeRequest();
        CookbookException exception = new CookbookException("error");
        when(cookbookService.findRecipes(recipeRequest)).thenThrow(exception);

        MvcResult result = this.mvc.perform(post("/api/recipe/find/")
                .content(objectMapper.writeValueAsString(recipeRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals(exception.getMessage(), result.getResponse().getContentAsString());

        verify(cookbookService, times(1)).findRecipes(recipeRequest);
    }

    @Test
    public void findRecipesNotHaveIngredientsTest() throws Exception {
        List<RecipeDtoJson> recipes = new ArrayList<>();
        recipes.add(new RecipeDtoJson());
        RecipeRequest recipeRequest = new RecipeRequest();
        when(cookbookService.findRecipesNotHaveIngredients(recipeRequest)).thenReturn(recipes);

        MvcResult result = this.mvc.perform(post("/api/recipe/notHave")
                .content(objectMapper.writeValueAsString(recipeRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals(recipes, objectMapper.readValue(result.getResponse().getContentAsString(UTF_8),
                objectMapper.getTypeFactory().constructCollectionType(List.class, RecipeDtoJson.class)));

        verify(cookbookService, times(1)).findRecipesNotHaveIngredients(recipeRequest);
    }

    @Test
    public void findRecipesNotHaveIngredientsTestFailed() throws Exception {
        RecipeRequest recipeRequest = new RecipeRequest();
        CookbookException exception = new CookbookException("error");
        when(cookbookService.findRecipesNotHaveIngredients(recipeRequest)).thenThrow(exception);

        MvcResult result = this.mvc.perform(post("/api/recipe/notHave")
                .content(objectMapper.writeValueAsString(recipeRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals(exception.getMessage(), result.getResponse().getContentAsString());

        verify(cookbookService, times(1)).findRecipesNotHaveIngredients(recipeRequest);
    }

}
