package ru.avalieva.otus.library_hw09_mvc.dto;


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

    private long genreId;
}
