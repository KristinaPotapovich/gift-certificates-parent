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
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String ID_CERTIFICATE = "id";
    private static final String TAGS = "tags";


    @Override
    public GiftCertificate create(GiftCertificate giftCertificate) {
        LocalDateTime createDate = LocalDateTime.now();
        giftCertificate.setCreateDate(createDate);
        return (GiftCertificate) session.merge(giftCertificate);
    }

    @Override
    public GiftCertificate update(GiftCertificate giftCertificate) {
        LocalDateTime lastUpdateDate = LocalDateTime.now();
        giftCertificate.setLastUpdateDate(lastUpdateDate);
        return (GiftCertificate) session.merge(giftCertificate);
    }

    @Override
    public void delete(GiftCertificate giftCertificate) {
        session.remove(session.contains(giftCertificate) ? giftCertificate : session.merge(giftCertificate));
    }

    @Override
    public List<GiftCertificate> findAll(int page, int size) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> giftCertificateCriteriaQuery =
                criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> giftCertificateRoot = giftCertificateCriteriaQuery.from(GiftCertificate.class);
        giftCertificateCriteriaQuery.select(giftCertificateRoot);
        return session.createQuery(giftCertificateCriteriaQuery)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public List<GiftCertificate> findCertificateByParam(String param, int page, int size) {
        List<GiftCertificate> giftCertificates;
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
        return giftCertificates;
    }

    @Override
    public GiftCertificate findCertificateById(long id) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> giftCertificateRoot = criteriaQuery.from(GiftCertificate.class);
        criteriaQuery.where(criteriaBuilder.equal(giftCertificateRoot.get(ID_CERTIFICATE), id));
        return session.createQuery(criteriaQuery).getSingleResult();
    }

    public List<GiftCertificate> searchAllCertificatesByTagName(String tagName, int page, int size) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> giftCertificateRoot = criteriaQuery.from(GiftCertificate.class);
        criteriaQuery.select(giftCertificateRoot)
                .where(criteriaBuilder.equal(giftCertificateRoot.join(TAGS).get(NAME), tagName));
        return session.createQuery(criteriaQuery)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList();
    }

    public List<GiftCertificate> sortByParam(SortingSpecification<GiftCertificate> orderBySpecification,
                                             int page, int size) {
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
    }

    @Override
    public List<GiftCertificate> findAllBySeveralTags(List<Long> tags, int page, int size) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> giftCertificateCriteriaQuery =
                criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> giftCertificateRoot = giftCertificateCriteriaQuery.from(GiftCertificate.class);
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
        return session.createQuery(giftCertificateCriteriaQuery)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList();
    }
}

