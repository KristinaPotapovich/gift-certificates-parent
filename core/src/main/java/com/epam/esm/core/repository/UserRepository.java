package com.epam.esm.core.repository;

import com.epam.esm.core.entity.Order;
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

    /**
     * Find all orders by user list.
     *
     * @param id   the id
     * @param page the page
     * @param size the size
     * @return the list
     */
    List<Order> getInformationAboutUsersOrders(long id, int page, int size);

    /**
     * Find all users list.
     *
     * @param page the page
     * @param size the size
     * @return the list
     */
    List<User> findAllUsers(int page, int size);
}
