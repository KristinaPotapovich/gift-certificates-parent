package com.epam.esm.service.exception;

public class ValidationException extends Exception {
    private String argument;

    public ValidationException() {
    }

    public ValidationException(String message,String argument) {
        super(message);
        this.argument = argument;
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }
}
