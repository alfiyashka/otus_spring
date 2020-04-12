package ru.otus.avaliva.hw17.library.docker.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.avaliva.hw17.library.docker.domain.Author;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByFirstNameAndLastName(String firstName, String lastName);
}