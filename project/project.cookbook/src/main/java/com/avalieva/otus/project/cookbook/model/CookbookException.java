package com.avalieva.otus.project.cookbook.model;

public class CookbookException extends RuntimeException {
    public CookbookException(String error) {
        super(error);
    }

    public CookbookException(String error, Throwable throwable) {
        super(error, throwable);
    }
}