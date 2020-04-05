package ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.projection;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.domain.Book;

@Projection(
        name = "bookDto",
        types = { Book.class })
public interface BookDto {
    @Value("#{target.isbn}")
    Long getIsbn();

    String getName();

    Integer getPublishingYear();

    @Value("#{target.author.id}")
    long getAuthorId();

    @Value("#{target.author.fullName()}")
    String getAuthorFullName();

    @Value("#{target.author.id}")
    long getGenreId();

    @Value("#{target.genre.genreName}")
    String getGenreName();
}

