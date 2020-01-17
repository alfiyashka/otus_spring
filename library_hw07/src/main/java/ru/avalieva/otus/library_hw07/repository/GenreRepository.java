package ru.avalieva.otus.library_hw07.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.avalieva.otus.library_hw07.domain.Genre;

import javax.transaction.Transactional;
import java.util.Optional;


@Transactional
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByGenreName(String name);
}
