package org.example.custom_exception;

public class BaseCustomException extends RuntimeException {
    private static final boolean ENABLE_SUPPRESSION = true;

    private static final boolean WRITABLE_STACK_TRACE = false;

    public BaseCustomException(String message) {
        super(message, null, ENABLE_SUPPRESSION, WRITABLE_STACK_TRACE);
    }
}
