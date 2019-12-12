package ru.otus.avalieva.testing.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.avalieva.testing.MessageService;

import java.util.Locale;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageSource messageSource;

    private final Locale locale;

    MessageServiceImpl(@Value("${locale.data}") String localValue,
                       MessageSource messageSource) {
        locale = getLocale(localValue);
        this.messageSource = messageSource;
    }

    private Locale getLocale(String localValue)
    {
        if (localValue == null || localValue.isEmpty()) {
            return Locale.getDefault();
        }
        return Locale.forLanguageTag(localValue);
    }

    @Override
    public String getMessage(String messageId, Object... objs) {
        return messageSource.getMessage(messageId, objs, locale);
    }

    @Override
    public String getMessage(String messageId) {
        return getMessage(messageId, null);
    }
}
