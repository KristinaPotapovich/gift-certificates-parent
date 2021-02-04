package com.epam.esm.core.repository;

import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.entity.Tag;
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
     * Find all certificates list.
     *
     * @param resolver          the resolver
     * @param baseSpecification the base specification
     * @param page              the page
     * @param size              the size
     * @return the list
     */
    List<GiftCertificate> findAllCertificates(ResolverForSearchParams resolver,
                                              BaseSpecificationForSorting<GiftCertificate> baseSpecification,
                                              int page, int size);

    /**
     * Find certificate by name list.
     *
     * @param name the name
     * @return the list
     */
    List<GiftCertificate> findCertificateByName(String name);

    /**
     * Find all tags by certificate id list.
     *
     * @param idCertificate the id certificate
     * @param page          the page
     * @param size          the size
     * @return the list
     */
    List<Tag> getInformationAboutCertificatesTags(long idCertificate, int page, int size);
}
