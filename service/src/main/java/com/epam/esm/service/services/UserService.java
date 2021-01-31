package com.epam.esm.service.services;


import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.ServiceException;

import java.util.Optional;

/**
 * The interface User service.
 */
public interface UserService extends BaseService<UserDto> {
    /**
     * Find user by id optional.
     *
     * @param id the id
     * @return the optional
     * @throws ServiceException the service exception
     */
    Optional<UserDto> findUserById(long id) throws ServiceException;
}
