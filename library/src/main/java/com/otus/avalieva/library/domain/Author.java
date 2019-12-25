package com.otus.avalieva.library.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Author {
    Long id;
    String firstName;
    String lastName;
    String phoneNumber;
    String email;

    public String fullName() {
        return String.format("%s %s", firstName, lastName);
    }
}
