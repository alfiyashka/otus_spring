package ru.avalieva.otus.hw15spring.integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ru.avalieva.otus.hw15spring.integration.domain.Medicine;
import ru.avalieva.otus.hw15spring.integration.domain.MedicineItem;
import ru.avalieva.otus.hw15spring.integration.domain.Recipe;
import ru.avalieva.otus.hw15spring.integration.integration.Config;
import ru.avalieva.otus.hw15spring.integration.integration.PharmacyIntegration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class Application {

	public static void main(String[] args)  {
		SpringApplication.run(Application.class, args);
	}

}
