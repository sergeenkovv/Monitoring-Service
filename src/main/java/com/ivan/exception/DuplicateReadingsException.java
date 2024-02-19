package com.ivan.exception;

/**
 * the type duplicate readings exception
 */
public class DuplicateReadingsException extends RuntimeException {
    /**
     * instantiates a new  duplicate readings exception.
     *
     * @param message
     */
    public DuplicateReadingsException(String message) {
        super(message);
    }
}