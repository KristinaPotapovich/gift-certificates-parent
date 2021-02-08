package com.epam.esm.core.repository.impl;

import com.epam.esm.core.entity.Tag;
import com.epam.esm.core.repository.TagRepository;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

/**
 * Tag repository.
 */
@Repository
public class TagRepositoryImpl implements TagRepository {
    @PersistenceContext
    private Session session;
    private static final String QUERY_FOR_POPULAR_TAG =
            "SELECT t.id_tag, t.name, SUM(ot.price)sum FROM tag t " +
                    "  JOIN certificates_tags ct ON ct.id_tag =t.id_tag " +
                    "  JOIN gift_certificate gc ON ct.id_certificate=gc.id_certificate " +
                    "  JOIN orders_certificates oc ON gc.id_certificate = oc.id_certificate " +
                    "  JOIN order_table ot ON ot.id_order = oc.id_order" +
                    "  JOIN user u ON ot.id_user = u.id_user " +
                    "GROUP BY t.id_tag, ot.price, u.login " +
                    "ORDER BY COUNT(t.id_tag) DESC limit 1";


    @Override
    public Tag create(Tag tag) {
        return (Tag) session.merge(tag);
    }

    @Override
    public Tag update(Tag tag) {
        return (Tag) session.merge(tag);
    }

    @Override
    public void delete(Tag tag) {
        session.remove(session.contains(tag) ? tag : session.merge(tag));
    }

    @Override
    public Optional<Tag> findTagById(long id) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Tag> tagCriteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> tagRoot = tagCriteriaQuery.from(Tag.class);
        tagCriteriaQuery.where(criteriaBuilder.equal(tagRoot.get("id"), id));
        Tag tag = session.createQuery(tagCriteriaQuery).getSingleResult();
        return Optional.of(tag);
    }

    @Override
    public List<Tag> findAllTags(int page, int size) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Tag> tagCriteriaQuery =
                criteriaBuilder.createQuery(Tag.class);
        Root<Tag> tagRootRoot = tagCriteriaQuery.from(Tag.class);
        tagCriteriaQuery.select(tagRootRoot);
        return session.createQuery(tagCriteriaQuery)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public Tag findPopularTag() {
        Query result = session.createNativeQuery(QUERY_FOR_POPULAR_TAG, Tag.class);
        return (Tag) result.getSingleResult();
    }
}

