package ru.avalieva.otus.library_hw13_security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.avalieva.otus.library_hw13_security.domain.User;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
}
