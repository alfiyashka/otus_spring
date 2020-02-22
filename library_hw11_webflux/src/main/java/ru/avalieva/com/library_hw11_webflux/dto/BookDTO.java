package ru.avalieva.com.library_hw11_webflux.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

    private String isbn;

    private String name;

    private Integer publishingYear;

    private String authorId;

    private String authorFullName;

    private String genreId;

    private String genre;
}
