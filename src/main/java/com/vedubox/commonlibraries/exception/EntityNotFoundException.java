package com.vedubox.commonlibraries.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException() {
        super("Entity not found!");
    }
    public EntityNotFoundException(String message) {
        super(message);
    }
}
