package com.epam.esm.service.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthException extends AuthenticationException {
    public JwtAuthException(String msg) {
        super(msg);
    }
}
