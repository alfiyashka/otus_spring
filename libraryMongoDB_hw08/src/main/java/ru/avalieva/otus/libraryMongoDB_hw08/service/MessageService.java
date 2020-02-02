package ru.avalieva.otus.libraryMongoDB_hw08.service;

public interface MessageService {
    String getMessage(String messageId, Object... objs);
    String getMessage(String messageId);
}
