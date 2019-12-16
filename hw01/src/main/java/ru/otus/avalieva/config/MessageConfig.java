package ru.otus.avalieva.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class MessageConfig {

    @Bean(name = "messageSource")
    public MessageSource getMessageResource() {
        ReloadableResourceBundleMessageSource messageResource =
                new ReloadableResourceBundleMessageSource();
        messageResource.setCacheSeconds(3600);
        messageResource.setBasenames("classpath:messages/messages");
        messageResource.setUseCodeAsDefaultMessage(true);
        messageResource.setDefaultEncoding("UTF-8");
        return messageResource;
    }
}
