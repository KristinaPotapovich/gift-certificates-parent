package com.epam.esm.service.services.impl;

import com.epam.esm.core.entity.*;
import com.epam.esm.core.repository.UserRepository;
import com.epam.esm.core.repository.impl.UserRepositoryImpl;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.FullInfoUserDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.mapper.UserFullInfoConverter;
import com.epam.esm.service.services.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceImplTest {
    private UserRepository userRepository;
    private UserService userService;
    private User user;
    private List<User> users;


    @BeforeAll
    void setUp() {
        userRepository = mock(UserRepositoryImpl.class);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userRepository,bCryptPasswordEncoder);
        FullInfoUserDto fullInfoUserDto = new FullInfoUserDto(1L, "testLogin", "testPassword", Role.USER);
        user = UserFullInfoConverter.mapToUser(fullInfoUserDto);
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
    void findUserById() {
        when(userRepository.findUserById(1L)).thenReturn(user);
        Optional<UserDto> actual = userService.findUserById(1L);
        verify(userRepository).findUserById(1L);
        assertTrue(actual.isPresent());
    }

    @Test
    void findAll() {
        when(userRepository.findAllUsers(1, 3)).thenReturn(users);
        List<FullInfoUserDto> actual = userService.findAllUsers(1, 3);
        verify(userRepository).findAllUsers(1, 3);
        assertFalse(actual.isEmpty());
        assertEquals(1, actual.get(0).getId());
    }

    @Test
    void getInformationAboutUsersOrders() {
        Tag tag = new Tag();
        tag.setId(1);
        tag.setName("newTag");
        List<Tag> tags = new ArrayList<>();
        tags.add(tag);
        GiftCertificate giftCertificate = new GiftCertificate(1L, "testCertificate2",
                "testDescription1",
                BigDecimal.valueOf(15.22), 5,
                LocalDateTime.of(2021, 1, 16, 19, 10),
                LocalDateTime.of(2021, 1, 16, 19, 10), tags,false);
        List<GiftCertificate> giftCertificates = new ArrayList<>();
        giftCertificates.add(giftCertificate);
        Order order = new Order(1L, BigDecimal.valueOf(15.22),
                LocalDateTime.of(2021, 1, 16, 19, 15), giftCertificates, user);
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        when(userRepository.getInformationAboutUsersOrders(1L, 1, 3)).thenReturn(orders);
        Optional<List<OrderDto>> actual = userService.getInformationAboutUsersOrders(1L, 1, 3);
        verify(userRepository).getInformationAboutUsersOrders(1L, 1, 3);
        assertTrue(actual.isPresent());
    }
}