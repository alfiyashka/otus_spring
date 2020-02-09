package ru.avalieva.otus.library_hw09_mvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.WebServer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.net.URL;

@SpringBootApplication
public class LibraryHw09MvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryHw09MvcApplication.class, args);
	}
}
