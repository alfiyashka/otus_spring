package ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.service.impl;

public class LibraryException extends RuntimeException {
    public LibraryException(String error) {
        super(error);
    }

    public LibraryException(String error, Throwable throwable) {
        super(error, throwable);
    }
}
