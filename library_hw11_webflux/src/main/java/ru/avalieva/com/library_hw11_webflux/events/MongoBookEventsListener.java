package ru.avalieva.com.library_hw11_webflux.events;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import ru.avalieva.com.library_hw11_webflux.domain.Book;
import ru.avalieva.com.library_hw11_webflux.domain.Comment;
import ru.avalieva.com.library_hw11_webflux.repository.CommentRepository;
import ru.avalieva.com.library_hw11_webflux.service.MessageService;


@Component
@RequiredArgsConstructor
public class MongoBookEventsListener extends AbstractMongoEventListener<Book>  {

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
            throw new  MongoEventsException(messageService.
                    getMessage("book.author.is.null", book.getName()));

        }
    }

    private void checkGenreExistence(Book book) {
        val genre = book.getGenre();
        if (genre == null) {
            throw new MongoEventsException(messageService.getMessage("book.genre.is.null", book.getName()));
        }
    }

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Book> event) {
        super.onBeforeDelete(event);
        val source = event.getSource();
        String id = source.get("_id").toString();
        Flux<Comment> comments = commentRepository.getCommentsOfBook(id);
        comments.flatMap(commentRepository::delete).subscribe();
    }
}
