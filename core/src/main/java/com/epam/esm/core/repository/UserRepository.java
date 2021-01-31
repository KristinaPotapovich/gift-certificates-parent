package com.epam.esm.core.repository;

import com.epam.esm.core.entity.User;
import com.epam.esm.core.exception.RepositoryException;


/**
 * The interface User repository.
 */
public interface UserRepository extends BaseRepository<User> {
    /**
     * Find user by id user.
     *
     * @param id the id
     * @return the user
     * @throws RepositoryException the repository exception
     */
    User findUserById(long id) throws RepositoryException;
}
