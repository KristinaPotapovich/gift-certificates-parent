package com.epam.esm.core.repository.specification.impl;

import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.repository.specification.OrderBySpecification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import static com.epam.esm.core.repository.specification.OrderParam.ASC;
import static com.epam.esm.core.repository.specification.OrderParam.DESC;

public class OrderByDateSpecification implements OrderBySpecification<GiftCertificate> {
    private String order;

    public OrderByDateSpecification(String order) {
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
        }
    }
}
