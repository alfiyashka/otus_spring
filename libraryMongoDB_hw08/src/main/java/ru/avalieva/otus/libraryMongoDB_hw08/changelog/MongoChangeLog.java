package ru.avalieva.otus.libraryMongoDB_hw08.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.avalieva.otus.libraryMongoDB_hw08.domain.Author;
import ru.avalieva.otus.libraryMongoDB_hw08.domain.Book;
import ru.avalieva.otus.libraryMongoDB_hw08.domain.Comment;
import ru.avalieva.otus.libraryMongoDB_hw08.domain.Genre;

@ChangeLog
public class MongoChangeLog {
    private Genre genre1;
    private Genre genre2;
    private Genre genre3;
    private Genre genre4;
    private Genre genre5;
    private Genre genre6;

    private Author author1;
    private Author author2;
    private Author author3;
    private Author author4;

    private Book book1;

    @ChangeSet(order = "000", id = "dropDB", author = "avalieva", runAlways = true)
    public void dropDB(MongoDatabase database){
        database.drop();
    }

    @ChangeSet(order = "001", id = "initGenres", author = "avalieva", runAlways = true)
    public void initGenres(MongoTemplate template){
        genre1 = template.save(new Genre(null, "DRAMA"));
        genre2 = template.save(new Genre(null, "SCIENTIFIC"));
        genre3 = template.save(new Genre(null, "COMEDY"));
        genre4 = template.save(new Genre(null, "TRAGEDY"));
        genre5 = template.save(new Genre(null, "FANTASY"));
        genre6 = template.save(new Genre(null, "BIOGRATHY"));
    }


    @ChangeSet(order = "002", id = "initAuthors", author = "avalieva", runAlways = true)
    public void initAuthors(MongoTemplate template){
        author1 = template.save(new Author(null, "Dmitry", "Tolstoy", null, "dtlstoy@mail.ru"));
        author2 = template.save(new Author(null, "Alexandra", "Belova", "8912232434" , "abelova@yandex.ru"));
        author3 = template.save(new Author(null, "John", "Tipping", "23434354", "jtibbing@gmail.ru"));
        author4 = template.save(new Author(null, "Michail", "Fry", "234354565", "michailbook@gmail.ru"));
    }

    @ChangeSet(order = "003", id = "initBooks", author = "avalieva", runAlways = true)
    public void initBooks(MongoTemplate template){
        book1 = template.save(new Book(null, "Vetra", 1991, author1, genre4));
        template.save(new Book(null, "Human Anatomy", 2018, author3 , genre2));
        template.save(new Book(null, "My Life", 2013, author4, genre6));
        template.save(new Book(null, "While the river flows", 2019, author2, genre1));
        template.save(new Book(null, "Joe Adventures", 1996, author1, genre5));
        template.save(new Book(null, "Vetra 2", 1998, author1, genre4));
    }

    @ChangeSet(order = "004", id = "initComments", author = "avalieva", runAlways = true)
    public void initComments(MongoTemplate template){
        template.save(new Comment(null, "Interesting", book1));
        template.save(new Comment(null, "Read 3 times", book1));
    }
}
