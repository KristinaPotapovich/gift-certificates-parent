package com.epam.esm.service.services;

import com.epam.esm.service.exception.ServiceException;

import java.util.Optional;

public interface BaseService<T> {
    Optional<T> create (T t) throws ServiceException;
    Optional<T> update (T t);
    boolean delete (long id);
}
