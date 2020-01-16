package ru.otus.avalieva.library.orm.jpa.service;

public interface MessageService {
    String getMessage(String messageId, Object... objs);
    String getMessage(String messageId);
}

