package ru.otus.avalieva.testing.impl;


public class TestProcessorException extends RuntimeException  {
    public TestProcessorException(String error) {
        super(error);
    }

    public TestProcessorException(String error, Throwable throwable) {
        super(error, throwable);
    }
}
