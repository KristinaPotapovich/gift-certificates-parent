package com.epam.esm.core.repository;

import com.epam.esm.core.entity.Order;
import com.epam.esm.core.entity.User;
import com.epam.esm.core.exception.RepositoryException;

import java.util.List;


public interface UserRepository extends BaseRepository<User> {
    User findUserById(long id) throws RepositoryException;
}
