package com.epam.esm.core.repository;

import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.entity.Tag;
import com.epam.esm.core.exception.RepositoryException;
import com.epam.esm.core.repository.specification.OrderBySpecification;

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
    List<GiftCertificate> findCertificateByParam(String param,int page,int size) throws RepositoryException;

    /**
     * Search all certificates by tag name list.
     *
     * @param tagName the tag name
     * @return the list
     * @throws RepositoryException the repository exception
     */
    List<GiftCertificate> searchAllCertificatesByTagName(String tagName,int page,int size) throws RepositoryException;

    List<GiftCertificate> sortByParam(OrderBySpecification<GiftCertificate> orderBySpecification,
                                      int page,int size)
            throws RepositoryException;

    List<GiftCertificate> findAllBySeveralTags(List<Long> tags,int page,int size) throws RepositoryException;
}
