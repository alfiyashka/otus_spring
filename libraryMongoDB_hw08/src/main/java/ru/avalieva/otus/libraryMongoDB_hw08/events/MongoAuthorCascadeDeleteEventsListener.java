package ru.avalieva.otus.libraryMongoDB_hw08.events;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.avalieva.otus.libraryMongoDB_hw08.domain.Author;
import ru.avalieva.otus.libraryMongoDB_hw08.domain.Book;
import ru.avalieva.otus.libraryMongoDB_hw08.repository.AuthorRepository;
import ru.avalieva.otus.libraryMongoDB_hw08.repository.BookRepository;
import ru.avalieva.otus.libraryMongoDB_hw08.service.MessageService;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MongoAuthorCascadeDeleteEventsListener extends AbstractMongoEventListener<Author> {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final MessageService messageService;


    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Author> event) {
        super.onBeforeDelete(event);
        val source = event.getSource();
        String id = source.get("_id").toString();
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new MongoEventsException(messageService.getMessage("author.not.exist", id)));
        List<Book> bookList = bookRepository.findByAuthor(author);
        if (!bookList.isEmpty()) {
            throw new MongoEventsException(messageService.getMessage("book.for.author.exist",
                    author.fullName(),
                    bookList.stream().map(Book::getName).collect(Collectors.joining(", "))));
        }
    }
}
