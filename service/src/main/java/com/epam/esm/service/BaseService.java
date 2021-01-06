package com.epam.esm.service;


import java.util.Optional;


public interface BaseService<T> {
    Optional <T> create (T t);
    Optional <T> update (T t);
    Optional <T> delete (T t);
}
