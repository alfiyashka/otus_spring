package ru.avalieva.otus.library_hw10_ajax.service.impl;

public class LibraryException extends RuntimeException {
    public LibraryException(String error) {
        super(error);
    }

    public LibraryException(String error, Throwable throwable) {
        super(error, throwable);
    }
}

