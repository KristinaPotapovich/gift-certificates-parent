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
     * Update optional.
     *
     * @param t the t
     * @return the optional
     * @throws ServiceException the service exception
     */
    Optional<T> update(T t) throws ServiceException;

    /**
     * Delete.
     *
     * @param id the id
     * @throws ServiceException the service exception
     */
    void delete(long id) throws ServiceException;

    /**
     * Find all list.
     *
     * @param page the page
     * @param size the size
     * @return the list
     * @throws ServiceException the service exception
     */
    List<T> findAll(int page, int size) throws ServiceException;
}
