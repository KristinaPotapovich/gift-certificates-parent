package com.epam.esm.core.repository;

import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.exception.RepositoryException;
import com.epam.esm.core.repository.specification.SortByParamSpecification;

import java.util.List;

/**
 * The interface Gift certificate repository.
 */
public interface GiftCertificateRepository extends BaseRepository<GiftCertificate> {
    /**
     * Find certificate by id gift certificate.
     *
     * @param id the id
     * @return the gift certificate
     * @throws RepositoryException the repository exception
     */
    GiftCertificate findCertificateById(long id) throws RepositoryException;

    /**
     * Find certificate by param list.
     *
     * @param param the param
     * @return the list
     * @throws RepositoryException the repository exception
     */
    List<GiftCertificate> findCertificateByParam(String param) throws RepositoryException;

    /**
     * Create certificate and tag relation.
     *
     * @param idCertificate the id certificate
     * @param idTag         the id tag
     * @throws RepositoryException the repository exception
     */
    void createCertificateAndTagRelation(long idCertificate, long idTag) throws RepositoryException;

    /**
     * Delete certificate and tag relation.
     *
     * @param idCertificate the id certificate
     * @throws RepositoryException the repository exception
     */
    void deleteCertificateAndTagRelation(long idCertificate) throws RepositoryException;

    /**
     * Search all certificates by tag name list.
     *
     * @param tagName the tag name
     * @return the list
     * @throws RepositoryException the repository exception
     */
    List<GiftCertificate> searchAllCertificatesByTagName(String tagName) throws RepositoryException;

    /**
     * Sort by param list.
     *
     * @param sortByParamSpecification the sort by param specification
     * @return the list
     * @throws RepositoryException the repository exception
     */
    List<GiftCertificate> sortByParam(SortByParamSpecification sortByParamSpecification) throws RepositoryException;

    /**
     * Is certificate exist boolean.
     *
     * @param giftCertificate the gift certificate
     * @return the boolean
     * @throws RepositoryException the repository exception
     */
    boolean isCertificateExist(GiftCertificate giftCertificate) throws RepositoryException;
}
