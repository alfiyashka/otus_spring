package ru.avalieva.otus.hw18.library.hystrix.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.avalieva.otus.hw18.library.hystrix.domain.Genre;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByGenreName(String name);
}
