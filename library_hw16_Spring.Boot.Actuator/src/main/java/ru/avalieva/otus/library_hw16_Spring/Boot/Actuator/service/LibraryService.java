package ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.service;

import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.domain.Book;

public interface LibraryService {
    void addNewBook(Book book);
    void addBookComment(long isbn, String comment);
}
