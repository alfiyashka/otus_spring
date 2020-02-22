package ru.avalieva.otus.library_hw10_ajax.dto;

import ru.avalieva.otus.library_hw10_ajax.domain.Comment;

public class CommentDtoConverter {
    public static CommentDTO convert(Comment comment) {
        return new CommentDTO(comment.getComment(), comment.getId());
    }
}
