package com.epam.esm.core.repository.impl;

import com.epam.esm.core.entity.Tag;
import com.epam.esm.core.exception.RepositoryException;
import com.epam.esm.core.repository.TagRepository;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

/**
 * The type Tag repository.
 */
@Repository
public class TagRepositoryImpl implements TagRepository {
    @PersistenceContext
    private Session session;
    private static final String CREATE_TAG_FAIL = "tag_create_fail";
    private static final String UPDATE_TAG_FAIL = "tag_update_fail";
    private static final String DELETE_TAG_FAIL = "tag_delete_fail";
    private static final String FIND_BY_NAME_TAG_FAIL = "tag_find_by_name_fail";
    private static final String FIND_ALL_TAG_FAIL = "tag_find_all_fail";
    private static final String NAME = "name";
    private static final String QUERY_FOR_POPULAR_TAG =
            "SELECT t.id_tag, t.name, SUM(ot.price) FROM tag t " +
                    "  JOIN certificates_tags ct ON ct.id_tag =t.id_tag " +
                    "  JOIN gift_certificate gc ON ct.id_certificate=gc.id_certificate " +
                    "  JOIN orders_certificates oc ON gc.id_certificate = oc.id_certificate " +
                    "  JOIN order_table ot ON ot.id_order = oc.id_order" +
                    "  JOIN user u ON ot.id_user = u.id_user " +
                    "GROUP BY t.id_tag, ot.price, u.login " +
                    "ORDER BY COUNT(t.id_tag) DESC limit 1";


    @Override
    public Tag create(Tag tag) throws RepositoryException {
        try {
            session.persist(tag);
        } catch (DataAccessException e) {
            throw new RepositoryException(CREATE_TAG_FAIL);
        }
        return session.find(Tag.class,tag);
    }

    @Override
    public void update(Tag tag) throws RepositoryException {
        try {
            session.merge(tag);
        } catch (DataAccessException e) {
            throw new RepositoryException(UPDATE_TAG_FAIL);
        }
    }

    @Override
    public void delete(Tag tag) throws RepositoryException {
        try {
            session.remove(tag);
        } catch (DataAccessException e) {
            throw new RepositoryException(DELETE_TAG_FAIL);
        }
    }

    public Optional<Tag> findTagByName(String name) throws RepositoryException {
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Tag> tagCriteriaQuery = criteriaBuilder.createQuery(Tag.class);
            Root<Tag> tagRoot = tagCriteriaQuery.from(Tag.class);
            tagCriteriaQuery.where(criteriaBuilder.equal(tagRoot.get(NAME), name));
            Tag tag = session.createQuery(tagCriteriaQuery).getSingleResult();
            return Optional.of(tag);
        } catch (DataAccessException e) {
            throw new RepositoryException(FIND_BY_NAME_TAG_FAIL);
        }
    }

    public List<Tag> findAll() throws RepositoryException {
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Tag> tagCriteriaQuery =
                    criteriaBuilder.createQuery(Tag.class);
            Root<Tag> tagRootRoot = tagCriteriaQuery.from(Tag.class);
            tagCriteriaQuery.select(tagRootRoot);
            return session.createQuery(tagCriteriaQuery).getResultList();
        } catch (DataAccessException e) {
            throw new RepositoryException(FIND_ALL_TAG_FAIL);
        }
    }

    public List<Tag> findAllTagsByCertificateId(long idCertificate) throws RepositoryException {
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
            Root<Tag> tagRoot = criteriaQuery.from(Tag.class);
            criteriaQuery.select(tagRoot)
                    .where(criteriaBuilder.equal(tagRoot.join("certificates").get("id"), idCertificate));
            return session.createQuery(criteriaQuery).getResultList();
        } catch (DataAccessException e) {
            throw new RepositoryException(FIND_ALL_TAG_FAIL);
        }
    }

    @Override
    public Tag findPopularTag() {
        Query result = session.createNativeQuery(QUERY_FOR_POPULAR_TAG, Tag.class);
        return (Tag) result.getSingleResult();

    }
}

