package com.epam.esm.core.repository;

public interface BaseRepository<T> {
     T create(T t);

     void update(T t);

     void delete(long id);
}
