package com.epam.esm.core.repository;


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
     */
    T create(T t);

    /**
     * Update t.
     *
     * @param t the t
     * @return the t
     */
    T update(T t);

    /**
     * Delete.
     *
     * @param t the t
     */
    void delete(T t);

    /**
     * Find all list.
     *
     * @param page the page
     * @param size the size
     * @return the list
     */


}
