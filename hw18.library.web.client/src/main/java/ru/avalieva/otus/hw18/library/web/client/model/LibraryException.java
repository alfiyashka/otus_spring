package ru.avalieva.otus.hw18.library.web.client.model;

public class LibraryException extends RuntimeException {
    public LibraryException(String error) {
        super(error);
    }

    public LibraryException(String error, Throwable throwable) {
        super(error, throwable);
    }
}