package com.epam.esm.core.repository.impl;

import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.entity.Order;
import com.epam.esm.core.exception.RepositoryException;
import com.epam.esm.core.repository.OrderRepository;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class OrderRepositoryImpl implements OrderRepository {
    @PersistenceContext
    private Session session;
    private static final String CREATE_ORDER_FAIL = "order_create_fail";


    @Override
    public Order create(Order order) throws RepositoryException {
        try {
            LocalDateTime createOrder = LocalDateTime.now();
            order.setTimeOfPurchase(createOrder);
            session.merge(order);
        } catch (DataAccessException e) {
            throw new RepositoryException(CREATE_ORDER_FAIL);
        }
        return order;
    }

    @Override
    public void update(Order order) throws RepositoryException {

    }

    @Override
    public void delete(Order order) throws RepositoryException {

    }

    @Override
    public List<Order> findAll() throws RepositoryException {
        return null;
    }
    public List<Order> findAllOrdersByUser(long id) throws RepositoryException {
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
            Root<Order> orderRoot = criteriaQuery.from(Order.class);
            criteriaQuery.select(orderRoot)
                    .where(criteriaBuilder.equal(orderRoot.join("user").get("id"), id));
            return session.createQuery(criteriaQuery).getResultList();
        } catch (DataAccessException e) {
            throw new RepositoryException();
        }
    }
}
