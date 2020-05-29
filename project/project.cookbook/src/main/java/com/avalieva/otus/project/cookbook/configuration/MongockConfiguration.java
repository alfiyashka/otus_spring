package com.avalieva.otus.project.cookbook.configuration;

import com.github.cloudyrock.mongock.Mongock;
import com.github.cloudyrock.mongock.SpringMongockBuilder;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongockConfiguration {

    private static final String CHANGE_LOG_PACKAGE = "com.avalieva.otus.project.cookbook.changelog";

    @Bean
    public Mongock mongock(MongoSettings mongoSettings, MongoClient mongoClient) {
        return new SpringMongockBuilder(mongoClient, mongoSettings.getDatabase(), CHANGE_LOG_PACKAGE)
                .build();
    }

}