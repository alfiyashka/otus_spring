package ru.avalieva.otus.library_hw13_security.service;

public interface MessageService {
    String getMessage(String messageId, Object... objs);
    String getMessage(String messageId);
}