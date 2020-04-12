package ru.otus.avaliva.hw17.library.docker.service;

public interface MessageService {
    String getMessage(String messageId, Object... objs);
    String getMessage(String messageId);
}
