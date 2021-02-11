package com.epam.esm.app.exception;

import com.epam.esm.core.exception.UnsupportedParametersForSorting;
import com.epam.esm.service.exception.JwtAuthException;
import com.epam.esm.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


/**
 * Controller exception handler.
 */
@RestControllerAdvice
public class ControllerExceptionHandler {
    private static final String MESSAGE = ".message";
    private static final String CODE = ".code";
    private MessageSource messageSource;
    private ErrorResponseMessage errorResponseMessage = new ErrorResponseMessage();
    private static final String NO_RESULT_MESSAGE = "no_result";
    private static final String TAG_WITHOUT_ID_MESSAGE = "tag_without_id";
    private static final String METHOD_NOT_ALLOWED_MESSAGE = "method_not_allowed";

    /**
     * Instantiates a new Controller exception handler.
     *
     * @param messageSource the message source
     */
    @Autowired
    public ControllerExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Error by Controller Exception.
     *
     * @param e      e from exception
     * @param locale locale
     * @return error response message
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = UnsupportedParametersForSorting.class)
    public ErrorResponseMessage controllerError(UnsupportedParametersForSorting e, Locale locale) {
        errorResponseMessage.setTimestamp(LocalDateTime.now());
        errorResponseMessage.setCode(messageSource.getMessage(e.getMessage() + CODE,
                new Object[]{}, locale));
        errorResponseMessage.setError(HttpStatus.BAD_REQUEST.toString());
        errorResponseMessage.setMessage(messageSource.getMessage(e.getMessage() + MESSAGE,
                new Object[]{}, locale));
        return errorResponseMessage;
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public List<ErrorResponseMessage> controllerErrorMessage(ConstraintViolationException e, Locale locale) {
        List<String> messages = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        return messages.stream()
                .map(message -> {
                    errorResponseMessage = new ErrorResponseMessage();
                    errorResponseMessage.setCode(messageSource.getMessage(message + CODE,
                            new Object[]{}, locale));
                    errorResponseMessage.setError(HttpStatus.BAD_REQUEST.toString());
                    errorResponseMessage.setTimestamp(LocalDateTime.now());
                    errorResponseMessage.setMessage(messageSource.getMessage(message + MESSAGE,
                            new Object[]{}, locale));
                    return errorResponseMessage;
                })
                .collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = ServiceException.class)
    public ErrorResponseMessage controllerError(ServiceException e, Locale locale) {
        errorResponseMessage.setTimestamp(LocalDateTime.now());
        errorResponseMessage.setCode(messageSource.getMessage(e.getMessage() + CODE,
                new Object[]{}, locale));
        errorResponseMessage.setError(HttpStatus.BAD_REQUEST.toString());
        errorResponseMessage.setMessage(messageSource.getMessage(e.getMessage() + MESSAGE,
                new Object[]{}, locale));
        return errorResponseMessage;
    }
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = JwtAuthException.class)
    public ErrorResponseMessage controllerError(JwtAuthException e, Locale locale) {
        errorResponseMessage.setTimestamp(LocalDateTime.now());
        errorResponseMessage.setCode(messageSource.getMessage(e.getMessage() + CODE,
                new Object[]{}, locale));
        errorResponseMessage.setError(HttpStatus.BAD_REQUEST.toString());
        errorResponseMessage.setMessage(messageSource.getMessage(e.getMessage() + MESSAGE,
                new Object[]{}, locale));
        return errorResponseMessage;
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ErrorResponseMessage controllerErrorForMethod(Locale locale) {
        errorResponseMessage.setTimestamp(LocalDateTime.now());
        errorResponseMessage.setCode(messageSource.getMessage(METHOD_NOT_ALLOWED_MESSAGE + CODE,
                new Object[]{}, locale));
        errorResponseMessage.setError(HttpStatus.METHOD_NOT_ALLOWED.toString());
        errorResponseMessage.setMessage(messageSource.getMessage(METHOD_NOT_ALLOWED_MESSAGE + MESSAGE,
                new Object[]{}, locale));
        return errorResponseMessage;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = EmptyResultDataAccessException.class)
    public ErrorResponseMessage controllerError(Locale locale) {
        errorResponseMessage.setTimestamp(LocalDateTime.now());
        errorResponseMessage.setCode(messageSource.getMessage(NO_RESULT_MESSAGE + CODE,
                new Object[]{}, locale));
        errorResponseMessage.setError(HttpStatus.NOT_FOUND.toString());
        errorResponseMessage.setMessage(messageSource.getMessage(NO_RESULT_MESSAGE + MESSAGE,
                new Object[]{}, locale));
        return errorResponseMessage;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ErrorResponseMessage handleError(Locale locale) {
        errorResponseMessage.setTimestamp(LocalDateTime.now());
        errorResponseMessage.setCode(messageSource.getMessage(TAG_WITHOUT_ID_MESSAGE + CODE,
                new Object[]{}, locale));
        errorResponseMessage.setError(HttpStatus.BAD_REQUEST.toString());
        errorResponseMessage.setMessage(messageSource.getMessage(TAG_WITHOUT_ID_MESSAGE + MESSAGE,
                new Object[]{}, locale));
        return errorResponseMessage;
    }

    /**
     * Handle illegal argument exception error response message.
     *
     * @param e      e from RuntimeException
     * @param locale locale
     * @return error response message
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({RuntimeException.class})
    public ErrorResponseMessage handleIllegalArgumentException(RuntimeException e, Locale locale) {
        errorResponseMessage.setTimestamp(LocalDateTime.now());
        errorResponseMessage.setCode(messageSource.getMessage(e.getMessage(),
                new Object[]{}, locale));
        errorResponseMessage.setError(HttpStatus.BAD_REQUEST.toString());
        errorResponseMessage.setMessage(messageSource.getMessage(e.getMessage(),
                new Object[]{}, locale));
        return errorResponseMessage;
    }
}
