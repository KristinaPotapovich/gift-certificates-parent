package com.epam.esm.core.repository.impl;

import com.epam.esm.core.entity.Order;
import com.epam.esm.core.repository.OrderRepository;
import com.epam.esm.core.repository.impl.config.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
    public void findAllOrdersByUserPositiveTest() {
        List<Order> orders = orderRepository.findAllOrdersByUser(1, 1, 5);
        assertFalse(orders.isEmpty());
        assertEquals(1, orders.get(0).getId());
    }

    @Test
    public void findAllPositiveTest() {
        List<Order> orders = orderRepository.findAllOrders(1, 5);
        assertFalse(orders.isEmpty());
    }

    @Test
    public void findOrderByIdPositiveTest() {
        Order order = orderRepository.findOrderById(1);
        assertNotNull(order);
        assertEquals(1, order.getId());
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void findOrderByIdNegativeTest() {
        orderRepository.findOrderById(0);
    }
}