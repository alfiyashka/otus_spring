package ru.avalieva.otus.hw15spring.integration.service.impl;

import org.springframework.stereotype.Service;
import ru.avalieva.otus.hw15spring.integration.domain.Medicine;
import ru.avalieva.otus.hw15spring.integration.domain.MedicineItem;
import ru.avalieva.otus.hw15spring.integration.service.PharmacyService;
import ru.avalieva.otus.hw15spring.integration.repository.MedicineRepository;

import java.util.List;

@Service
public class PharmacyServiceImpl implements PharmacyService {

    private final MedicineRepository medicineRepository;

    public PharmacyServiceImpl(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }


    @Override
    public Medicine get(MedicineItem medicineItem) {
        return medicineRepository.findByName(medicineItem.getName())
                .orElse(Medicine.getEmptyMedicine());
    }

    @Override
    public boolean add(Medicine medicine) {
        try {
            return medicineRepository.save(medicine) != null;
        }
        catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Medicine> getAll(String message) {
        return medicineRepository.findAll();
    }
}
