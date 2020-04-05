package ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.projection.BookDto;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.domain.Author;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.domain.Book;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.domain.Comment;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.domain.Genre;

@Configuration
public class LibraryRestConfiguration {
    @Bean
    public RepositoryRestConfigurer repositoryRestConfigurer()
    {
        return RepositoryRestConfigurer.withConfig(config -> {
            config.exposeIdsFor(Comment.class);
            config.exposeIdsFor(Genre.class);
            config.exposeIdsFor(Author.class);
            config.exposeIdsFor(Book.class);
            config.getProjectionConfiguration().addProjection(BookDto.class);
        });
    }
}
