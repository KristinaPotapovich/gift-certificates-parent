package com.epam.esm.app.controller;

import com.epam.esm.app.config.GiftCertificatesParentApplication;
import com.epam.esm.core.entity.Role;
import com.epam.esm.service.dto.*;
import com.epam.esm.service.services.OrderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(MockitoJUnitRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = GiftCertificatesParentApplication.class)
@AutoConfigureMockMvc
class OrderControllerTest {
    @MockBean
    private OrderService orderService;
    private OrderDto orderDto;
    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        TagDto tagDto = new TagDto(1L, "tag");
        List<TagDto> tagDtos = new ArrayList<>();
        tagDtos.add(tagDto);
        GiftCertificateDto giftCertificateDto1 = new GiftCertificateDto(1L, "testCertificate2", "testDescription1",
                BigDecimal.valueOf(15.22), 5,
                LocalDateTime.of(2021, 1, 16, 19, 10),
                LocalDateTime.of(2021, 1, 16, 19, 10), tagDtos);
        List<GiftCertificateDto> giftCertificateDtos = new ArrayList<>();
        giftCertificateDtos.add(giftCertificateDto1);
        UserDto user = new UserDto(1L, "kristina",  Role.USER);
        orderDto = new OrderDto(1L, BigDecimal.valueOf(15.22),
                LocalDateTime.of(2021, 1, 16, 19, 15), giftCertificateDtos, user);
    }

    @AfterEach
    void tearDown() {
    }

    @WithMockUser(username = "mary", roles = {"ADMIN", "USER"})
    @Test
    void purchaseCertificatePositiveTest() throws Exception {
        List<Long> certificates = new ArrayList<>();
        certificates.add(1L);
        PurchaseParam purchaseParam = new PurchaseParam(certificates, 1);
        when(orderService.purchaseCertificate(purchaseParam)).thenReturn(Optional.of(orderDto));
        mvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "    \"certificatesIds\": [" +
                        "      1          " +
                        "    ]," +
                        "    \"userId\" : 1" +
                        "}"))
                .andExpect(status().isCreated());
        verify(orderService).purchaseCertificate(purchaseParam);
    }

    @WithAnonymousUser
    @Test
    void purchaseCertificateNegativeTest() throws Exception {
        mvc.perform(post("/orders")).andExpect(status().isForbidden());
    }

    @WithMockUser(username = "mary", roles = {"ADMIN"})
    @Test
    void findOrderByIdPositiveTest() throws Exception {
        Map<String, Object> orders = new HashMap<>();
        orders.put("time_of_purchase", orderDto.getTimeOfPurchase());
        orders.put("price", orderDto.getPrice());
        when(orderService.findOrderById(1)).thenReturn(Optional.of(orders));
        mvc.perform(get("/orders/1"))
                .andExpect(status().isOk());
        verify(orderService).findOrderById(1);
    }

    @WithMockUser(username = "mary")
    @Test
    void findOrderByIdNegativeTestByUser() throws Exception {
        Map<String, Object> orders = new HashMap<>();
        orders.put("time_of_purchase", orderDto.getTimeOfPurchase());
        orders.put("price", orderDto.getPrice());
        when(orderService.findOrderById(1)).thenReturn(Optional.of(orders));
        mvc.perform(get("/orders/1"))
                .andExpect(status().isForbidden());
    }

    @WithAnonymousUser
    @Test
    void findOrderByIdNegativeTest() throws Exception {
        mvc.perform(get("/orders/1")).andExpect(status().isForbidden());
    }

    @WithMockUser(username = "mary", roles = {"ADMIN"})
    @Test
    void findAllOrdersPositiveTest() throws Exception {
        List<OrderDto>orderDtos = new ArrayList<>();
        orderDtos.add(orderDto);
        when(orderService.findAllOrders(1,5)).thenReturn(orderDtos);
        mvc.perform(get("/orders")
                .param("page", "1")
                .param("size", "5"))
                .andExpect(status().isOk());
        verify(orderService).findAllOrders(1,5);
    }
    @WithMockUser(username = "mary")
    @Test
    void findAllOrdersNegativeTestByUser() throws Exception {
        List<OrderDto>orderDtos = new ArrayList<>();
        orderDtos.add(orderDto);
        when(orderService.findAllOrders(1,5)).thenReturn(orderDtos);
        mvc.perform(get("/orders")
                .param("page", "1")
                .param("size", "5"))
                .andExpect(status().isForbidden());
    }
    @WithAnonymousUser
    @Test
    void findAllOrdersNegativeTest() throws Exception {
        mvc.perform(get("/orders")).andExpect(status().isForbidden());
    }
}