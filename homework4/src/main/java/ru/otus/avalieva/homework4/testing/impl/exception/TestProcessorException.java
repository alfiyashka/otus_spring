package ru.otus.avalieva.homework4.testing.impl.exception;

public class TestProcessorException extends RuntimeException  {
    public TestProcessorException(String error) {
        super(error);
    }

    public TestProcessorException(String error, Throwable throwable) {
        super(error, throwable);
    }
}
