package ru.avalieva.otus.library_hw10_ajax.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "locale")
@Getter
@Setter
public class LocaleSettings {
    String country;
    String language;
    String filename;
}
