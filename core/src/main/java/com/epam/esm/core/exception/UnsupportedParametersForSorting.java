package com.epam.esm.core.exception;

public class UnsupportedParametersForSorting extends RuntimeException{
    public UnsupportedParametersForSorting() {
    }

    public UnsupportedParametersForSorting(String message) {
        super(message);
    }

    public UnsupportedParametersForSorting(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedParametersForSorting(Throwable cause) {
        super(cause);
    }
}
