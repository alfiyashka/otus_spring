package ru.avalieva.otus.libraryMongoDB_hw08.events;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import ru.avalieva.otus.libraryMongoDB_hw08.domain.Comment;
import ru.avalieva.otus.libraryMongoDB_hw08.repository.BookRepository;
import ru.avalieva.otus.libraryMongoDB_hw08.service.MessageService;

@Component
@RequiredArgsConstructor
public class MongoCommentSaveEventsListener extends AbstractMongoEventListener<Comment>
{

    private final BookRepository bookRepository;
    private final MessageService messageService;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Comment> event) {
        super.onBeforeConvert(event);
        val comment = event.getSource();
        checkBookExistence(comment);
    }

    private void checkBookExistence(Comment comment) {
        val commentBook = comment.getBook();
        if (commentBook == null) {
            throw new MongoEventsException(messageService.getMessage("comment.book.absent", comment.getId()));
        }
        bookRepository.findById(commentBook.getIsbn())
                .orElseThrow(() -> new MongoEventsException(messageService.getMessage("book.id.not.found",
                        commentBook.getIsbn())));
    }
}
