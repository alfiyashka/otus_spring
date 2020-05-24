package ru.avalieva.otus.recipe.recomendation.system.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {

    @GetMapping("/")
    public String indexPage() {
        return "index";
    }

    @GetMapping("/newUser")
    public String newUserPage() {
        return "new_user";
    }

    @GetMapping("/recipe_info")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String getRecipeInfoPage() {
        return "recipe_info";
    }

    @GetMapping("/addRecipe")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String getAddRecipePage() {
        return "add_recipe";
    }

    @GetMapping("/findRecipe")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String getFindRecipePage() {
        return "find_recipe";
    }

    @GetMapping("/ration")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String getRationPage() {
        return "ration";
    }

    @GetMapping("/recipeSystem/{recipeType}/recipe")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String getRecipeInfoByTypePage(@PathVariable String recipeType) {
        return "recipes_by_type";
    }
}
