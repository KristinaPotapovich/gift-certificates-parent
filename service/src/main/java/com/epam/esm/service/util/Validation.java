package com.epam.esm.service.util;

import com.epam.esm.service.exception.ValidationException;

public interface Validation<T> {
    void validate(T t) throws ValidationException;
}
