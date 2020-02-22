package ru.avalieva.com.library_hw11_webflux.dto;

import ru.avalieva.com.library_hw11_webflux.domain.Comment;

public class CommentDtoConverter {
    public static CommentDTO convert(Comment comment) {
        return new CommentDTO(comment.getComment(), comment.getId());
    }
}