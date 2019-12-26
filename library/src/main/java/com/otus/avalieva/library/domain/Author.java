package com.otus.avalieva.library.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Author {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;

    public String fullName() {
        return String.format("%s %s", firstName, lastName);
    }
}
