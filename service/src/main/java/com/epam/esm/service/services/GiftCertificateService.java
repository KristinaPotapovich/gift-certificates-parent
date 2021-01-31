package com.epam.esm.service.services;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.exception.ServiceException;

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
     * @throws ServiceException the service exception
     */
    Optional<GiftCertificateDto> findCertificateById(long id) throws ServiceException;

    /**
     * Find certificate by param optional.
     *
     * @param param the param
     * @param page  the page
     * @param size  the size
     * @return the optional
     * @throws ServiceException the service exception
     */
    Optional<List<GiftCertificateDto>> findCertificateByParam(String param, int page,
                                                              int size) throws ServiceException;

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
     * @throws ServiceException the service exception
     */
    Optional<List<GiftCertificateDto>> sortByParam(String paramForSorting, String order, int page, int size) throws ServiceException;

    /**
     * Patch optional.
     *
     * @param giftCertificateDto the gift certificate dto
     * @return the optional
     * @throws ServiceException the service exception
     */
    Optional<GiftCertificateDto> patch(GiftCertificateDto giftCertificateDto) throws ServiceException;

    /**
     * Find all by several tags optional.
     *
     * @param tags the tags
     * @param page the page
     * @param size the size
     * @return the optional
     * @throws ServiceException the service exception
     */
    Optional<List<GiftCertificateDto>> findAllBySeveralTags(List<Long> tags, int page, int size) throws ServiceException;
}
