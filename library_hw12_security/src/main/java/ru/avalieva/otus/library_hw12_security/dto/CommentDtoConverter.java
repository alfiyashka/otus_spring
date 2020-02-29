package ru.avalieva.otus.library_hw12_security.dto;

import ru.avalieva.otus.library_hw12_security.domain.Comment;

public class CommentDtoConverter {
    public static CommentDTO convert(Comment comment) {
        return new CommentDTO(comment.getComment(), comment.getId());
    }
}