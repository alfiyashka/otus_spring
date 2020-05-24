package ru.avalieva.otus.hw18.library.hystrix.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.avalieva.otus.hw18.library.hystrix.domain.Author;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByFirstNameAndLastName(String firstName, String lastName);
}
