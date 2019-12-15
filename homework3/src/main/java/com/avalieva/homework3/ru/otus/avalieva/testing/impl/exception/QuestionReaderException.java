package com.avalieva.homework3.ru.otus.avalieva.testing.impl.exception;

public class QuestionReaderException extends RuntimeException  {
    public QuestionReaderException(String error) {
        super(error);
    }

    public QuestionReaderException(String error, Throwable throwable) {
        super(error, throwable);
    }
}
