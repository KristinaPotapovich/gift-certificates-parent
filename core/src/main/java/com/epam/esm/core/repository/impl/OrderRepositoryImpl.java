package com.epam.esm.core.repository.impl;

import com.epam.esm.core.entity.Order;
import com.epam.esm.core.repository.OrderRepository;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Order repository.
 */
@Repository
public class OrderRepositoryImpl implements OrderRepository {
    @PersistenceContext
    private Session session;
    private static final String ID_ORDER = "id";

    @Override
    public Order create(Order order) {
        LocalDateTime createOrder = LocalDateTime.now();
        order.setTimeOfPurchase(createOrder);
        return (Order) session.merge(order);
    }

    @Override
    public List<Order> findAllOrdersByUser(long id, int page, int size) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> orderRoot = criteriaQuery.from(Order.class);
        criteriaQuery.select(orderRoot)
                .where(criteriaBuilder.equal(orderRoot.join("user").get("id"), id));
        return session.createQuery(criteriaQuery)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public Order update(Order order) {
        return null;
    }

    @Override
    public void delete(Order order) {

    }

    @Override
    public List<Order> findAllOrders(int page, int size) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Order> orderCriteriaQuery =
                criteriaBuilder.createQuery(Order.class);
        Root<Order> orderRoot = orderCriteriaQuery.from(Order.class);
        orderCriteriaQuery.select(orderRoot);
        return session.createQuery(orderCriteriaQuery)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public Order findOrderById(long id) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> orderRoot = criteriaQuery.from(Order.class);
        criteriaQuery.where(criteriaBuilder.equal(orderRoot.get(ID_ORDER), id));
        return session.createQuery(criteriaQuery).getSingleResult();
    }
}
