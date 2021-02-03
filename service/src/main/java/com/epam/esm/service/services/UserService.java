package com.epam.esm.service.services;


import com.epam.esm.service.dto.UserDto;

import java.util.List;
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
     */
    Optional<UserDto> findUserById(long id);
    List<UserDto> findAllUsers(int page, int size);
}
