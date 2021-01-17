package com.epam.esm.core.repository;

import com.epam.esm.core.exception.RepositoryException;

import java.util.List;

/**
 * The interface Base repository.
 *
 * @param <T> the type parameter
 */
public interface BaseRepository<T> {
    /**
     * Create t.
     *
     * @param t the t
     * @return the t
     * @throws RepositoryException the repository exception
     */
    T create(T t) throws RepositoryException;

    /**
     * Update boolean.
     *
     * @param t the t
     * @return the boolean
     * @throws RepositoryException the repository exception
     */
    boolean update(T t) throws RepositoryException;

    /**
     * Delete boolean.
     *
     * @param id the id
     * @return the boolean
     * @throws RepositoryException the repository exception
     */
    boolean delete(long id) throws RepositoryException;

    /**
     * Find all list.
     *
     * @return the list
     * @throws RepositoryException the repository exception
     */
    List<T> findAll() throws RepositoryException;

}
