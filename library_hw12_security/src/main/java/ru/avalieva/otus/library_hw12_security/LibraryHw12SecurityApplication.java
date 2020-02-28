package ru.avalieva.otus.library_hw12_security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.avalieva.otus.library_hw12_security.security.UserDetailsServiceImpl;
import ru.avalieva.otus.library_hw12_security.service.UserService;

@SpringBootApplication
public class LibraryHw12SecurityApplication {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		SpringApplication.run(LibraryHw12SecurityApplication.class, args);
	}

}
