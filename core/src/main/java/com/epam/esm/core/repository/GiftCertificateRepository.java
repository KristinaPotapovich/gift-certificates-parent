package com.epam.esm.core.repository;

import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.repository.specification.BaseSpecificationForSorting;
import com.epam.esm.core.repository.specification.ResolverForSearchParams;

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
     * Find all by several tags list.
     *
     * @param tags the tags
     * @param page the page
     * @param size the size
     * @return the list
     */
    List<GiftCertificate> findAllBySeveralTags(List<Long> tags, int page, int size);

    List<GiftCertificate> findAllCertificates(ResolverForSearchParams resolver,
                                              BaseSpecificationForSorting<GiftCertificate> baseSpecification,
                                              int page, int size);

    List<GiftCertificate> findCertificateByName(String name);
}
