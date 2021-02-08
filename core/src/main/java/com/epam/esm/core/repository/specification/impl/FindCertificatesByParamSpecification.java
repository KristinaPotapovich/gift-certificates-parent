package com.epam.esm.core.repository.specification.impl;

import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.repository.specification.BaseSpecificationForSearch;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class FindCertificatesByParamSpecification implements BaseSpecificationForSearch<GiftCertificate> {

    private String param;
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";

    public FindCertificatesByParamSpecification(String param) {
        this.param = param;
    }

    @Override
    public Predicate buildWherePart(CriteriaQuery<GiftCertificate> criteriaQuery, CriteriaBuilder criteriaBuilder,
                                    Root<GiftCertificate> root) {
        Predicate predicate = null;
        if (param != null && !param.isEmpty()) {
            Predicate predicateForName = criteriaBuilder.like(root.get(NAME), "%" + param + "%");
            Predicate predicateForDescription = criteriaBuilder
                    .like(root.get(DESCRIPTION), "%" + param + "%");
            predicate = criteriaBuilder.or(predicateForName, predicateForDescription);
        }
        return predicate;
    }
}
