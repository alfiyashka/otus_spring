package ru.otus.avaliva.hw17.library.docker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.avaliva.hw17.library.docker.domain.User;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
}
