package com.avalieva.otus.project.cookbook;

import com.avalieva.otus.project.cookbook.neo.data.DataLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Bean;

@EnableEurekaServer
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public DataLoader initDataLoad(DataLoader dataLoader) throws Exception {
		dataLoader.createTestData();
		return dataLoader;
	}


}
