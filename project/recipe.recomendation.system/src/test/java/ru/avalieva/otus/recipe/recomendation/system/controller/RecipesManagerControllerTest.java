package ru.avalieva.otus.recipe.recomendation.system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.avalieva.otus.recipe.recomendation.system.domain.User;
import ru.avalieva.otus.recipe.recomendation.system.dto.*;
import ru.avalieva.otus.recipe.recomendation.system.model.ENutrient;
import ru.avalieva.otus.recipe.recomendation.system.model.ERecipeType;
import ru.avalieva.otus.recipe.recomendation.system.model.ERation;
import ru.avalieva.otus.recipe.recomendation.system.model.ERationStrategy;
import ru.avalieva.otus.recipe.recomendation.system.security.RecipeManagerUserDetails;
import ru.avalieva.otus.recipe.recomendation.system.security.UserDetailsServiceImpl;
import ru.avalieva.otus.recipe.recomendation.system.security.UserRole;
import ru.avalieva.otus.recipe.recomendation.system.service.MessageService;
import ru.avalieva.otus.recipe.recomendation.system.service.RecipeManagerService;
import ru.avalieva.otus.recipe.recomendation.system.service.UserService;
import ru.avalieva.otus.recipe.recomendation.system.model.RecipeManagerException;

import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@WebMvcTest({RecipesManagerController.class, UserDetailsServiceImpl.class})
public class RecipesManagerControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private RecipeManagerService recipeManagerService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private MessageService messageService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @ParameterizedTest
    @EnumSource(
            value = UserRole.class,
            names = {"USER", "ADMIN"})
    public void getIngredientsTest(UserRole role) throws Exception {
        List<String> ingredients = new ArrayList<>();
        ingredients.add("APPLE");
        when(recipeManagerService.getAllIngredients()).thenReturn(ingredients);

        RecipeManagerUserDetails user = new RecipeManagerUserDetails(new User(null,"user", "password"), role);

        MvcResult result = this.mvc.perform(get("/recipeSystem/api/ingredient")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(user)))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals(ingredients, objectMapper.readValue(result.getResponse().getContentAsString(UTF_8),
                objectMapper.getTypeFactory().constructCollectionType(List.class, String.class)));

        verify(recipeManagerService, times(1)).getAllIngredients();
    }

    @ParameterizedTest
    @EnumSource(
            value = UserRole.class,
            names = {"USER", "ADMIN"})
    public void getIngredientsTestFailed(UserRole role) throws Exception {
        RecipeManagerException exception = new RecipeManagerException("error");
        when(recipeManagerService.getAllIngredients()).thenThrow(exception);

        RecipeManagerUserDetails user = new RecipeManagerUserDetails(new User(null,"user", "password"), role);

        MvcResult result = this.mvc.perform(get("/recipeSystem/api/ingredient")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(user)))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals(exception.getMessage(), result.getResponse().getContentAsString());

        verify(recipeManagerService, times(1)).getAllIngredients();
    }

    @ParameterizedTest
    @EnumSource(
            value = UserRole.class,
            names = {"USER", "ADMIN"})
    public void addRecipeTest(UserRole role) throws Exception {
        RecipeDtoJson recipe = new RecipeDtoJson();
        doNothing().when(recipeManagerService).addRecipe(recipe);

        RecipeManagerUserDetails user = new RecipeManagerUserDetails(new User(null,"user", "password"), role);

        this.mvc.perform(post("/recipeSystem/api/recipe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recipe))
                .with(user(user)))
                .andExpect(status().isOk());

        verify(recipeManagerService, times(1)).addRecipe(recipe);
    }

    @ParameterizedTest
    @EnumSource(
            value = UserRole.class,
            names = {"USER", "ADMIN"})
    public void addRecipeTestFailed(UserRole role) throws Exception {
        RecipeDtoJson recipe = new RecipeDtoJson();
        RecipeManagerException exception = new RecipeManagerException("error");
        doThrow(exception).when(recipeManagerService).addRecipe(recipe);

        RecipeManagerUserDetails user = new RecipeManagerUserDetails(new User(null,"user", "password"), role);

        this.mvc.perform(post("/recipeSystem/api/recipe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recipe))
                .with(user(user)))
                .andExpect(status().isBadRequest());

        verify(recipeManagerService, times(1)).addRecipe(recipe);
    }

    @ParameterizedTest
    @EnumSource(
            value = UserRole.class,
            names = {"USER", "ADMIN"})
    public void findRecipeTest(UserRole role) throws Exception {
        IngredientDto ingredient = new IngredientDto("Apple", 100);
        List<String> ingredients = new ArrayList<>();
        ingredients.add(ingredient.getIngredientName());
        RecipeRequest recipeRequest = new RecipeRequest("Recipe Name", ERecipeType.SALAD.name(), ingredients);
        List<IngredientDto> ingredientsDto = new ArrayList<>();
        ingredientsDto.add(ingredient);
        List<RecipeDtoJson> recipes = new ArrayList<>();
        RecipeDtoJson recipe = new RecipeDtoJson("RecipeName", ERecipeType.SALAD.name(), 100, "description", ingredientsDto, null);
        recipes.add(recipe);
        when(recipeManagerService.findRecipe(recipeRequest)).thenReturn(recipes);

        RecipeManagerUserDetails user = new RecipeManagerUserDetails(new User(null,"user", "password"), role);

        MvcResult result = this.mvc.perform(post("/recipeSystem/api/recipe/find")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recipeRequest))
                .with(user(user)))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals(recipes, objectMapper.readValue(result.getResponse().getContentAsString(UTF_8),
                objectMapper.getTypeFactory().constructCollectionType(List.class, RecipeDtoJson.class)));

        verify(recipeManagerService, times(1)).findRecipe(recipeRequest);
    }

    @ParameterizedTest
    @EnumSource(
            value = UserRole.class,
            names = {"USER", "ADMIN"})
    public void findRecipeTestFailed(UserRole role) throws Exception {
        IngredientDto ingredient = new IngredientDto("Apple", 100);
        List<String> ingredients = new ArrayList<>();
        ingredients.add(ingredient.getIngredientName());
        RecipeRequest recipeRequest = new RecipeRequest("Recipe Name", ERecipeType.SALAD.name(), ingredients);

        RecipeManagerException exception = new RecipeManagerException("error");
        when(recipeManagerService.findRecipe(recipeRequest)).thenThrow(exception);

        RecipeManagerUserDetails user = new RecipeManagerUserDetails(new User(null,"user", "password"), role);

        MvcResult result = this.mvc.perform(post("/recipeSystem/api/recipe/find")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recipeRequest))
                .with(user(user)))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals(exception.getMessage(), result.getResponse().getContentAsString());

        verify(recipeManagerService, times(1)).findRecipe(recipeRequest);
    }

    @ParameterizedTest
    @EnumSource(
            value = UserRole.class,
            names = {"USER", "ADMIN"})
    public void getRecipeTypesTest(UserRole role) throws Exception {
        List<RecipeTypeDto> recipeTypes = new ArrayList<>();
        recipeTypes.add(new RecipeTypeDto(ERecipeType.SOUP.name(), ERecipeType.SOUP.getValue()));
        when(recipeManagerService.getRecipeTypes()).thenReturn(recipeTypes);

        RecipeManagerUserDetails user = new RecipeManagerUserDetails(new User(null,"user", "password"), role);

        MvcResult result = this.mvc.perform(get("/recipeSystem/api/recipeType")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(user)))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals(recipeTypes, objectMapper.readValue(result.getResponse().getContentAsString(UTF_8),
                objectMapper.getTypeFactory().constructCollectionType(List.class, RecipeTypeDto.class)));

        verify(recipeManagerService, times(1)).getRecipeTypes();
    }

    @ParameterizedTest
    @EnumSource(
            value = UserRole.class,
            names = {"USER", "ADMIN"})
    public void getRecipeTypesTestFailed(UserRole role) throws Exception {
        RecipeManagerException exception = new RecipeManagerException("error");
        when(recipeManagerService.getRecipeTypes()).thenThrow(exception);

        RecipeManagerUserDetails user = new RecipeManagerUserDetails(new User(null,"user", "password"), role);

        MvcResult result = this.mvc.perform(get("/recipeSystem/api/recipeType")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(user)))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals(exception.getMessage(), result.getResponse().getContentAsString());

        verify(recipeManagerService, times(1)).getRecipeTypes();
    }

    @ParameterizedTest
    @EnumSource(
            value = UserRole.class,
            names = {"USER", "ADMIN"})
    public void getRecipeTypeValueTest(UserRole role) throws Exception {
        RecipeManagerUserDetails user = new RecipeManagerUserDetails(new User(null,"user", "password"), role);

        MvcResult result = this.mvc.perform(get("/recipeSystem/api/recipeType/" + ERecipeType.SALAD.name())
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(user)))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals(ERecipeType.SALAD.getValue(), result.getResponse().getContentAsString(UTF_8));

    }

    @ParameterizedTest
    @EnumSource(
            value = UserRole.class,
            names = {"USER", "ADMIN"})
    public void getСriterionListTest(UserRole role) throws Exception {
        List<RationStrategyDto> rationStrategies = new ArrayList<>();
        rationStrategies.add(RationStrategyDtoConverter.convert(ERationStrategy.DIET_SOUP));
        when(recipeManagerService.getStrategyList()).thenReturn(rationStrategies);

        RecipeManagerUserDetails user = new RecipeManagerUserDetails(new User(null,"user", "password"), role);

        MvcResult result = this.mvc.perform(get("/recipeSystem/api/strategy")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(user)))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals(rationStrategies, objectMapper.readValue(result.getResponse().getContentAsString(UTF_8),
                objectMapper.getTypeFactory().constructCollectionType(List.class, RationStrategyDto.class)));

        verify(recipeManagerService, times(1)).getStrategyList();
    }

    @ParameterizedTest
    @EnumSource(
            value = UserRole.class,
            names = {"USER", "ADMIN"})
    public void getСriterionListTestFailed(UserRole role) throws Exception {
        RecipeManagerException exception = new RecipeManagerException("error");
        when(recipeManagerService.getStrategyList()).thenThrow(exception);

        RecipeManagerUserDetails user = new RecipeManagerUserDetails(new User(null,"user", "password"), role);

        MvcResult result = this.mvc.perform(get("/recipeSystem/api/strategy")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(user)))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals(exception.getMessage(), result.getResponse().getContentAsString());

        verify(recipeManagerService, times(1)).getStrategyList();
    }


    @ParameterizedTest
    @EnumSource(
            value = UserRole.class,
            names = {"USER", "ADMIN"})
    public void getRecipesByTypeTest(UserRole role) throws Exception {
        IngredientDto ingredient = new IngredientDto("Apple", 100);
        List<IngredientDto> ingredientsDto = new ArrayList<>();
        ingredientsDto.add(ingredient);
        List<RecipeDtoJson> recipes = new ArrayList<>();
        RecipeDtoJson recipe = new RecipeDtoJson("RecipeName", ERecipeType.SALAD.name(), 100, "description", ingredientsDto, null);
        recipes.add(recipe);
        when(recipeManagerService.getRecipesByType(ERecipeType.SALAD.name())).thenReturn(recipes);

        RecipeManagerUserDetails user = new RecipeManagerUserDetails(new User(null,"user", "password"), role);

        MvcResult result = this.mvc.perform(get("/recipeSystem/api/recipeType/" + ERecipeType.SALAD.name() + "/recipe")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(user)))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals(recipes, objectMapper.readValue(result.getResponse().getContentAsString(UTF_8),
                objectMapper.getTypeFactory().constructCollectionType(List.class, RecipeDtoJson.class)));

        verify(recipeManagerService, times(1)).getRecipesByType(ERecipeType.SALAD.name());
    }

    @ParameterizedTest
    @EnumSource(
            value = UserRole.class,
            names = {"USER", "ADMIN"})
    public void getRecipesByTypeTestFailed(UserRole role) throws Exception {
        RecipeManagerException exception = new RecipeManagerException("error");
        when(recipeManagerService.getRecipesByType(ERecipeType.SALAD.name())).thenThrow(exception);

        RecipeManagerUserDetails user = new RecipeManagerUserDetails(new User(null,"user", "password"), role);

        MvcResult result = this.mvc.perform(get("/recipeSystem/api/recipeType/" + ERecipeType.SALAD.name() + "/recipe")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(user)))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals(exception.getMessage(), result.getResponse().getContentAsString());

        verify(recipeManagerService, times(1)).getRecipesByType(ERecipeType.SALAD.name());
    }

    @ParameterizedTest
    @EnumSource(
            value = UserRole.class,
            names = {"USER", "ADMIN"})
    public void getRecipesByRationStrategyTest(UserRole role) throws Exception {
        IngredientDto ingredient = new IngredientDto("Apple", 100);
        List<IngredientDto> ingredientsDto = new ArrayList<>();
        ingredientsDto.add(ingredient);
        List<RecipeDtoJson> recipes = new ArrayList<>();
        RecipeDtoJson recipe = new RecipeDtoJson("RecipeName", ERecipeType.SALAD.name(), 100, "description", ingredientsDto, null);
        recipes.add(recipe);
        RecipeForRationDto recipeForRationDto = new RecipeForRationDto(RationDtoConverter.convert(ERation.DINNER), recipes);
        List<RecipeForRationDto> recipeForRationDtos = new ArrayList<>();
        recipeForRationDtos.add(recipeForRationDto);
        when(recipeManagerService.getRecipes(ERationStrategy.VEGAN)).thenReturn(recipeForRationDtos);

        RecipeManagerUserDetails user = new RecipeManagerUserDetails(new User(null,"user", "password"), role);

        MvcResult result = this.mvc.perform(get("/recipeSystem/api/recipeRation/" + ERationStrategy.VEGAN.name() + "/recipe")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(user)))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals(recipeForRationDtos, objectMapper.readValue(result.getResponse().getContentAsString(UTF_8),
                objectMapper.getTypeFactory().constructCollectionType(List.class, RecipeForRationDto.class)));

        verify(recipeManagerService, times(1)).getRecipes(ERationStrategy.VEGAN);
    }

    @ParameterizedTest
    @EnumSource(
            value = UserRole.class,
            names = {"USER", "ADMIN"})
    public void getRecipesByRationStrategyTestFailed(UserRole role) throws Exception {
        RecipeManagerException exception = new RecipeManagerException("error");
        when(recipeManagerService.getRecipes(ERationStrategy.VEGAN)).thenThrow(exception);

        RecipeManagerUserDetails user = new RecipeManagerUserDetails(new User(null,"user", "password"), role);

        MvcResult result = this.mvc.perform(get("/recipeSystem/api/recipeRation/" + ERationStrategy.VEGAN.name() + "/recipe")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(user)))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals(exception.getMessage(), result.getResponse().getContentAsString());

        verify(recipeManagerService, times(1)).getRecipes(ERationStrategy.VEGAN);
    }

    @ParameterizedTest
    @EnumSource(
            value = UserRole.class,
            names = {"USER", "ADMIN"})
    public void getNutrientValueTest(UserRole role) throws Exception {
        RecipeManagerUserDetails user = new RecipeManagerUserDetails(new User(null,"user", "password"), role);

        MvcResult result = this.mvc.perform(get("/recipeSystem/api/nutrient/" + ENutrient.CARBOHYDRATE.name())
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(user)))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals(ENutrient.CARBOHYDRATE.getValue(), result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @EnumSource(
            value = UserRole.class,
            names = {"USER", "ADMIN"})
    public void getDietDescriptionTest(UserRole role) throws Exception {
        when(recipeManagerService.getRationDescription(ERationStrategy.VEGAN.name())).thenReturn("Description");

        RecipeManagerUserDetails user = new RecipeManagerUserDetails(new User(null,"user", "password"), role);

        MvcResult result = this.mvc.perform(get("/recipeSystem/api/diet/" + ERationStrategy.VEGAN.name())
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(user)))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals("Description", result.getResponse().getContentAsString(UTF_8));

        verify(recipeManagerService, times(1)).getRationDescription(ERationStrategy.VEGAN.name());
    }

    @ParameterizedTest
    @EnumSource(
            value = UserRole.class,
            names = {"USER", "ADMIN"})
    public void getDietDescriptionTestFailed(UserRole role) throws Exception {
        RecipeManagerException exception = new RecipeManagerException("error");
        when(recipeManagerService.getRationDescription(ERationStrategy.VEGAN.name())).thenThrow(exception);

        RecipeManagerUserDetails user = new RecipeManagerUserDetails(new User(null,"user", "password"), role);

        MvcResult result = this.mvc.perform(get("/recipeSystem/api/diet/" + ERationStrategy.VEGAN.name())
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(user)))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals(exception.getMessage(), result.getResponse().getContentAsString());

        verify(recipeManagerService, times(1)).getRationDescription(ERationStrategy.VEGAN.name());
    }
}
