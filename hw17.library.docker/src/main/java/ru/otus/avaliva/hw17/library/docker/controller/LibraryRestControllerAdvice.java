package ru.otus.avaliva.hw17.library.docker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.avaliva.hw17.library.docker.service.impl.LibraryException;

@RestControllerAdvice
public class LibraryRestControllerAdvice {

    @ExceptionHandler(LibraryException.class)
    public ResponseEntity handleLibraryException(LibraryException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }


}