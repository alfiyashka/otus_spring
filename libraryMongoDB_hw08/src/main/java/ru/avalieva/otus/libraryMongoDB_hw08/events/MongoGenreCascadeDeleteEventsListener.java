package ru.avalieva.otus.libraryMongoDB_hw08.events;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.avalieva.otus.libraryMongoDB_hw08.domain.Book;
import ru.avalieva.otus.libraryMongoDB_hw08.domain.Genre;
import ru.avalieva.otus.libraryMongoDB_hw08.repository.BookRepository;
import ru.avalieva.otus.libraryMongoDB_hw08.repository.GenreRepository;
import ru.avalieva.otus.libraryMongoDB_hw08.service.MessageService;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MongoGenreCascadeDeleteEventsListener extends AbstractMongoEventListener<Genre> {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final MessageService messageService;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Genre> event) {
        super.onBeforeDelete(event);
        val source = event.getSource();
        String id = source.get("_id").toString();
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new MongoEventsException(messageService.getMessage("genre.not.exist", id)));
        List<Book> bookList = bookRepository.findByGenre(genre);
        if (!bookList.isEmpty()) {
            throw new MongoEventsException(messageService.getMessage("book.for.genre.exist",
                   genre, bookList.stream().map(Book::getName).collect(Collectors.joining(", "))));

        }
    }
}

