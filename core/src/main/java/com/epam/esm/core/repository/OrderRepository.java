package com.epam.esm.core.repository;

import com.epam.esm.core.entity.Order;
import com.epam.esm.core.exception.RepositoryException;

import java.util.List;


/**
 * The interface Order repository.
 */
public interface OrderRepository extends BaseRepository<Order> {

    /**
     * Find order by id order.
     *
     * @param id the id
     * @return the order
     * @throws RepositoryException the repository exception
     */
    Order findOrderById(long id) throws RepositoryException;

    /**
     * Find all orders by user list.
     *
     * @param id   the id
     * @param page the page
     * @param size the size
     * @return the list
     * @throws RepositoryException the repository exception
     */
    List<Order> findAllOrdersByUser(long id, int page, int size) throws RepositoryException;


}
