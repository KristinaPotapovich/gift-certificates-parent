package com.epam.esm.service.services;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.ServiceException;

import java.util.List;
import java.util.Optional;

/**
 * The interface Tag service.
 */
public interface TagService extends BaseService<TagDto> {
    /**
     * Find tag by id optional.
     *
     * @param id the id
     * @return the optional
     * @throws ServiceException the service exception
     */
    Optional<TagDto> findTagById(long id) throws ServiceException;

    /**
     * Find popular tag optional.
     *
     * @return the optional
     */
    Optional<TagDto> findPopularTag();

    /**
     * Find all tags by certificate id optional.
     *
     * @param idCertificate the id certificate
     * @param page          the page
     * @param size          the size
     * @return the optional
     * @throws ServiceException the service exception
     */
    Optional<List<TagDto>> findAllTagsByCertificateId(long idCertificate, int page, int size)
            throws ServiceException;
}
