package com.epam.esm.core.exception;

public class RepositoryException extends Exception {
    private String argument;
    public RepositoryException() {
    }

    public RepositoryException(String message) {
        super(message);
    }

    public RepositoryException(String message, String argument) {
        super(message);
        this.argument = argument;
    }

    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepositoryException(Throwable cause) {
        super(cause);
    }
}
