package com.epam.esm.core.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * The interface Base specification for sorting.
 *
 * @param <T> the type parameter
 */
public interface BaseSpecificationForSorting<T> {
    /**
     * Build query.
     *
     * @param criteriaQuery   the criteria query
     * @param criteriaBuilder the criteria builder
     * @param root            the root
     */
    void buildQuery(CriteriaQuery<T> criteriaQuery, CriteriaBuilder criteriaBuilder,
                    Root<T> root);
}
