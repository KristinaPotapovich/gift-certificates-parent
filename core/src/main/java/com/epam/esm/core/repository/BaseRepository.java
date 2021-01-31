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
     * Update t.
     *
     * @param t the t
     * @return the t
     * @throws RepositoryException the repository exception
     */
    T update(T t) throws RepositoryException;

    /**
     * Delete.
     *
     * @param t the t
     * @throws RepositoryException the repository exception
     */
    void delete(T t) throws RepositoryException;

    /**
     * Find all list.
     *
     * @param page the page
     * @param size the size
     * @return the list
     * @throws RepositoryException the repository exception
     */
    List<T> findAll(int page, int size) throws RepositoryException;

}
