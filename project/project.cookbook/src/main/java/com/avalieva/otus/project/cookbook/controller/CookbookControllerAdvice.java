package com.avalieva.otus.project.cookbook.controller;

import com.avalieva.otus.project.cookbook.model.CookbookException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CookbookControllerAdvice {

    @ExceptionHandler(CookbookException.class)
    public ResponseEntity handleCookbookException(CookbookException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
