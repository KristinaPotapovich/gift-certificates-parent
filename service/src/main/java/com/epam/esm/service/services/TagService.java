package com.epam.esm.service.services;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.ServiceException;

import java.util.Optional;

public interface TagService extends BaseService<TagDto> {
    Optional<TagDto> findTagByName(String name) throws ServiceException;

    boolean isTagExistByName(String name) throws ServiceException;
}
