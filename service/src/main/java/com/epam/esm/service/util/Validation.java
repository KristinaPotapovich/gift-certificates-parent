package com.epam.esm.service.util;

import com.epam.esm.service.exception.ValidationException;

/**
 * The interface Validation.
 *
 * @param <T> the type parameter
 */
public interface Validation<T> {
    /**
     * Validate.
     *
     * @param t the t
     * @throws ValidationException the validation exception
     */
    void validate(T t) throws ValidationException;
}
