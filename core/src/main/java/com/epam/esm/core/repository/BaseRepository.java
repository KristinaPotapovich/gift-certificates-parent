package com.epam.esm.core.repository;

import com.epam.esm.core.exception.RepositoryException;

import java.util.List;

public interface BaseRepository<T> {
    T create(T t) throws RepositoryException;

    T update(T t) throws RepositoryException;

    void delete(T t) throws RepositoryException;

    List<T> findAll(int page,int size) throws RepositoryException;

}
