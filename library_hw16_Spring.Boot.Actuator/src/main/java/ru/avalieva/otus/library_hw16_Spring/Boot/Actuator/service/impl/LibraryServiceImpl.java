package ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.service.impl;


import org.springframework.stereotype.Service;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.domain.Book;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.domain.Comment;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.repository.AuthorRepository;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.repository.BookRepository;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.repository.CommentRepository;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.repository.GenreRepository;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.service.LibraryService;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.service.MessageService;


@Service
public class LibraryServiceImpl implements LibraryService {

    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;
    private final MessageService messageService;

    public LibraryServiceImpl(AuthorRepository authorRepository,
                              GenreRepository genreRepository,
                              BookRepository bookRepository,
                              CommentRepository commentRepository,
                              MessageService messageService) {
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
        this.messageService = messageService;
        this.commentRepository = commentRepository;
    }

    @Override
    public void addNewBook(Book book) {
        try {
            bookRepository.save(book);
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("book.add.error",
                    book.getName(), e.getMessage()), e);
        }
    }

    @Override
    public void addBookComment(long isbn, String comment) {
        try {
            Book book = bookRepository.findById(isbn)
                    .orElseThrow(()-> new LibraryException(messageService.getMessage("book.id.not.found", isbn)));
            Comment newComment = new Comment(0, comment, book);
            commentRepository.save(newComment);
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("add.comment.error",
                    isbn, e.getMessage()), e);
        }
    }

}


