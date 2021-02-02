package com.epam.esm.core.repository;

import com.epam.esm.core.entity.GiftCertificate;
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
     */
    GiftCertificate findCertificateById(long id);

    /**
     * Find certificate by param list.
     *
     * @param param the param
     * @param page  the page
     * @param size  the size
     * @return the list
     */
    List<GiftCertificate> findCertificateByParam(String param, int page, int size);

    /**
     * Search all certificates by tag name list.
     *
     * @param tagName the tag name
     * @param page    the page
     * @param size    the size
     * @return the list
     */
    List<GiftCertificate> searchAllCertificatesByTagName(String tagName, int page, int size);

    /**
     * Sort by param list.
     *
     * @param orderBySpecification the order by specification
     * @param page                 the page
     * @param size                 the size
     * @return the list
     */
    List<GiftCertificate> sortByParam(SortingSpecification<GiftCertificate> orderBySpecification, int page, int size);

    /**
     * Find all by several tags list.
     *
     * @param tags the tags
     * @param page the page
     * @param size the size
     * @return the list
     */
    List<GiftCertificate> findAllBySeveralTags(List<Long> tags, int page, int size);
}
