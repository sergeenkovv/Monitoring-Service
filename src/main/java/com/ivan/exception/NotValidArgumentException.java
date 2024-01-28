package com.ivan.exception;

public class NotValidArgumentException extends RuntimeException {
    /**
     * Instantiates a new Not valid argument exception.
     *
     * @param message the message
     */
    public NotValidArgumentException(String message) {
        super(message);
    }
}