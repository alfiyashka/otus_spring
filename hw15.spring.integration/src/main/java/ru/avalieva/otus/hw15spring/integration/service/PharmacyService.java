package ru.avalieva.otus.hw15spring.integration.service;

import ru.avalieva.otus.hw15spring.integration.domain.Medicine;
import ru.avalieva.otus.hw15spring.integration.domain.MedicineItem;

import java.util.List;

public interface PharmacyService {

    Medicine get(MedicineItem medicineItem);

    boolean add(Medicine medicine);

    List<Medicine> getAll(String message);
}
