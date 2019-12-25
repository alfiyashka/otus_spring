package com.otus.avalieva.library.service;

public interface MessageService {
    String getMessage(String messageId, Object... objs);
    String getMessage(String messageId);
}
