package ru.avalieva.otus.hw15spring.integration.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Recipe {
    private List<MedicineItem> medicineItems;
    private final String date;
    private final String doctorFullName;

    public boolean isValid () {
        return doctorFullName != null && !doctorFullName.isEmpty();
    }
}
