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


    List<Order> findAllOrders(int page, int size);
}
