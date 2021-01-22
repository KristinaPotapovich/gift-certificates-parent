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
     * @return the optional
     * @throws ServiceException the service exception
     */
    Optional<List<GiftCertificateDto>> findCertificateByParam(String param) throws ServiceException;

    /**
     * Search all certificates by tag name optional.
     *
     * @param tagName the tag name
     * @return the optional
     */
    Optional<List<GiftCertificateDto>> searchAllCertificatesByTagName(String tagName);

    /**
     * Sort by param optional.
     *
     * @param paramForSorting the param for sorting
     * @param order           the order
     * @return the optional
     * @throws ServiceException the service exception
     */
    Optional<List<GiftCertificateDto>> sortByParam(String paramForSorting, String order) throws ServiceException;

    void patch(GiftCertificateDto giftCertificateDto) throws ServiceException;
}
