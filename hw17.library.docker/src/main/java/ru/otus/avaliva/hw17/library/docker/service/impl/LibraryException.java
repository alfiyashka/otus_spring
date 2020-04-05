package ru.otus.avaliva.hw17.library.docker.service.impl;

public class LibraryException extends RuntimeException {
    public LibraryException(String error) {
        super(error);
    }

    public LibraryException(String error, Throwable throwable) {
        super(error, throwable);
    }
}