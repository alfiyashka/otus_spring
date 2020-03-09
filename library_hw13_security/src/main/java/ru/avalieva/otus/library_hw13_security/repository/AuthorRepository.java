package ru.avalieva.otus.library_hw13_security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.avalieva.otus.library_hw13_security.domain.Author;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByFirstNameAndLastName(String firstName, String lastName);
}