package com.epam.esm.core.repository;

import com.epam.esm.core.entity.User;
import com.epam.esm.core.exception.RepositoryException;



public interface UserRepository extends BaseRepository<User> {
    User findUserById(long id) throws RepositoryException;

    User findUserByLogin(String login) throws RepositoryException;

}
