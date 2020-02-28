package ru.avalieva.com.library_hw11_webflux.configuration;


import com.github.cloudyrock.mongock.Mongock;
import com.github.cloudyrock.mongock.SpringMongockBuilder;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongockConfiguration {

    private static final String CHANGELOGS_PACKAGE = "ru.avalieva.com.library_hw11_webflux.changelog";

    @Bean
    public Mongock mongock(MongoSettings mongoSettings, MongoClient mongoClient) {
        return new SpringMongockBuilder(mongoClient, mongoSettings.getDatabase(), CHANGELOGS_PACKAGE)
                .build();
    }

}
