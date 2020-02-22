package ru.avalieva.com.library_hw11_webflux.events;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import ru.avalieva.com.library_hw11_webflux.domain.Comment;
import ru.avalieva.com.library_hw11_webflux.service.MessageService;

@Component
@RequiredArgsConstructor
public class MongoCommentSaveEventsListener extends AbstractMongoEventListener<Comment>
{
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
    }
}

