package com.epam.esm.core.repository;

import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.exception.RepositoryException;
import com.epam.esm.core.repository.specification.SortingSpecification;

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
     * @param page  the page
     * @param size  the size
     * @return the list
     * @throws RepositoryException the repository exception
     */
    List<GiftCertificate> findCertificateByParam(String param, int page, int size) throws RepositoryException;

    /**
     * Search all certificates by tag name list.
     *
     * @param tagName the tag name
     * @param page    the page
     * @param size    the size
     * @return the list
     * @throws RepositoryException the repository exception
     */
    List<GiftCertificate> searchAllCertificatesByTagName(String tagName, int page, int size) throws RepositoryException;

    /**
     * Sort by param list.
     *
     * @param orderBySpecification the order by specification
     * @param page                 the page
     * @param size                 the size
     * @return the list
     * @throws RepositoryException the repository exception
     */
    List<GiftCertificate> sortByParam(SortingSpecification<GiftCertificate> orderBySpecification,
                                      int page, int size)
            throws RepositoryException;

    /**
     * Find all by several tags list.
     *
     * @param tags the tags
     * @param page the page
     * @param size the size
     * @return the list
     * @throws RepositoryException the repository exception
     */
    List<GiftCertificate> findAllBySeveralTags(List<Long> tags, int page, int size) throws RepositoryException;
}
