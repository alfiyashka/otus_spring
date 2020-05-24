package ru.avalieva.otus.recipe.recomendation.system.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.avalieva.otus.recipe.recomendation.system.model.RecipeManagerException;

@RestControllerAdvice
public class RecipeManagerRestControllerAdvice {

    @ExceptionHandler(RecipeManagerException.class)
    public ResponseEntity handleRecipeManagerException(RecipeManagerException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
