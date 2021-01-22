package com.epam.esm.core.repository.impl;


import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.exception.RepositoryException;
import com.epam.esm.core.repository.GiftCertificateRepository;
import com.epam.esm.core.repository.specification.OrderBySpecification;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The type Gift certificate repository.
 */
@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    @PersistenceContext
    private Session session;
    private static final String CREATE_CERTIFICATE_FAIL = "giftCertificate_create_fail";
    private static final String UPDATE_CERTIFICATE_FAIL = "giftCertificate_update_fail";
    private static final String DELETE_CERTIFICATE_FAIL = "giftCertificate_delete_fail";
    private static final String FIND_ALL_CERTIFICATES_FAIL = "giftCertificate_find_all_certificates_fail";
    private static final String FIND_CERTIFICATE_BY_PARAM_FAIL = "giftCertificate_find_certificate_by_param_fail";
    private static final String SORT_CERTIFICATE_FAIL = "giftCertificate_sort_certificate_fail";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String ID_CERTIFICATE = "id";


    @Override
    public GiftCertificate create(GiftCertificate giftCertificate) throws RepositoryException {
        try {
            LocalDateTime createDate = LocalDateTime.now();
            giftCertificate.setCreateDate(createDate);
            session.persist(giftCertificate);
        } catch (DataAccessException e) {
            throw new RepositoryException(CREATE_CERTIFICATE_FAIL);
        }
        return giftCertificate;
    }

    @Override
    public void update(GiftCertificate giftCertificate) throws RepositoryException {
        try {
            LocalDateTime lastUpdateDate = LocalDateTime.now();
            giftCertificate.setLastUpdateDate(lastUpdateDate);
            session.merge(giftCertificate);
        } catch (DataAccessException e) {
            throw new RepositoryException(UPDATE_CERTIFICATE_FAIL);
        }
    }

    @Override
    public void delete(GiftCertificate giftCertificate) throws RepositoryException {
        try {
            session.remove(giftCertificate);
        } catch (DataAccessException e) {
            throw new RepositoryException(DELETE_CERTIFICATE_FAIL);
        }
    }

    @Override
    public List<GiftCertificate> findAll() throws RepositoryException {
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<GiftCertificate> giftCertificateCriteriaQuery =
                    criteriaBuilder.createQuery(GiftCertificate.class);
            Root<GiftCertificate> giftCertificateRoot = giftCertificateCriteriaQuery.from(GiftCertificate.class);
            giftCertificateCriteriaQuery.select(giftCertificateRoot);
            return session.createQuery(giftCertificateCriteriaQuery).getResultList();
        } catch (DataAccessException e) {
            throw new RepositoryException(FIND_ALL_CERTIFICATES_FAIL);
        }
    }

    @Override
    public List<GiftCertificate> findCertificateByParam(String param) throws RepositoryException {
        List<GiftCertificate> giftCertificates;
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
            Root<GiftCertificate> giftCertificateRoot = criteriaQuery.from(GiftCertificate.class);
            Predicate predicateForName = criteriaBuilder.like(giftCertificateRoot.get(NAME), "%" + param + "%");
            Predicate predicateForDescription = criteriaBuilder
                    .like(giftCertificateRoot.get(DESCRIPTION), "%" + param + "%");
            Predicate finalPredicate
                    = criteriaBuilder.and(predicateForName, predicateForDescription);
            criteriaQuery.select(giftCertificateRoot).distinct(true)
                    .where(finalPredicate);
            giftCertificates = session.createQuery(criteriaQuery).getResultList();
        } catch (DataAccessException e) {
            throw new RepositoryException(FIND_CERTIFICATE_BY_PARAM_FAIL);
        }
        return giftCertificates;
    }

    @Override
    public GiftCertificate findCertificateById(long id) throws RepositoryException {
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
            Root<GiftCertificate> giftCertificateRoot = criteriaQuery.from(GiftCertificate.class);
            criteriaQuery.where(criteriaBuilder.equal(giftCertificateRoot.get(ID_CERTIFICATE), id));
            return session.createQuery(criteriaQuery).getSingleResult();
        } catch (DataAccessException e) {
            throw new RepositoryException(FIND_CERTIFICATE_BY_PARAM_FAIL);
        }
    }

    public List<GiftCertificate> searchAllCertificatesByTagName(String tagName) throws RepositoryException {
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
            Root<GiftCertificate> giftCertificateRoot = criteriaQuery.from(GiftCertificate.class);
            criteriaQuery.select(giftCertificateRoot)
                    .where(criteriaBuilder.equal(giftCertificateRoot.join("tags").get("name"), tagName));
            return session.createQuery(criteriaQuery).getResultList();
        } catch (DataAccessException e) {
            throw new RepositoryException(FIND_CERTIFICATE_BY_PARAM_FAIL);
        }
    }

    public List<GiftCertificate> sortByParam(OrderBySpecification orderBySpecification) throws
            RepositoryException {
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<GiftCertificate> giftCertificateCriteriaQuery =
                    criteriaBuilder.createQuery(GiftCertificate.class);
            Root<GiftCertificate> giftCertificateRoot = giftCertificateCriteriaQuery.from(GiftCertificate.class);
            giftCertificateCriteriaQuery.select(giftCertificateRoot);
            if (orderBySpecification != null) {
              orderBySpecification.buildQuery(giftCertificateCriteriaQuery, criteriaBuilder, giftCertificateRoot);
            }
            return session.createQuery(giftCertificateCriteriaQuery).getResultList();
        } catch (DataAccessException e) {
            throw new RepositoryException(SORT_CERTIFICATE_FAIL);
        }
    }
}

