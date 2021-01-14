package com.epam.esm.service.services;


import com.epam.esm.service.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface BaseService<T> {
    Optional<T> create(T t) throws ServiceException;

    void update(T t) throws ServiceException;

    boolean delete(long id) throws ServiceException;

    Optional<List<T>> findAll() throws ServiceException;
}
