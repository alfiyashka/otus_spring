package ru.avalieva.otus.recipe.recomendation.system.model;

public class RecipeManagerException extends RuntimeException {
    public RecipeManagerException(String error) {
        super(error);
    }

    public RecipeManagerException(String error, Throwable throwable) {
        super(error, throwable);
    }
}
