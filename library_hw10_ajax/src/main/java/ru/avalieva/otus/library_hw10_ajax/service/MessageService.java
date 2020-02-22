package ru.avalieva.otus.library_hw10_ajax.service;

public interface MessageService {
    String getMessage(String messageId, Object... objs);
    String getMessage(String messageId);
}
