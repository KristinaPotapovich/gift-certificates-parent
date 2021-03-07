package com.epam.esm.app.controller;

import com.epam.esm.app.config.GiftCertificatesParentApplication;
import com.epam.esm.core.entity.Role;
import com.epam.esm.service.dto.*;
import com.epam.esm.service.jwt.JwtUser;
import com.epam.esm.service.services.UserService;
import com.epam.esm.service.services.impl.JwtUserDetailsService;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(MockitoJUnitRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = GiftCertificatesParentApplication.class)
@AutoConfigureMockMvc
class UserControllerTest {
    @MockBean
    private UserService userService;
    @MockBean
    private JwtUserDetailsService jwtUserDetailsService;
    private UserDto userDto;
    private FullInfoUserDto fullInfoUserDto;
    private static final String NAME_CERTIFICATE = "testCertificate2";
    private static final String DESCRIPTION_CERTIFICATE = "testDescription1";
    private static final String LOGIN = "testUser";
    private static final String PASSWORD = "testPassword";
    private static final String URL_ALL_USERS = "/users";
    private static final String URL_USER_ORDER = "/users/1/orders";
    private static final String URL_USER="/users/1";


    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        userDto =
                new UserDto(1, LOGIN,  Role.USER);
        fullInfoUserDto = new FullInfoUserDto(1,LOGIN,PASSWORD,Role.USER);
    }

    @AfterEach
    void tearDown() {
        userDto = null;
    }

    @WithAnonymousUser
    @Test
    void createPositiveTest() throws Exception {
        FullInfoUserDto fullInfoUserDto1 = new FullInfoUserDto();
        fullInfoUserDto1.setLogin(LOGIN);
        fullInfoUserDto1.setPassword(PASSWORD);
        when(userService.create(fullInfoUserDto1)).thenReturn(Optional.of(fullInfoUserDto));
        mvc.perform(post(URL_ALL_USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "        \"login\": \"testUser\"," +
                        "        \"password\": \"testPassword\"" +
                        "}"))
                .andExpect(status().isCreated());
        verify(userService).create(fullInfoUserDto1);
    }

    @WithMockUser(authorities = {"ADMIN"})
    @Test
    void findAllUsersPositiveTest() throws Exception {
        List<FullInfoUserDto> fullInfoUserDtos = new ArrayList<>();
        fullInfoUserDtos.add(fullInfoUserDto);
        when(userService.findAllUsers(1, 5)).thenReturn(fullInfoUserDtos);
        mvc.perform(get(URL_ALL_USERS))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "mary")
    @Test
    void findAllUsersNegativeTestByUser() throws Exception {
        List<FullInfoUserDto> fullInfoUserDtos = new ArrayList<>();
        fullInfoUserDtos.add(fullInfoUserDto);
        when(userService.findAllUsers(1, 5)).thenReturn(fullInfoUserDtos);
        mvc.perform(get(URL_ALL_USERS))
                .andExpect(status().isForbidden());
    }

    @WithAnonymousUser
    @Test
    void findAllUsersNegativeTest() throws Exception {
        mvc.perform(get(URL_ALL_USERS)).andExpect(status().isForbidden());
    }

    @WithMockUser(authorities = {"ADMIN", "USER"})
    @Test
    void getInformationAboutUsersOrdersPositiveTestByUser() throws Exception {
        TagDto tagDto = new TagDto(1L, "tag");
        List<TagDto> tagDtos = new ArrayList<>();
        tagDtos.add(tagDto);
        GiftCertificateDto giftCertificateDto1 = new GiftCertificateDto(1L, NAME_CERTIFICATE,
                DESCRIPTION_CERTIFICATE,
                BigDecimal.valueOf(15.22), 5,
                LocalDateTime.of(2021, 1, 16, 19, 10),
                LocalDateTime.of(2021, 1, 16, 19, 10), tagDtos);
        List<GiftCertificateDto> giftCertificateDtos = new ArrayList<>();
        giftCertificateDtos.add(giftCertificateDto1);
        OrderDto orderDto = new OrderDto(1L, BigDecimal.valueOf(15.22),
                LocalDateTime.of(2021, 1, 16, 19, 15), giftCertificateDtos, userDto);
        List<OrderDto> orderDtos = new ArrayList<>();
        orderDtos.add(orderDto);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Role.USER.toString()));
        JwtUser jwtUser = new JwtUser(1, "mary", "123", authorities);
        when(jwtUserDetailsService.loadUserByUsername("mary")).thenReturn(jwtUser);
        when(userService.getInformationAboutUsersOrders(1, 1, 5)).thenReturn(Optional.of(orderDtos));
        mvc.perform(get(URL_USER_ORDER)
                .param("page", "1")
                .param("size", "5"))
                .andExpect(status().isOk());
        verify(userService).getInformationAboutUsersOrders(1, 1, 5);
    }

    @WithMockUser(username = "mary")
    @Test
    void getInformationAboutUsersOrdersNegativeTestByUser() throws Exception {
        TagDto tagDto = new TagDto(1L, "tag");
        List<TagDto> tagDtos = new ArrayList<>();
        tagDtos.add(tagDto);
        GiftCertificateDto giftCertificateDto1 = new GiftCertificateDto(1L, NAME_CERTIFICATE,
                DESCRIPTION_CERTIFICATE,
                BigDecimal.valueOf(15.22), 5,
                LocalDateTime.of(2021, 1, 16, 19, 10),
                LocalDateTime.of(2021, 1, 16, 19, 10), tagDtos);
        List<GiftCertificateDto> giftCertificateDtos = new ArrayList<>();
        giftCertificateDtos.add(giftCertificateDto1);
        OrderDto orderDto = new OrderDto(1L, BigDecimal.valueOf(15.22),
                LocalDateTime.of(2021, 1, 16, 19, 15), giftCertificateDtos, userDto);
        List<OrderDto> orderDtos = new ArrayList<>();
        orderDtos.add(orderDto);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Role.USER.toString()));
        JwtUser jwtUser = new JwtUser(2, "mary", "123", authorities);
        when(jwtUserDetailsService.loadUserByUsername("mary")).thenReturn(jwtUser);
        when(userService.getInformationAboutUsersOrders(1, 1, 5)).thenReturn(Optional.of(orderDtos));
        mvc.perform(get(URL_USER_ORDER)
                .param("page", "1")
                .param("size", "5"))
                .andExpect(status().isForbidden());
    }

    @WithAnonymousUser
    @Test
    void getInformationAboutUsersOrdersNegativeTest() throws Exception {
        mvc.perform(get(URL_USER_ORDER)).andExpect(status().isForbidden());
    }

    @WithMockUser(authorities = {"ADMIN", "USER"})
    @Test
    void findUserByIdPositiveTestByUser() throws Exception {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Role.USER.toString()));
        JwtUser jwtUser = new JwtUser(1, "mary", "123", authorities);
        when(jwtUserDetailsService.loadUserByUsername("mary")).thenReturn(jwtUser);
        when(userService.findUserById(1)).thenReturn(Optional.of(userDto));
        mvc.perform(get(URL_USER)
                .param("page", "1")
                .param("size", "5"))
                .andExpect(status().isOk());
        verify(userService).findUserById(1);
    }
    @WithMockUser(username = "mary")
    @Test
    void findUserByIdNegativeTestByUser() throws Exception {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Role.USER.toString()));
        JwtUser jwtUser = new JwtUser(2, "mary", "123", authorities);
        when(jwtUserDetailsService.loadUserByUsername("mary")).thenReturn(jwtUser);
        when(userService.findUserById(1)).thenReturn(Optional.of(userDto));
        mvc.perform(get(URL_USER)
                .param("page", "1")
                .param("size", "5"))
                .andExpect(status().isForbidden());
    }
    @WithAnonymousUser
    @Test
    void findUserByIdNegativeTest() throws Exception {
        mvc.perform(get(URL_USER)).andExpect(status().isForbidden());
    }
}