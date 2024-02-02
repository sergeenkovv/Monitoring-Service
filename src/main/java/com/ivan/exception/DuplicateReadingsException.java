package com.ivan.exception;

public class DuplicateReadingsException extends RuntimeException {

    public DuplicateReadingsException(String message) {
        super(message);
    }
}