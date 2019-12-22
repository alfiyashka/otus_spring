package ru.otus.avalieva.homework4.testing.impl.exception;

public class QuestionReaderException extends RuntimeException  {
    public QuestionReaderException(String error) {
        super(error);
    }

    public QuestionReaderException(String error, Throwable throwable) {
        super(error, throwable);
    }
}
