package ru.avalieva.otus.recipe.recomendation.system.service;

public interface MessageService {
    String getMessage(String messageId, Object... objs);
    String getMessage(String messageId);
}
