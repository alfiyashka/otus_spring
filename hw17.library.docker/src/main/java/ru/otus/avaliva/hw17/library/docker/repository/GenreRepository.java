package ru.otus.avaliva.hw17.library.docker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.avaliva.hw17.library.docker.domain.Genre;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByGenreName(String name);
}
