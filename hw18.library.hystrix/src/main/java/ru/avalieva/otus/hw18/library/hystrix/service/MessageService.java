package ru.avalieva.otus.hw18.library.hystrix.service;

public interface MessageService {
    String getMessage(String messageId, Object... objs);
    String getMessage(String messageId);
}
