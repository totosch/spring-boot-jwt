package com.rsch.exception;

public class FileEntityNotFoundException extends RuntimeException {
    public FileEntityNotFoundException(String message) {
        super(message);
    }
}