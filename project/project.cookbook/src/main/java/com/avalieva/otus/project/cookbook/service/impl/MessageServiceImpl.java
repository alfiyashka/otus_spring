package com.avalieva.otus.project.cookbook.service.impl;


import com.avalieva.otus.project.cookbook.configuration.LocaleSettings;
import com.avalieva.otus.project.cookbook.service.MessageService;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

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


