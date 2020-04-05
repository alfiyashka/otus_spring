package ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.service;

public interface MessageService {
    String getMessage(String messageId, Object... objs);
    String getMessage(String messageId);
}