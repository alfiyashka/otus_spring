package ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.domain.Genre;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@RepositoryRestResource(path = "genre")
public interface GenreRepository extends JpaRepository<Genre, Long> {
    List<Genre> findAll();
}