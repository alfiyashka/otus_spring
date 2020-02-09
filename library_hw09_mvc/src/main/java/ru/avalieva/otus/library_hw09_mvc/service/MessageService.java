package ru.avalieva.otus.library_hw09_mvc.service;

public interface MessageService {
    String getMessage(String messageId, Object... objs);
    String getMessage(String messageId);
}
