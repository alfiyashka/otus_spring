package ru.avalieva.otus.libraryMongoDB_hw08.events;

public class MongoEventsException extends RuntimeException {
    public MongoEventsException(String error) {
        super(error);
    }

    public MongoEventsException(String error, Throwable throwable) {
        super(error, throwable);
    }

}
