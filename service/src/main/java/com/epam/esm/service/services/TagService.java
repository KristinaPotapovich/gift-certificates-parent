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
     * Find tag by name optional.
     *
     * @return the optional
     * @throws ServiceException the service exception
     */
    Optional<TagDto> findTagById(long id) throws ServiceException;

    Optional<TagDto> findPopularTag();

    Optional<List<TagDto>> findAllTagsByCertificateId(long idCertificate, int page, int size)
            throws ServiceException;
}
