package ru.avalieva.otus.library_hw13_security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.avalieva.otus.library_hw13_security.service.impl.LibraryException;

@RestControllerAdvice
public class LibraryRestControllerAdvice {

    @ExceptionHandler(LibraryException.class)
    public ResponseEntity handleLibraryException(LibraryException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }


}