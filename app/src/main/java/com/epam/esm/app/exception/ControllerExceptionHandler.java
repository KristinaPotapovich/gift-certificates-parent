package com.epam.esm.app.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.time.LocalDateTime;
import java.util.Locale;


@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String MESSAGE = ".message";
    private static final String CODE = ".code";

    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = ControllerException.class)
    public ErrorResponseMessage controllerError(ControllerException e, Locale locale) {
      ErrorResponseMessage errorResponseMessage = new ErrorResponseMessage();
      errorResponseMessage.setTimestamp(LocalDateTime.now());
      errorResponseMessage.setCode(messageSource.getMessage(e.getMessage()+CODE,
              new Object[]{},locale));
      errorResponseMessage.setError(HttpStatus.BAD_REQUEST.toString());
      errorResponseMessage.setMessage(messageSource.getMessage(e.getMessage()+MESSAGE,
              new Object[]{},locale));
      return errorResponseMessage;
    }
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({ RuntimeException.class })
    public ErrorResponseMessage handleIllegalArgumentException(RuntimeException e, Locale locale) {
        ErrorResponseMessage errorResponseMessage = new ErrorResponseMessage();
        errorResponseMessage.setTimestamp(LocalDateTime.now());
        errorResponseMessage.setCode(messageSource.getMessage(e.getMessage(),
                new Object[]{},locale));
        errorResponseMessage.setError(HttpStatus.BAD_REQUEST.toString());
        errorResponseMessage.setMessage(messageSource.getMessage(e.getMessage(),
                new Object[]{},locale));
        return errorResponseMessage;
    }
}
