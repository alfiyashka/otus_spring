package ru.avalieva.otus.hw15spring.integration.integration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageChannel;
import ru.avalieva.otus.hw15spring.integration.domain.Recipe;

import java.util.List;

@IntegrationComponentScan
@EnableIntegration
@Configuration
public class Config {
    @Bean
    public MessageChannel recipeChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    public PublishSubscribeChannel medicineChannel() {
        return MessageChannels.publishSubscribe().datatype(List.class).get();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {
        return Pollers.fixedDelay(1000).get();
    }

    @Bean
    public IntegrationFlow getMedicineIntegrationFlow() {
        return IntegrationFlows.from("recipeChannel")
                .transform(Recipe::getMedicineItems)
                .split()
                .handle("pharmacyServiceImpl", "get")
                .aggregate()
                .channel("medicineChannel")
                .get();
    }

    @Bean
    public MessageChannel addMedicineChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    public PublishSubscribeChannel addMedicineResultChannel() {
        return MessageChannels.publishSubscribe().datatype(Boolean.class).get();
    }

    @Bean
    public IntegrationFlow addMedicineIntegrationFlow() {
        return IntegrationFlows.from("addMedicineChannel")
                .handle("pharmacyServiceImpl", "add")
                .channel("addMedicineResultChannel")
                .get();
    }

    @Bean
    public MessageChannel addAllChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    public PublishSubscribeChannel addAllResultChannel() {
        return MessageChannels.publishSubscribe().datatype(List.class).get();
    }

    @Bean
    public IntegrationFlow getAllMedicineIntegrationFlow() {
        return IntegrationFlows.from("addAllChannel")
                .handle("pharmacyServiceImpl", "getAll")
                .channel("addAllResultChannel")
                .get();
    }
}
