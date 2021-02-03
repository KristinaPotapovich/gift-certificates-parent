package com.epam.esm.core.repository.specification.impl;

import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.repository.specification.BaseSpecificationForSearch;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class FindCertificatesByTagNameSpecification implements BaseSpecificationForSearch<GiftCertificate> {
    private static final String TAGS = "tags";
    private static final String NAME = "name";
    private String tagName;


    public FindCertificatesByTagNameSpecification(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public Predicate buildWherePart(CriteriaQuery<GiftCertificate> criteriaQuery, CriteriaBuilder criteriaBuilder, Root<GiftCertificate> root) {
        Predicate predicate = null;
        if (tagName != null && !tagName.isEmpty()) {
            predicate = criteriaBuilder.equal(root.join(TAGS).get(NAME), tagName);
        }
        return predicate;
    }
}
