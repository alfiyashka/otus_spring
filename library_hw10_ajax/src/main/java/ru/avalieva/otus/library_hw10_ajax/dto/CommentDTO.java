package ru.avalieva.otus.library_hw10_ajax.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private String comment;
    private long bookIsbn;
}
