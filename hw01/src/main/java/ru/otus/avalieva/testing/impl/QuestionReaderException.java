package ru.otus.avalieva.testing.impl;

public class QuestionReaderException extends RuntimeException  {
    public QuestionReaderException(String error) {
        super(error);
    }

    public QuestionReaderException(String error, Throwable throwable) {
        super(error, throwable);
    }
}
