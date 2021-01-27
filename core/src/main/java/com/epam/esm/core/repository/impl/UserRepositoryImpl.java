package com.epam.esm.core.repository.impl;

import com.epam.esm.core.entity.User;
import com.epam.esm.core.exception.RepositoryException;
import com.epam.esm.core.repository.UserRepository;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private static final String FIND_ALL_USERS_FAIL = "user_find_all";
    private static final String FIND_USER_BY_ID = "user_find_by_id";
    private static final String FIND_USER_BY_LOGIN = "user_find_by_login";


    @PersistenceContext
    private Session session;


    @Override
    public User create(User user) throws RepositoryException {
        session.persist(user);
        return user;
    }

    @Override
    public User update(User user) throws RepositoryException {
        return null;
    }

    @Override
    public void delete(User user) throws RepositoryException {

    }

    @Override
    public List<User> findAll(int page, int size) throws RepositoryException {
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<User> userCriteriaQuery =
                    criteriaBuilder.createQuery(User.class);
            Root<User> userRoot = userCriteriaQuery.from(User.class);
            userCriteriaQuery.select(userRoot);
            return session.createQuery(userCriteriaQuery)
                    .setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .getResultList();
        } catch (DataAccessException e) {
            throw new RepositoryException(FIND_ALL_USERS_FAIL);
        }
    }

    @Override
    public User findUserById(long id) throws RepositoryException {
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
            Root<User> userRoot = criteriaQuery.from(User.class);
            criteriaQuery.where(criteriaBuilder.equal(userRoot.get("id"), id));
            return session.createQuery(criteriaQuery).getSingleResult();
        } catch (DataAccessException e) {
            throw new RepositoryException(FIND_USER_BY_ID);
        }
    }
}
