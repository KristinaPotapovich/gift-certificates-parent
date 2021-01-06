package com.epam.esm.repository;

public interface BaseRepository<T> {
     T create(T t);

     T update(T t);

     void delete(T t);
}
