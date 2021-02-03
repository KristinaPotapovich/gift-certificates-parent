package com.epam.esm.core.repository.specification;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * The interface Sorting specification.
 *
 * @param <T> the type parameter
 */
public interface BaseSpecificationForSearch<T> {
    /**
     * Build query.
     *
     * @param criteriaQuery   the criteria query
     * @param criteriaBuilder the criteria builder
     * @param root            the root
     */
    Predicate buildWherePart(CriteriaQuery<T> criteriaQuery, CriteriaBuilder criteriaBuilder,
                             Root<T> root);
}
