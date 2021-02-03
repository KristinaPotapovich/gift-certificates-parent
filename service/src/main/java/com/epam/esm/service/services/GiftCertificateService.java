package com.epam.esm.service.services;

import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.service.dto.GiftCertificateDto;

import java.util.List;
import java.util.Optional;

/**
 * The interface Gift certificate service.
 */
public interface GiftCertificateService extends BaseService<GiftCertificateDto> {
    /**
     * Find certificate by id optional.
     *
     * @param id the id
     * @return the optional
     */
    Optional<GiftCertificateDto> findCertificateById(long id);

    /**
     * Patch optional.
     *
     * @param giftCertificateDto the gift certificate dto
     * @return the optional
     */
    Optional<GiftCertificateDto> patch(GiftCertificateDto giftCertificateDto);

    /**
     * Find all by several tags optional.
     *
     * @param tags the tags
     * @param page the page
     * @param size the size
     * @return the optional
     */
    Optional<List<GiftCertificateDto>> findAllBySeveralTags(List<Long> tags, int page, int size);

    List<GiftCertificateDto> findAllCertificates(String param, String tagName, String paramForSorting,
                                     String order, int page, int size);
}
