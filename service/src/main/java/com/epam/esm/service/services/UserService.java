package com.epam.esm.service.services;

import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.ServiceException;

import java.util.Optional;

public interface UserService extends BaseService<UserDto> {
    Optional<UserDto> findUserById(long id) throws ServiceException;

    Optional<UserDto> findUserByLogin(String login) throws ServiceException;
}
