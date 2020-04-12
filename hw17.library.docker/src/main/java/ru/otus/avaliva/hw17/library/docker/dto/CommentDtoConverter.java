package ru.otus.avaliva.hw17.library.docker.dto;

import ru.otus.avaliva.hw17.library.docker.domain.Comment;

public class CommentDtoConverter {
    public static CommentDTO convert(Comment comment) {
        return new CommentDTO(comment.getComment(), comment.getId());
    }
}