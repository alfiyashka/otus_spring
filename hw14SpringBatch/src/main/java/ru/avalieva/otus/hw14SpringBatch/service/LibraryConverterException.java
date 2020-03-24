package ru.avalieva.otus.hw14SpringBatch.service;

public class LibraryConverterException extends RuntimeException {
    public LibraryConverterException(String error) {
        super(error);
    }

    public LibraryConverterException(String error, Throwable throwable) {
        super(error, throwable);
    }

}