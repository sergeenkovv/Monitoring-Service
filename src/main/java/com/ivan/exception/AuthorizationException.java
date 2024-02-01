package com.ivan.exception;

/**
 * The type Authorize exception.
 */
public class AuthorizationException extends RuntimeException {
    /**
     * Instantiates a new Authorize exception.
     *
     * @param message the message
     */
    public AuthorizationException(String message) {
        super(message);
    }
}