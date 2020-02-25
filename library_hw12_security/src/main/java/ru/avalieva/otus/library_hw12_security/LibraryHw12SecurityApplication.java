package ru.avalieva.otus.library_hw12_security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.avalieva.otus.library_hw12_security.security.UserDetailsServiceImpl;
import ru.avalieva.otus.library_hw12_security.service.UserService;

@SpringBootApplication
public class LibraryHw12SecurityApplication {

	@Bean
	public UserDetailsService getUserDetailsService(UserService userService){
		return new UserDetailsServiceImpl(userService);
	}

	public static void main(String[] args) {
		SpringApplication.run(LibraryHw12SecurityApplication.class, args);
	}

}
