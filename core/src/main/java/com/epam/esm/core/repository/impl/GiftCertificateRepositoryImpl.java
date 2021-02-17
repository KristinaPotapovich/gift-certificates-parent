package com.epam.esm.core.repository.impl;


import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.entity.Tag;
import com.epam.esm.core.repository.GiftCertificateRepository;
import com.epam.esm.core.repository.specification.BaseSpecificationForSorting;
import com.epam.esm.core.repository.specification.ResolverForSearchParams;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Gift certificate repository.
 */
@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    @PersistenceContext
    private Session session;
    private static final String ID_CERTIFICATE = "id";


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
        session.merge(giftCertificate);
    }

    @Override
    public List<Tag> getInformationAboutCertificatesTags(long idCertificate, int page, int size) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> tagRoot = criteriaQuery.from(Tag.class);
        criteriaQuery.select(tagRoot)
                .where(criteriaBuilder.and(criteriaBuilder
                                .equal(tagRoot.join("certificates").get("id"), idCertificate)),
                        criteriaBuilder.equal(tagRoot.join("certificates").get("isDeleted"), false));
        return session.createQuery(criteriaQuery)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public List<GiftCertificate> findAllCertificates(ResolverForSearchParams resolver,
                                                     BaseSpecificationForSorting<GiftCertificate> specification,
                                                     int page, int size) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> giftCertificateCriteriaQuery =
                criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> giftCertificateRoot = giftCertificateCriteriaQuery.from(GiftCertificate.class);
        CriteriaQuery<GiftCertificate> query = giftCertificateCriteriaQuery.select(giftCertificateRoot);
        Expression<Long> countCertificates = criteriaBuilder.count(giftCertificateRoot);
        query = query.distinct(true).where(resolver.buildListPredicatesForQuery(query,
                criteriaBuilder, giftCertificateRoot).toArray(new Predicate[0]));
        concatQuery(query, criteriaBuilder, countCertificates, resolver, giftCertificateRoot);
        if (specification != null) {
            specification.buildQuery(query, criteriaBuilder, giftCertificateRoot);
        }
        return session.createQuery(query)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public GiftCertificate findCertificateById(long id) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> giftCertificateRoot = criteriaQuery.from(GiftCertificate.class);
        criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(giftCertificateRoot.get(ID_CERTIFICATE), id)),
                criteriaBuilder.equal(giftCertificateRoot.get("isDeleted"), false));
        return session.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    public List<GiftCertificate> findCertificateByName(String name) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> giftCertificateRoot = criteriaQuery.from(GiftCertificate.class);
        criteriaQuery.where(criteriaBuilder.equal(giftCertificateRoot.get("name"), name));
        return session.createQuery(criteriaQuery).getResultList();
    }

    private void concatQuery(CriteriaQuery<GiftCertificate> criteriaQuery, CriteriaBuilder criteriaBuilder
            , Expression<Long> countCertificates, ResolverForSearchParams resolver, Root<GiftCertificate> root) {
        if (resolver.getTags() != null && !resolver.getTags().isEmpty()) {
            criteriaQuery.having(criteriaBuilder.equal(countCertificates, resolver.getTags().size()))
                    .groupBy(root);
        }
    }
}

