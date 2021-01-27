package com.epam.esm.core.repository;

import com.epam.esm.core.entity.Order;
import com.epam.esm.core.exception.RepositoryException;

import java.util.List;


public interface OrderRepository extends BaseRepository<Order> {

    Order findOrderById(long id) throws RepositoryException;

    List<Order> findAllOrdersByUser(long id, int page, int size) throws RepositoryException;
}
