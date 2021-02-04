package com.epam.esm.service.services.impl;

import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.entity.Order;
import com.epam.esm.core.entity.User;
import com.epam.esm.core.entity.UserRole;
import com.epam.esm.core.repository.GiftCertificateRepository;
import com.epam.esm.core.repository.OrderRepository;
import com.epam.esm.core.repository.UserRepository;
import com.epam.esm.core.repository.impl.GiftCertificateRepositoryImpl;
import com.epam.esm.core.repository.impl.OrderRepositoryImpl;
import com.epam.esm.core.repository.impl.UserRepositoryImpl;
import com.epam.esm.service.dto.*;
import com.epam.esm.service.mapper.GiftCertificateConverter;
import com.epam.esm.service.mapper.OrderConverter;
import com.epam.esm.service.mapper.UserConverter;
import com.epam.esm.service.services.GiftCertificateService;
import com.epam.esm.service.services.OrderService;
import com.epam.esm.service.services.UserService;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderServiceImplTest {
    private OrderService orderService;
    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private GiftCertificateRepository giftCertificateRepository;
    private OrderDto orderDto;
    private Order order;
    private UserService userService;
    private GiftCertificateService giftCertificateService;
    private List<Order> orders;
    private GiftCertificateDto giftCertificateDto1;
    private UserDto user;
    private List<GiftCertificate> giftCertificates;


    @BeforeAll
    public void setUp() {
        orderRepository = mock(OrderRepositoryImpl.class);
        userRepository = mock(UserRepositoryImpl.class);
        giftCertificateRepository = mock(GiftCertificateRepositoryImpl.class);
        giftCertificateService = new GiftCertificateServiceImpl(giftCertificateRepository);
        userService = new UserServiceImpl(userRepository);
        orderService = new OrderServiceImpl(giftCertificateService, userService, orderRepository);
        TagDto tagDto = new TagDto(1L, "tag");
        List<TagDto> tagDtos = new ArrayList<>();
        tagDtos.add(tagDto);
        giftCertificateDto1 = new GiftCertificateDto(1L, "testCertificate2", "testDescription1",
                BigDecimal.valueOf(15.22), 5,
                LocalDateTime.of(2021, 1, 16, 19, 10),
                LocalDateTime.of(2021, 1, 16, 19, 10), tagDtos);
        List<GiftCertificateDto> giftCertificateDtos = new ArrayList<>();
        giftCertificateDtos.add(giftCertificateDto1);
        user = new UserDto(1L, "kristina", "123fghj", UserRole.USER);
        orderDto = new OrderDto(1L, BigDecimal.valueOf(15.22),
                LocalDateTime.of(2021, 1, 16, 19, 15), giftCertificateDtos, user);
        order = OrderConverter.mapToOrder(orderDto);
        orders = new ArrayList<>();
        orders.add(order);
        List<OrderDto> orderDtos = new ArrayList<>();
        orderDtos.add(orderDto);
        GiftCertificate giftCertificate = GiftCertificateConverter.mapToGiftCertificate(giftCertificateDto1);
        giftCertificates = new ArrayList<>();
        giftCertificates.add(giftCertificate);
    }

    @AfterAll
    public void tearDown() {
        orderRepository = null;
        userRepository = null;
        giftCertificateRepository = null;
        giftCertificateService = null;
        userService = null;
        orderService = null;
        giftCertificateDto1 = null;
        user = null;
        orderDto = null;
        order = null;
        orders = null;
        giftCertificates = null;
    }

    @Test
    void purchaseCertificate() {
        when(giftCertificateRepository.findCertificateById(1L))
                .thenReturn(GiftCertificateConverter.mapToGiftCertificate(giftCertificateDto1));
        when(userRepository.findUserById(1L)).thenReturn(UserConverter.mapToUser(user));
        order.setId(0);
        when(orderRepository.create(order)).thenReturn(order);
        List<Long> idCertificates = new ArrayList<>();
        idCertificates.add(giftCertificateDto1.getId());
        PurchaseParam purchaseParam = new PurchaseParam(idCertificates, 1L);
        Optional<OrderDto> actual = orderService.purchaseCertificate(purchaseParam);
        verify(orderRepository).create(order);
        assertTrue(actual.isPresent());
    }

    @Test
    void findAll() {
        when(orderRepository.findAllOrders(1, 3)).thenReturn(orders);
        List<OrderDto> actual = orderService.findAllOrders(1, 3);
        verify(orderRepository).findAllOrders(1, 3);
        assertFalse(actual.isEmpty());
        assertEquals(1, actual.get(0).getId());
    }

    @Test
    void findOrderById() {
        User user1 = new User(1L, "testLogin", "testPassword", UserRole.USER, orders);
        Order orderForCreate = new Order(1L, BigDecimal.valueOf(15.22),
                LocalDateTime.of(2021, 1, 16, 19, 15), giftCertificates,
                user1);
        when(orderRepository.findOrderById(orderForCreate.getId())).thenReturn(orderForCreate);
        Optional<Map<String, Object>> actual = orderService.findOrderById(orderForCreate.getId());
        verify(orderRepository).findOrderById(orderForCreate.getId());
        assertTrue(actual.isPresent());
    }

    @Test
    void findAllOrdersByUser() {
        when(orderRepository.findAllOrdersByUser(1L, 1, 3)).thenReturn(orders);
        Optional<List<OrderDto>> actual = orderService.findAllOrdersByUser(1L, 1, 3);
        verify(orderRepository).findAllOrdersByUser(1L, 1, 3);
        assertTrue(actual.isPresent());
    }
}