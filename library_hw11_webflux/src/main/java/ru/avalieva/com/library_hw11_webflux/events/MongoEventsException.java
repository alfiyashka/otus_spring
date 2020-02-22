package ru.avalieva.com.library_hw11_webflux.events;

public class MongoEventsException extends RuntimeException {
    public MongoEventsException(String error) {
        super(error);
    }

    public MongoEventsException(String error, Throwable throwable) {
        super(error, throwable);
    }

}
