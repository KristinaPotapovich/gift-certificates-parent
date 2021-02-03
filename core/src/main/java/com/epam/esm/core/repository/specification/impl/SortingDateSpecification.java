package com.epam.esm.core.repository.specification.impl;

import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.exception.UnsupportedParametersForSorting;
import com.epam.esm.core.repository.specification.BaseSpecificationForSorting;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import static com.epam.esm.core.repository.specification.SortingParameters.ASC;
import static com.epam.esm.core.repository.specification.SortingParameters.DESC;

/**
 * Sorting date specification.
 */
public class SortingDateSpecification implements BaseSpecificationForSorting<GiftCertificate> {
    private String order;
    private static final String SORTING_FAIL_MASSAGE = "sorting_param_fail";

    /**
     * Instantiates a new Sorting date specification.
     *
     * @param order the order
     */
    public SortingDateSpecification(String order) {
        this.order = order;
    }


    @Override
    public void buildQuery(CriteriaQuery<GiftCertificate> criteriaQuery,
                           CriteriaBuilder criteriaBuilder,
                           Root<GiftCertificate> giftCertificateRoot) {
        if (ASC.name().equalsIgnoreCase(order)) {
            criteriaQuery.orderBy(criteriaBuilder.asc(giftCertificateRoot.get("createDate")));
        } else if (DESC.name().equalsIgnoreCase(order)) {
            criteriaQuery.orderBy(criteriaBuilder.desc(giftCertificateRoot.get("createDate")));
        } else {
            throw new UnsupportedParametersForSorting(SORTING_FAIL_MASSAGE);
        }
    }
}
