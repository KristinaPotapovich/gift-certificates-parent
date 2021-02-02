package com.epam.esm.core.repository;

import com.epam.esm.core.entity.Order;

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
     */
    Order findOrderById(long id);

    /**
     * Find all orders by user list.
     *
     * @param id   the id
     * @param page the page
     * @param size the size
     * @return the list
     */
    List<Order> findAllOrdersByUser(long id, int page, int size);


}
