package ru.avalieva.otus.hw18.library.hystrix.dto;

import ru.avalieva.otus.hw18.library.hystrix.domain.Book;

public class BookDtoConverter {
    public static Book convert(BookDTO bookDTO) {
        return new Book(bookDTO.getIsbn(), bookDTO.getName(), bookDTO.getPublishingYear(), 1L, 1L);
    }

    public static BookDTO convert(Book book) {
        return new BookDTO(book.getIsbn(),
                book.getName(),
                book.getPublishingYear(),
                book.getAuthor().getId(),
                book.getAuthor().fullName(),
                book.getGenre().getId(),
                book.genreName());
    }
}
