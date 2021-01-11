package com.epam.esm.service;

import com.epam.esm.exception.ServiceException;

import java.util.Optional;

public interface BaseService<T> {
    Optional<T> create (T t) throws ServiceException;
    Optional<T> update (T t);
    boolean delete (long id);
}
