package ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.domain.Author;

import javax.transaction.Transactional;
import java.util.List;
@Transactional
@RepositoryRestResource(path = "author")
public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<Author> findAll();
}