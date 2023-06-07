package org.example.custom_exception;

public class NotFoundException extends BaseCustomException {
    public NotFoundException(String message) {
        super(message);
    }
}