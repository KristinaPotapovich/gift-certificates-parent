package com.epam.esm.service.services;


import com.epam.esm.service.exception.ServiceException;

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
     * @throws ServiceException the service exception
     */
    Optional<T> create(T t) throws ServiceException;

    /**
     * Update.
     *
     * @param t the t
     * @throws ServiceException the service exception
     */
    void update(T t) throws ServiceException;

    /**
     * Delete boolean.
     *
     * @param id the id
     * @return the boolean
     * @throws ServiceException the service exception
     */
    void delete(long id) throws ServiceException;

    /**
     * Find all optional.
     *
     * @return the optional
     * @throws ServiceException the service exception
     */
    Optional<List<T>> findAll(int page,int size) throws ServiceException;
}
