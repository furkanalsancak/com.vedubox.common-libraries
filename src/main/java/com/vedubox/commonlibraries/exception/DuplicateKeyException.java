package com.vedubox.commonlibraries.exception;

public class DuplicateKeyException extends RuntimeException {

    public DuplicateKeyException() {
        super("The key already exists!");
    }
    public DuplicateKeyException(String message) {
        super(message);
    }
}
