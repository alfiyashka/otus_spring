package ru.avalieva.otus.hw15spring.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.avalieva.otus.hw15spring.integration.domain.Medicine;

import java.util.Optional;

public interface MedicineRepository extends JpaRepository<Medicine, String> {
    Optional<Medicine> findByName(String name);
}
