package com.epam.esm.core.repository.impl;


import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.exception.RepositoryException;
import com.epam.esm.core.repository.GiftCertificateRepository;
import com.epam.esm.core.repository.specification.SortingSpecification;
import org.hibernate.Session;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Gift certificate repository.
 */
@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    @PersistenceContext
    private Session session;
    private static final String CREATE_CERTIFICATE_FAIL_MESSAGE = "giftCertificate_create_fail";
    private static final String UPDATE_CERTIFICATE_FAIL_MESSAGE = "giftCertificate_update_fail";
    private static final String DELETE_CERTIFICATE_FAIL_MESSAGE = "giftCertificate_delete_fail";
    private static final String FIND_ALL_CERTIFICATES_FAIL_MESSAGE = "giftCertificate_find_all_certificates_fail";
    private static final String FIND_CERTIFICATE_BY_PARAM_FAIL_MESSAGE = "giftCertificate_find_certificate_by_param_fail";
    private static final String SORT_CERTIFICATE_FAIL_MESSAGE = "giftCertificate_sort_certificate_fail";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String ID_CERTIFICATE = "id";
    private static final String TAGS = "tags";


    @Override
    public GiftCertificate create(GiftCertificate giftCertificate) throws RepositoryException {
        try {
            LocalDateTime createDate = LocalDateTime.now();
            giftCertificate.setCreateDate(createDate);
            return (GiftCertificate) session.merge(giftCertificate);
        } catch (DataIntegrityViolationException e) {
            throw new RepositoryException(CREATE_CERTIFICATE_FAIL_MESSAGE);
        }
    }

    @Override
    public GiftCertificate update(GiftCertificate giftCertificate) throws RepositoryException {
        try {
            LocalDateTime lastUpdateDate = LocalDateTime.now();
            giftCertificate.setLastUpdateDate(lastUpdateDate);
            return (GiftCertificate) session.merge(giftCertificate);
        } catch (DataIntegrityViolationException e) {
            throw new RepositoryException(UPDATE_CERTIFICATE_FAIL_MESSAGE);
        }
    }

    @Override
    public void delete(GiftCertificate giftCertificate) throws RepositoryException {
        try {
            session.remove(session.contains(giftCertificate) ? giftCertificate : session.merge(giftCertificate));
        } catch (DataIntegrityViolationException e) {
            throw new RepositoryException(DELETE_CERTIFICATE_FAIL_MESSAGE);
        }
    }

    @Override
    public List<GiftCertificate> findAll(int page, int size) throws RepositoryException {
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<GiftCertificate> giftCertificateCriteriaQuery =
                    criteriaBuilder.createQuery(GiftCertificate.class);
            Root<GiftCertificate> giftCertificateRoot = giftCertificateCriteriaQuery.from(GiftCertificate.class);
            giftCertificateCriteriaQuery.select(giftCertificateRoot);
            return session.createQuery(giftCertificateCriteriaQuery)
                    .setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .getResultList();
        } catch (NoResultException e) {
            throw new RepositoryException(FIND_ALL_CERTIFICATES_FAIL_MESSAGE);
        }
    }

    @Override
    public List<GiftCertificate> findCertificateByParam(String param, int page, int size) throws RepositoryException {
        List<GiftCertificate> giftCertificates;
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
            Root<GiftCertificate> giftCertificateRoot = criteriaQuery.from(GiftCertificate.class);
            Predicate predicateForName = criteriaBuilder.like(giftCertificateRoot.get(NAME), "%" + param + "%");
            Predicate predicateForDescription = criteriaBuilder
                    .like(giftCertificateRoot.get(DESCRIPTION), "%" + param + "%");
            Predicate finalPredicate
                    = criteriaBuilder.or(predicateForName, predicateForDescription);
            criteriaQuery.select(giftCertificateRoot).distinct(true)
                    .where(finalPredicate);
            giftCertificates = session.createQuery(criteriaQuery)
                    .setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .getResultList();
        } catch (NoResultException e) {
            throw new RepositoryException(FIND_CERTIFICATE_BY_PARAM_FAIL_MESSAGE);
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
        } catch (NoResultException e) {
            throw new RepositoryException(FIND_CERTIFICATE_BY_PARAM_FAIL_MESSAGE);
        }
    }

    public List<GiftCertificate> searchAllCertificatesByTagName(String tagName, int page, int size) throws RepositoryException {
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
            Root<GiftCertificate> giftCertificateRoot = criteriaQuery.from(GiftCertificate.class);
            criteriaQuery.select(giftCertificateRoot)
                    .where(criteriaBuilder.equal(giftCertificateRoot.join(TAGS).get(NAME), tagName));
            return session.createQuery(criteriaQuery)
                    .setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .getResultList();
        } catch (NoResultException e) {
            throw new RepositoryException(FIND_CERTIFICATE_BY_PARAM_FAIL_MESSAGE);
        }
    }

    public List<GiftCertificate> sortByParam(SortingSpecification<GiftCertificate> orderBySpecification,
                                             int page, int size) throws
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
            return session.createQuery(giftCertificateCriteriaQuery)
                    .setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .getResultList();
        } catch (NoResultException e) {
            throw new RepositoryException(SORT_CERTIFICATE_FAIL_MESSAGE);
        }
    }

    @Override
    public List<GiftCertificate> findAllBySeveralTags(List<Long> tags,
                                                      int page, int size) throws RepositoryException {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> giftCertificateCriteriaQuery =
                criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> giftCertificateRoot = giftCertificateCriteriaQuery.from(GiftCertificate.class);
        try {
            giftCertificateCriteriaQuery.select(giftCertificateRoot);
            List<Predicate> predicates = new ArrayList<>();
            if (tags != null) {
                Expression<Long> countCertificates = criteriaBuilder.count(giftCertificateRoot);
                Predicate joinCertificateAndTag = giftCertificateRoot.join(TAGS).get(ID_CERTIFICATE).in(tags);
                predicates.add(joinCertificateAndTag);
                giftCertificateCriteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])))
                        .having(criteriaBuilder.equal(countCertificates, tags.size()))
                        .groupBy(giftCertificateRoot);
            } else {
                giftCertificateCriteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
            }
        } catch (NoResultException e) {
            throw new RepositoryException(FIND_ALL_CERTIFICATES_FAIL_MESSAGE);
        }
        return session.createQuery(giftCertificateCriteriaQuery)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList();
    }
}

