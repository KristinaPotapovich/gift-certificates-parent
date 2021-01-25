package com.epam.esm.service.services;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.ServiceException;

import java.util.Optional;

/**
 * The interface Tag service.
 */
public interface TagService extends BaseService<TagDto> {
    /**
     * Find tag by name optional.
     *
     * @param name the name
     * @return the optional
     * @throws ServiceException the service exception
     */
    Optional<TagDto> findTagByName(String name) throws ServiceException;

    Optional<TagDto> findPopularTag();
}
