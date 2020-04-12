package ru.avalieva.otus.hw18.library.web.client.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.avalieva.otus.hw18.library.web.client.model.LibraryException;

@RestControllerAdvice
public class LibraryRestControllerAdvice {

    @ExceptionHandler(LibraryException.class)
    public ResponseEntity handleLibraryException(LibraryException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }


}