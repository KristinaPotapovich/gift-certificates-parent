package com.epam.esm.service.services;

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
     * Find certificate by param optional.
     *
     * @param param the param
     * @param page  the page
     * @param size  the size
     * @return the optional
     */
    Optional<List<GiftCertificateDto>> findCertificateByParam(String param, int page, int size);

    /**
     * Search all certificates by tag name optional.
     *
     * @param tagName the tag name
     * @param page    the page
     * @param size    the size
     * @return the optional
     */
    Optional<List<GiftCertificateDto>> searchAllCertificatesByTagName(String tagName, int page, int size);

    /**
     * Sort by param optional.
     *
     * @param paramForSorting the param for sorting
     * @param order           the order
     * @param page            the page
     * @param size            the size
     * @return the optional
     */
    Optional<List<GiftCertificateDto>> sortByParam(String paramForSorting, String order, int page, int size);

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
}
