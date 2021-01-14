package com.epam.esm.core.repository;

import com.epam.esm.core.exception.RepositoryException;

import java.util.List;

public interface BaseRepository<T> {
    T create(T t) throws RepositoryException;

    T update(T t) throws RepositoryException;

    boolean delete(long id) throws RepositoryException;

    List<T> findAll() throws RepositoryException;

}
