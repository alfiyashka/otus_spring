package com.avalieva.otus.project.cookbook.service;

public interface MessageService {
    String getMessage(String messageId, Object... objs);
    String getMessage(String messageId);
}
