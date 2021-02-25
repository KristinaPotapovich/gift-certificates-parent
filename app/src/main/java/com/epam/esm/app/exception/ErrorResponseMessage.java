package com.epam.esm.app.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * Error response message.
 */
public class ErrorResponseMessage {
    private String error;
    private String message;
    private String code;

    /**
     * Instantiates a new Error response message.
     */
    public ErrorResponseMessage() {

    }

    /**
     * Instantiates a new Error response message.
     *
     * @param error     the error
     * @param message   the message
     * @param code      the code
     */
    public ErrorResponseMessage(String error, String message, String code) {
        this.error = error;
        this.message = message;
        this.code = code;
    }

    /**
     * Gets error.
     *
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * Sets error.
     *
     * @param error the error
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(String code) {
        this.code = code;
    }

}
