package ru.avalieva.com.library_hw11_webflux.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("spring.data.mongodb")
public class MongoSettings {
    private int port;
    private String database;
    private String uri;
}

