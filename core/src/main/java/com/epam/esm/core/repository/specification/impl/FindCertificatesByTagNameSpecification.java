package com.epam.esm.core.repository.specification.impl;

import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.repository.specification.BaseSpecificationForSearch;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class FindCertificatesByTagNameSpecification implements BaseSpecificationForSearch<GiftCertificate> {
    private static final String TAGS = "tags";
    private static final String NAME = "name";
    private List<String> tags;


    public FindCertificatesByTagNameSpecification(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public Predicate buildWherePart(CriteriaQuery<GiftCertificate> criteriaQuery, CriteriaBuilder criteriaBuilder, Root<GiftCertificate> root) {
        Predicate predicate = null;
        List<Predicate> predicates = new ArrayList<>();
        if (tags != null && !tags.isEmpty()) {
            Predicate joinCertificateAndTag = root.join(TAGS)
                    .get(NAME).in(tags);
            predicates.add(joinCertificateAndTag);
            predicate = criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }
        return predicate;
    }
}
