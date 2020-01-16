package ru.otus.avalieva.library.orm.jpa.service.impl;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.avalieva.library.orm.jpa.configuration.LocaleSettings;
import ru.otus.avalieva.library.orm.jpa.service.MessageService;

import java.util.Locale;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageSource messageSource;

    private final Locale locale;

    MessageServiceImpl(LocaleSettings localeSettings,
                       MessageSource messageSource) {
        locale = getLocale(localeSettings);
        this.messageSource = messageSource;
    }

    private Locale getLocale(LocaleSettings localeSettings)
    {
        if (localeSettings.getCountry() == null || localeSettings.getCountry().isEmpty()
                || localeSettings.getLanguage() == null || localeSettings.getLanguage().isEmpty()) {
            return Locale.getDefault();
        }
        return new Locale(localeSettings.getLanguage(), localeSettings.getCountry());
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

