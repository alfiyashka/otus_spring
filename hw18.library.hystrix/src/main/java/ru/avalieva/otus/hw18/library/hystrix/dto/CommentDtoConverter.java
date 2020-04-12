package ru.avalieva.otus.hw18.library.hystrix.dto;

import ru.avalieva.otus.hw18.library.hystrix.domain.Comment;

public class CommentDtoConverter {
    public static CommentDTO convert(Comment comment) {
        return new CommentDTO(comment.getComment(), comment.getId());
    }
}
