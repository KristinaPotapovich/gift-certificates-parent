package com.epam.esm.core.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public interface OrderBySpecification<T> {
    void buildQuery(CriteriaQuery<T> criteriaQuery, CriteriaBuilder criteriaBuilder,
                    Root<T> root);
}
