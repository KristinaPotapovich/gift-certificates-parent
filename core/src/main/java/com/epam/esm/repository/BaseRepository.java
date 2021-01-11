package com.epam.esm.repository;

import com.epam.esm.entity.Tag;

public interface BaseRepository<T> {
     T create(T t);

     void update(T t);

     void delete(long id);
}
