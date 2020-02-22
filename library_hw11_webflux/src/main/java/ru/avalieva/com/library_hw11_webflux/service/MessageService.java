package ru.avalieva.com.library_hw11_webflux.service;

public interface MessageService {
    String getMessage(String messageId, Object... objs);
    String getMessage(String messageId);
}

