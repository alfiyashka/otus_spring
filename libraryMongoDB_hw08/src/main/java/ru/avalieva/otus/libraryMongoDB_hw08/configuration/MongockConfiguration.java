package ru.avalieva.otus.libraryMongoDB_hw08.configuration;

import com.github.cloudyrock.mongock.Mongock;
import com.github.cloudyrock.mongock.SpringMongockBuilder;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongockConfiguration {

    private static final String CHANGELOGS_PACKAGE = "ru.avalieva.otus.libraryMongoDB_hw08.changelog";

    @Bean
    public Mongock mongock(MongoSettings mongoSettings, MongoClient mongoClient) {
        return new SpringMongockBuilder(mongoClient, mongoSettings.getDatabase(), CHANGELOGS_PACKAGE)
                .build();
    }

}
