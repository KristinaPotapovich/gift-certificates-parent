package com.epam.esm.service.services;


import java.util.List;
import java.util.Optional;

/**
 * The interface Base service.
 *
 * @param <T> the type parameter
 */
public interface BaseService<T> {
    /**
     * Create optional.
     *
     * @param t the t
     * @return the optional
     */
    Optional<T> create(T t);

    /**
     * Update optional.
     *
     * @param t the t
     * @return the optional
     */
    Optional<T> update(T t);

    /**
     * Delete.
     *
     * @param id the id
     */
    void delete(long id);

}
