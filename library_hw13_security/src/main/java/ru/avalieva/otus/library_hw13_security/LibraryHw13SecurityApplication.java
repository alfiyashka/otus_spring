package ru.avalieva.otus.library_hw13_security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class LibraryHw13SecurityApplication {




	public static void main(String[] args) {
		SpringApplication.run(LibraryHw13SecurityApplication.class, args);
	}

}
