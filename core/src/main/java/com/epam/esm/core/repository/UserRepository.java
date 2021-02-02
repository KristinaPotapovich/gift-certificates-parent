package com.epam.esm.core.repository;

import com.epam.esm.core.entity.User;


/**
 * The interface User repository.
 */
public interface UserRepository extends BaseRepository<User> {
    /**
     * Find user by id user.
     *
     * @param id the id
     * @return the user
     */
    User findUserById(long id);
}
