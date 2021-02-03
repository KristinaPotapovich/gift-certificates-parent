package com.epam.esm.core.repository;

import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.entity.User;

import java.util.List;


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
    List<User> findAllUsers(int page, int size);
}
