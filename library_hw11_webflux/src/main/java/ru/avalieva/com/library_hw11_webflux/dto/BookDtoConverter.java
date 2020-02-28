package ru.avalieva.com.library_hw11_webflux.dto;

import ru.avalieva.com.library_hw11_webflux.domain.Book;

public class BookDtoConverter {
    public static Book convert(BookDTO bookDTO) {
        return new Book(bookDTO.getIsbn(), bookDTO.getName(), bookDTO.getPublishingYear(), null, null);
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
