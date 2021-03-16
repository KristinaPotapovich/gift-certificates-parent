package com.epam.esm.app.controller;

import com.epam.esm.app.config.GiftCertificatesParentApplication;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.services.GiftCertificateService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = GiftCertificatesParentApplication.class)
@AutoConfigureMockMvc
class GiftCertificateControllerTest {
    @MockBean
    private GiftCertificateService giftCertificateService;
    private static final String REQUEST_JSON = "{    " +
            "    \"name\": \"testCertificate1\",  " +
            "      \"description\": \"testDescription1\",   " +
            "       \"price\": 15.22,   " +
            "        \"durationInDays\": 5,  " +
            "        \"createDate\": \"2021-01-16 19:10\",   " +
            "         \"tags\": [      " +
            "               {    " +
            "                   \"id\" : 1,       " +
            "                    \"name\": \"testTag\"     " +
            "             }    " +
            "                 ]" +
            "     }";
    private GiftCertificateDto giftCertificateDto;
    private TagDto tagDto;
    private List<TagDto> tagDtos;

    @Autowired
    private MockMvc mvc;

    @BeforeAll
    public void setUp() {
        tagDto = new TagDto(1L, "testTag");
        tagDtos = new ArrayList<>();
        tagDtos.add(tagDto);
        giftCertificateDto = new GiftCertificateDto(1L, "testCertificate1", "testDescription1",
                BigDecimal.valueOf(15.22), 5,
                LocalDateTime.of(2021, 1, 16, 19, 10),
                null, tagDtos);
    }

    @AfterAll
    public void tearDown() {
        tagDto = null;
        tagDtos = null;
        giftCertificateDto = null;
    }

    @WithMockUser(username = "mary", roles = {"ADMIN"})
    @Test
    void createPositiveTest() throws Exception {
        GiftCertificateDto giftCertificateDto1 = new GiftCertificateDto(0, "testCertificate1", "testDescription1",
                BigDecimal.valueOf(15.22), 5,
                LocalDateTime.of(2021, 1, 16, 19, 10),
                null, tagDtos);
        when(giftCertificateService.create(giftCertificateDto1)).thenReturn(Optional.of(giftCertificateDto));
        mvc.perform(post("/certificates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST_JSON)
                .param("page", "1")
                .param("size", "5"))
                .andExpect(status().isCreated());
        verify(giftCertificateService).create(giftCertificateDto1);
    }

    @WithAnonymousUser
    @Test
    void createGiftCertificateNegativeTest() throws Exception {
        mvc.perform(post("/certificates")).andExpect(status().isForbidden());
    }

    @WithMockUser(username = "mary")
    @Test
    void createGiftCertificateNegativeTestByUser() throws Exception {
        GiftCertificateDto giftCertificateDto1 = new GiftCertificateDto(0, "testCertificate1", "testDescription1",
                BigDecimal.valueOf(15.22), 5,
                LocalDateTime.of(2021, 1, 16, 19, 10),
                null, tagDtos);
        when(giftCertificateService.create(giftCertificateDto1)).thenReturn(Optional.of(giftCertificateDto));
        mvc.perform(post("/certificates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST_JSON)
                .param("page", "1")
                .param("size", "5"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "mary", roles = {"ADMIN"})
    @Test
    void updateGiftCertificatePositiveTest() throws Exception {
        when(giftCertificateService.update(giftCertificateDto)).thenReturn(Optional.of(giftCertificateDto));
        mvc.perform(put("/certificates/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST_JSON)
                .param("page", "1")
                .param("size", "5"))
                .andExpect(status().isCreated());
        verify(giftCertificateService).update(giftCertificateDto);
    }

//    @WithAnonymousUser
//    @Test
//    void updateGiftCertificateNegativeTest() throws Exception {
//        mvc.perform(put("/certificates")).andExpect(status().isForbidden());
//    }
//
//    @WithMockUser(username = "mary")
//    @Test
//    void updateGiftCertificateNegativeTestByUser() throws Exception {
//        when(giftCertificateService.update(giftCertificateDto)).thenReturn(Optional.of(giftCertificateDto));
//        mvc.perform(put("/certificates/1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(REQUEST_JSON)
//                .param("page", "1")
//                .param("size", "5"))
//                .andExpect(status().isForbidden());
//    }
//
//    @WithAnonymousUser
//    @Test
//    void getInformationAboutCertificatesTagsPositiveTest() throws Exception {
//        when(giftCertificateService.getInformationAboutCertificatesTags(1, 1, 5))
//                .thenReturn(Optional.of(tagDtos));
//        mvc.perform(get("/certificates/1/tags")
//                .param("page", "1")
//                .param("size", "5"))
//                .andExpect(status().isOk());
//        verify(giftCertificateService).getInformationAboutCertificatesTags(1, 1, 5);
//    }

    @WithMockUser(username = "mary", roles = {"ADMIN"})
    @Test
    void updateOneFieldGiftCertificatePositiveTest() throws Exception {
        when(giftCertificateService.patch(giftCertificateDto)).thenReturn(Optional.of(giftCertificateDto));
        mvc.perform(patch("/certificates/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST_JSON)
                .param("page", "1")
                .param("size", "5"))
                .andExpect(status().isCreated());
        verify(giftCertificateService).patch(giftCertificateDto);
    }

    @WithMockUser(username = "mary")
    @Test
    void updateOneFieldGiftCertificateNegativeTestByUser() throws Exception {
        when(giftCertificateService.patch(giftCertificateDto)).thenReturn(Optional.of(giftCertificateDto));
        mvc.perform(patch("/certificates/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST_JSON)
                .param("page", "1")
                .param("size", "5"))
                .andExpect(status().isForbidden());
    }

    @WithAnonymousUser
    @Test
    void updateOneFieldGiftCertificateNegativeTest() throws Exception {
        mvc.perform(patch("/certificates")).andExpect(status().isForbidden());
    }

    @WithMockUser(username = "mary", roles = {"ADMIN"})
    @Test
    void deleteGiftCertificatePositiveTest() throws Exception {
        when(giftCertificateService.findCertificateById(1)).thenReturn(Optional.of(giftCertificateDto));
        doNothing().when(giftCertificateService).delete(1);
        mvc.perform(delete("/certificates/1")).andExpect(status().isNoContent());
        verify(giftCertificateService).delete(1);
    }

    @WithMockUser(username = "mary")
    @Test
    void deleteGiftCertificateNegativeTestByUser() throws Exception {
        when(giftCertificateService.findCertificateById(1)).thenReturn(Optional.of(giftCertificateDto));
        doNothing().when(giftCertificateService).delete(1);
        mvc.perform(delete("/certificates/1")).andExpect(status().isForbidden());
    }

    @WithAnonymousUser
    @Test
    void deleteGiftCertificateNegativeTest() throws Exception {
        mvc.perform(delete("/certificates/1")).andExpect(status().isForbidden());
    }
}