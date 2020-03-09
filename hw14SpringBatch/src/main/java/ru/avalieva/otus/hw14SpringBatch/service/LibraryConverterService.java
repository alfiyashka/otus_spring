package ru.avalieva.otus.hw14SpringBatch.service;


import ru.avalieva.otus.hw14SpringBatch.model.jpa.Author;
import ru.avalieva.otus.hw14SpringBatch.model.jpa.Book;
import ru.avalieva.otus.hw14SpringBatch.model.jpa.Comment;
import ru.avalieva.otus.hw14SpringBatch.model.jpa.Genre;
import ru.avalieva.otus.hw14SpringBatch.model.mongo.AuthorMongo;
import ru.avalieva.otus.hw14SpringBatch.model.mongo.BookMongo;
import ru.avalieva.otus.hw14SpringBatch.model.mongo.CommentMongo;
import ru.avalieva.otus.hw14SpringBatch.model.mongo.GenreMongo;


public interface LibraryConverterService {
    AuthorMongo convertToMongoAuthor(Author author);

    GenreMongo convertToMongoGenre(Genre genre);

    BookMongo convertToMongoBook(Book book);

    CommentMongo convertToMongoComment(Comment comment);
}
