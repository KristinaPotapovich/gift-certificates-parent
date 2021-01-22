package com.epam.esm.service.services.impl;

import com.epam.esm.core.entity.User;
import com.epam.esm.core.exception.RepositoryException;
import com.epam.esm.core.repository.UserRepository;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.mapper.UserConverter;
import com.epam.esm.service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserDto> findUserById(long id) throws ServiceException {
        UserDto userDto;
        try {
            userDto = UserConverter.mapToUserDto(userRepository.findUserById(id));
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
        return Optional.of(userDto);
    }

    @Override
    public Optional<UserDto> findUserByLogin(String login) throws ServiceException {
        UserDto userDto = new UserDto();
        try {
            userDto.setLogin(login);
            userDto = UserConverter.mapToUserDto(userRepository.findUserByLogin(login));
            return Optional.ofNullable(userDto);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Optional<UserDto> create(UserDto userDto) throws ServiceException {
        return Optional.empty();
    }

    @Override
    public void update(UserDto userDto) throws ServiceException {

    }

    @Override
    public void delete(long id) throws ServiceException {

    }

    @Override
    public Optional<List<UserDto>> findAll() throws ServiceException {
        List<User> users;
        try {
            users = userRepository.findAll();
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
        return Optional.of(users.stream()
                .map(UserConverter::mapToUserDto)
                .collect(Collectors.toList()));
    }
}
