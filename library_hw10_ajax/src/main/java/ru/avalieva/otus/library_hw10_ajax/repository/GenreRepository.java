package ru.avalieva.otus.library_hw10_ajax.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.avalieva.otus.library_hw10_ajax.domain.Genre;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByGenreName(String name);
}
