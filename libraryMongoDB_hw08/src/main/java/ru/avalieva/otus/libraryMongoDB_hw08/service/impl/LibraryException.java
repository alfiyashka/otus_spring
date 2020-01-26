package ru.avalieva.otus.libraryMongoDB_hw08.service.impl;

public class LibraryException extends RuntimeException {
    public LibraryException(String error) {
        super(error);
    }

    public LibraryException(String error, Throwable throwable) {
        super(error, throwable);
    }

}
