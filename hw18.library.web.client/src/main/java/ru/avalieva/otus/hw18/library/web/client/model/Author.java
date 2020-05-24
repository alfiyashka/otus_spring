package ru.avalieva.otus.hw18.library.web.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Author {
    private long id;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String email;

    public String fullName() {
        return String.format("%s %s", firstName, lastName);
    }

}
