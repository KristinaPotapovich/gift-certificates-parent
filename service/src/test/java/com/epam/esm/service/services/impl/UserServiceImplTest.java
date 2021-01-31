package com.epam.esm.service.services.impl;

import com.epam.esm.core.entity.User;
import com.epam.esm.core.entity.UserRole;
import com.epam.esm.core.exception.RepositoryException;
import com.epam.esm.core.repository.UserRepository;
import com.epam.esm.core.repository.impl.UserRepositoryImpl;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.mapper.UserConverter;
import com.epam.esm.service.services.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceImplTest {
    UserRepository userRepository;
    UserService userService;
    User user;
    List<User> users;

    @BeforeAll
    void setUp() {
        userRepository = mock(UserRepositoryImpl.class);
        userService = new UserServiceImpl(userRepository);
        UserDto userDto = new UserDto(1L, "testLogin", "testPassword", UserRole.USER);
        user = UserConverter.mapToUser(userDto);
        users = new ArrayList<>();
        users.add(user);
    }

    @AfterAll
    public void tearDown() {
        userRepository = null;
        userService = null;
        user = null;
        users = null;
    }

    @Test
    void findUserById() throws RepositoryException, ServiceException {
        when(userRepository.findUserById(1L)).thenReturn(user);
        Optional<UserDto> actual = userService.findUserById(1L);
        verify(userRepository).findUserById(1L);
        assertTrue(actual.isPresent());
    }

    @Test
    void findAll() throws RepositoryException, ServiceException {
        when(userRepository.findAll(1, 3)).thenReturn(users);
        List<UserDto> actual = userService.findAll(1, 3);
        verify(userRepository).findAll(1, 3);
        assertFalse(actual.isEmpty());
    }
}