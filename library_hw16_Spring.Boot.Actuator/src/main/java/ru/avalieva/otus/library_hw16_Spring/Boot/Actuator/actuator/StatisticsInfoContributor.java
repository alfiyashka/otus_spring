package ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.repository.AuthorRepository;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.repository.BookRepository;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.repository.GenreRepository;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class StatisticsInfoContributor implements InfoContributor {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    @Override
    public void contribute(Info.Builder builder) {
        Map<String, Integer> bookDetails = new HashMap<>();
        bookDetails.put("book amount", bookRepository.findAll().size());
        bookDetails.put("author amount", authorRepository.findAll().size());
        bookDetails.put("genre amount", genreRepository.findAll().size());
        builder.withDetail("statistics", bookDetails);
    }
}
