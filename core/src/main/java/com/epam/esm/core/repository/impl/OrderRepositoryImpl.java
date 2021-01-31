package com.epam.esm.core.repository.impl;

import com.epam.esm.core.entity.Order;
import com.epam.esm.core.exception.RepositoryException;
import com.epam.esm.core.repository.OrderRepository;
import org.hibernate.Session;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
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
    private static final String CREATE_ORDER_FAIL_MESSAGE = "order_create_fail";
    private static final String FIND_ORDER_BY_ID_FAIL_MESSAGE = "order_find_by_id";
    private static final String ID_ORDER = "id";
    private static final String FIND_ALL_ORDERS_FAIL_MESSAGE = "find_all_orders_fail";
    private static final String FIND_ALL_ORDER_BY_USER_FAIL_MESSAGE = "order_find_all_by_user";

    @Override
    public Order create(Order order) throws RepositoryException {
        try {
            LocalDateTime createOrder = LocalDateTime.now();
            order.setTimeOfPurchase(createOrder);
            return (Order) session.merge(order);
        } catch (DataIntegrityViolationException e) {
            throw new RepositoryException(CREATE_ORDER_FAIL_MESSAGE);
        }
    }

    @Override
    public List<Order> findAllOrdersByUser(long id, int page, int size) throws RepositoryException {
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
            Root<Order> orderRoot = criteriaQuery.from(Order.class);
            criteriaQuery.select(orderRoot)
                    .where(criteriaBuilder.equal(orderRoot.join("user").get("id"), id));
            return session.createQuery(criteriaQuery)
                    .setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .getResultList();
        } catch (NoResultException e) {
            throw new RepositoryException(FIND_ALL_ORDER_BY_USER_FAIL_MESSAGE);
        }
    }

    @Override
    public Order update(Order order) throws RepositoryException {
        return null;
    }

    @Override
    public void delete(Order order) throws RepositoryException {

    }

    @Override
    public List<Order> findAll(int page, int size) throws RepositoryException {
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Order> orderCriteriaQuery =
                    criteriaBuilder.createQuery(Order.class);
            Root<Order> orderRoot = orderCriteriaQuery.from(Order.class);
            orderCriteriaQuery.select(orderRoot);
            return session.createQuery(orderCriteriaQuery)
                    .setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .getResultList();
        } catch (NoResultException e) {
            throw new RepositoryException(FIND_ALL_ORDERS_FAIL_MESSAGE);
        }
    }

    @Override
    public Order findOrderById(long id) throws RepositoryException {
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
            Root<Order> orderRoot = criteriaQuery.from(Order.class);
            criteriaQuery.where(criteriaBuilder.equal(orderRoot.get(ID_ORDER), id));
            return session.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
            throw new RepositoryException(FIND_ORDER_BY_ID_FAIL_MESSAGE);
        }
    }
}
