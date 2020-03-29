package ru.avalieva.otus.hw15spring.integration.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column(name = "PRODUCTION_DATE")
    private String productionDate;


    public static Medicine getEmptyMedicine() {
        return new Medicine(-1, null, null);
    }

    public boolean isNotEmpty () {
        return id > 0;
    }
}
