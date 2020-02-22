package ru.avalieva.com.library_hw11_webflux.service.impl;


public class LibraryException extends RuntimeException {
    public LibraryException(String error) {
        super(error);
    }

    public LibraryException(String error, Throwable throwable) {
        super(error, throwable);
    }

}

