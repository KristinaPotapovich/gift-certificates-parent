package com.epam.esm.core.repository;

import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.exception.RepositoryException;
import com.epam.esm.core.repository.specification.SortByParamSpecification;

import java.util.List;

public interface GiftCertificateRepository extends BaseRepository<GiftCertificate> {
    GiftCertificate findCertificateById(long id) throws RepositoryException;

    List<GiftCertificate> findCertificateByParam(String param) throws RepositoryException;

    void createCertificateAndTagRelationship(long idCertificate, long idTag) throws RepositoryException;
}
