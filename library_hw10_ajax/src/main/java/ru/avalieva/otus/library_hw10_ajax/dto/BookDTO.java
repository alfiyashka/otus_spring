package ru.avalieva.otus.library_hw10_ajax.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

    private long isbn;

    private String name;

    private Integer publishingYear;

    private long authorId;

    private String authorFullName;

    private long genreId;

    private String genre;
}
