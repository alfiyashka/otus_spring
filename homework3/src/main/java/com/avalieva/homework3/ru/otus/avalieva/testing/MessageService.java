package com.avalieva.homework3.ru.otus.avalieva.testing;

public interface MessageService {
    String getMessage(String messageId, Object... objs);
    String getMessage(String messageId);
}
