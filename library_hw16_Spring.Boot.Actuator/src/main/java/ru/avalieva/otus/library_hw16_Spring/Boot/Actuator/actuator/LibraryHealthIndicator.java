package ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.repository.AuthorRepository;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.repository.BookRepository;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.repository.GenreRepository;

@RequiredArgsConstructor
public class LibraryHealthIndicator implements HealthIndicator {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    @Override
    public Health health() {
        int errorCode = check();
        if (errorCode != 0) {
            return Health.down().withDetail("Error Code", errorCode).build();
        }
        return Health.up().build();
    }

    private int check() {
        if (bookRepository.findAll().isEmpty()
                || authorRepository.findAll().isEmpty()
                || genreRepository.findAll().isEmpty()) {
            return 1;
        }
        return 0;
    }
}


