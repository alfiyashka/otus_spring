package ru.avalieva.otus.libraryMongoDB_hw08.events;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.avalieva.otus.libraryMongoDB_hw08.domain.Book;
import ru.avalieva.otus.libraryMongoDB_hw08.domain.Comment;
import ru.avalieva.otus.libraryMongoDB_hw08.repository.AuthorRepository;
import ru.avalieva.otus.libraryMongoDB_hw08.repository.CommentRepository;
import ru.avalieva.otus.libraryMongoDB_hw08.repository.GenreRepository;
import ru.avalieva.otus.libraryMongoDB_hw08.service.MessageService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MongoBookEventsListener extends AbstractMongoEventListener<Book> {

    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final MessageService messageService;
    private final CommentRepository commentRepository;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Book> event) {
        super.onBeforeConvert(event);
        val book = event.getSource();
        checkAuthorExistence(book);
        checkGenreExistence(book);
    }

    private void checkAuthorExistence(Book book) {
        val author = book.getAuthor();
        if (author == null) {
            throw new MongoEventsException(messageService.getMessage("book.author.is.null", book.getIsbn()));
        }
        authorRepository.findByFirstNameAndLastName(author.getFirstName(), author.getLastName())
                .orElseThrow(() -> new MongoEventsException(messageService.getMessage("author.absent",
                        author.getFirstName(), author.getLastName())));
    }

    private void checkGenreExistence(Book book) {
        val genre = book.getGenre();
        if (genre == null) {
            throw new MongoEventsException(messageService.getMessage("book.genre.is.null", book.getIsbn()));
        }

        genreRepository.findByGenreName(genre.getGenreName())
                .orElseThrow(() -> new MongoEventsException(messageService.getMessage("genre.absent", genre.getGenreName())));
    }

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Book> event) {
        super.onBeforeDelete(event);
        val source = event.getSource();
        String id = source.get("_id").toString();
        List<Comment> comments = commentRepository.getCommentsOfBook(id);
        if (!comments.isEmpty()) {
            throw new MongoEventsException(messageService.getMessage("book.comment.exist", id));
        }
    }
}