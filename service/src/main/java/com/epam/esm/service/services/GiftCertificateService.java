package com.epam.esm.service.services;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;

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
     * Find all certificates list.
     *
     * @param param           the param
     * @param paramForSorting the param for sorting
     * @param tags            the tags
     * @param order           the order
     * @param page            the page
     * @param size            the size
     * @return the list
     */
    List<GiftCertificateDto> findAllCertificates(String param, String paramForSorting,
                                                 List<String> tags,
                                                 String order, int page, int size);

    /**
     * Gets information about certificates tags.
     *
     * @param idCertificate the id certificate
     * @param page          the page
     * @param size          the size
     * @return the information about certificates tags
     */
    Optional<List<TagDto>> getInformationAboutCertificatesTags(long idCertificate, int page, int size);

}
