package com.epam.esm.core.repository.specification;

import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.repository.specification.impl.FindCertificatesByParamSpecification;
import com.epam.esm.core.repository.specification.impl.FindCertificatesByTagNameSpecification;
import lombok.Getter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Resolver for search params.
 */
@Getter
public class ResolverForSearchParams {
    /**
     * Tag name.
     */
    List<String> tags;
    /**
     * Part name or description.
     */
    String param;


    public ResolverForSearchParams(List<String> tags, String param) {
        this.tags = tags;
        this.param = param;
    }

    /**
     * Build list predicates for query list.
     *
     * @param criteriaQuery   the criteria query
     * @param criteriaBuilder the criteria builder
     * @param root            the root
     * @return the list
     */
    public List<Predicate> buildListPredicatesForQuery(CriteriaQuery<GiftCertificate> criteriaQuery,
                                                       CriteriaBuilder criteriaBuilder, Root<GiftCertificate> root) {
        List<Predicate> predicates = new ArrayList<>();
        BaseSpecificationForSearch<GiftCertificate> specificationForSearch;
        if (tags != null && !tags.isEmpty()) {
            specificationForSearch = new FindCertificatesByTagNameSpecification(tags);
            predicates.add(specificationForSearch.buildWherePart(criteriaQuery, criteriaBuilder, root));
        }
        if (param != null && !param.isEmpty()) {
            specificationForSearch = new FindCertificatesByParamSpecification(param);
            predicates.add(specificationForSearch.buildWherePart(criteriaQuery, criteriaBuilder, root));
        }
        return predicates;
    }
}
