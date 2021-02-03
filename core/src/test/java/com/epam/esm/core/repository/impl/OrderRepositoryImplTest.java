package com.epam.esm.core.repository.impl;

import com.epam.esm.core.entity.Order;
import com.epam.esm.core.exception.RepositoryException;
import com.epam.esm.core.repository.OrderRepository;
import com.epam.esm.core.repository.impl.config.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@Component
@Transactional
@Sql({"classpath:drop-data-base.sql", "classpath:gift-certificates-parent.sql", "classpath:init-data_test.sql"})
@ContextConfiguration(classes = TestConfig.class)
public class OrderRepositoryImplTest {
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void findAllOrdersByUser() throws RepositoryException {
        List<Order> orders = orderRepository.findAllOrdersByUser(1, 1, 5);
        assertFalse(orders.isEmpty());
    }

    @Test
    public void findAll() throws RepositoryException {
        List<Order> orders = orderRepository.findAllOrders(1, 5);
        assertFalse(orders.isEmpty());
    }

    @Test
    public void findOrderById() throws RepositoryException {
        Order order = orderRepository.findOrderById(1);
        assertNotNull(order);
    }
}