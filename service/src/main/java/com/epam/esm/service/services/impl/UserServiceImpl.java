package com.epam.esm.service.services.impl;

import com.epam.esm.core.entity.Order;
import com.epam.esm.core.entity.Role;
import com.epam.esm.core.entity.User;
import com.epam.esm.core.repository.UserRepository;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.mapper.OrderConverter;
import com.epam.esm.service.mapper.UserConverter;
import com.epam.esm.service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * User service.
 */
@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private static final String USER_IS_EXIST_MESSAGE = "user_is_exist";

    /**
     * Instantiates a new User service.
     *
     * @param userRepository the user repository
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Optional<UserDto> findUserById(long id) {
        UserDto userDto = UserConverter.mapToUserDto(userRepository.findUserById(id));
        return Optional.of(userDto);
    }

    @Transactional
    @Override
    public Optional<UserDto> create(UserDto userDto) {
        List<User> users = userRepository.findUserByLogin(userDto.getLogin());
        if (!users.isEmpty()) {
            throw new ServiceException(USER_IS_EXIST_MESSAGE);
        }
        String actualPassword = userDto.getPassword();
        userDto.setPassword(bCryptPasswordEncoder.encode(actualPassword));
        userDto.setUserRole(Role.USER);
        User user = userRepository.create(UserConverter.mapToUser(userDto));
        return Optional.ofNullable(UserConverter.mapToUserDto(user));
    }

    @Override
    public Optional<UserDto> update(UserDto userDto) {
        return Optional.empty();
    }

    @Override
    public void delete(long id) {

    }

    @Override
    public List<UserDto> findAllUsers(int page, int size) {
        List<User> users = userRepository.findAllUsers(page, size);
        return users.stream()
                .map(UserConverter::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<List<OrderDto>> getInformationAboutUsersOrders(long id, int page, int size) {
        List<OrderDto> orderDtos;
        List<Order> orders;
        orders = userRepository.getInformationAboutUsersOrders(id, page, size);
        orderDtos = orders.stream()
                .map(OrderConverter::mapToOrderDto)
                .collect(Collectors.toList());
        return Optional.of(orderDtos);
    }
}
