package com.epam.esm.app.controller;

import com.epam.esm.app.config.GiftCertificatesParentApplication;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.services.TagService;
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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(MockitoJUnitRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = GiftCertificatesParentApplication.class)
@AutoConfigureMockMvc
class TagControllerTest {
    @MockBean
    private TagService tagService;
    private TagDto tagDto;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        tagDto = new TagDto(1, "testTag");
    }

    @AfterEach
    void tearDown() {
        tagDto = null;
    }

    @WithMockUser(authorities = {"ADMIN", "USER"})
    @Test
    void findTagByIdPositiveTest() throws Exception {
        when(tagService.findTagById(1)).thenReturn(Optional.of(tagDto));
        mvc.perform(get("/tags/1"))
                .andExpect(status().isOk());
        verify(tagService).findTagById(1);
    }

    @WithAnonymousUser
    @Test
    void findTagByIdNegativeTest() throws Exception {
        mvc.perform(get("/tags/1")).andExpect(status().isForbidden());
    }

    @WithAnonymousUser
    @Test
    void findAllTags() throws Exception {
        List<TagDto> tagDtos = new ArrayList<>();
        tagDtos.add(tagDto);
        when(tagService.findAllTags(1, 5)).thenReturn(tagDtos);
        mvc.perform(get("/tags")
                .param("page", "1")
                .param("size", "5"))
                .andExpect(status().isOk());
        verify(tagService).findAllTags(1, 5);
    }

    @WithMockUser(authorities = {"ADMIN"})
    @Test
    void createTagPositiveTest() throws Exception {
        when(tagService.create(tagDto)).thenReturn(Optional.of(tagDto));
        mvc.perform(post("/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "    \"id\": " +
                        "      1," +
                        "    \"name\" : \"testTag\"" +
                        "}"))
                .andExpect(status().isCreated());
        verify(tagService).create(tagDto);
    }

    @WithMockUser(username = "mary")
    @Test
    void createTagNegativeTestByUser() throws Exception {
        when(tagService.create(tagDto)).thenReturn(Optional.of(tagDto));
        mvc.perform(post("/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "    \"id\": " +
                        "      1," +
                        "    \"name\" : \"testTag\"" +
                        "}"))
                .andExpect(status().isForbidden());
    }

    @WithAnonymousUser
    @Test
    void createTagNegativeTest() throws Exception {
        mvc.perform(post("/tags")).andExpect(status().isForbidden());
    }

    @WithMockUser(authorities = {"ADMIN"})
    @Test
    void updateTagPositiveTest() throws Exception {
        when(tagService.update(tagDto)).thenReturn(Optional.of(tagDto));
        mvc.perform(put("/tags/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\" : \"testTag\"}"))
                .andExpect(status().isCreated());
        verify(tagService).update(tagDto);
    }

    @WithMockUser(username = "mary")
    @Test
    void updateTagNegativeTestByUser() throws Exception {
        when(tagService.update(tagDto)).thenReturn(Optional.of(tagDto));
        mvc.perform(put("/tags/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\" : \"testTag\"}"))
                .andExpect(status().isForbidden());
    }

    @WithAnonymousUser
    @Test
    void updateTagNegativeTest() throws Exception {
        mvc.perform(put("/tags")).andExpect(status().isForbidden());
    }

    @WithMockUser(authorities = {"ADMIN"})
    @Test
    void deleteTagPositiveTest() throws Exception {
        when(tagService.findTagById(1)).thenReturn(Optional.of(tagDto));
        doNothing().when(tagService).delete(1);
        mvc.perform(delete("/tags/1"))
                .andExpect(status().isNoContent());
        verify(tagService).delete(1);
    }

    @WithMockUser(username = "mary")
    @Test
    void deleteTagNegativeTestByUser() throws Exception {
        when(tagService.findTagById(1)).thenReturn(Optional.of(tagDto));
        doNothing().when(tagService).delete(1);
        mvc.perform(delete("/tags/1"))
                .andExpect(status().isForbidden());
    }

    @WithAnonymousUser
    @Test
    void deleteTagNegativeTest() throws Exception {
        mvc.perform(delete("/tags/1")).andExpect(status().isForbidden());
    }

    @WithAnonymousUser
    @Test
    void findPopularTag() throws Exception {
        when(tagService.findPopularTag()).thenReturn(Optional.of(tagDto));
        mvc.perform(get("/tags/popular-tag"))
                .andExpect(status().isOk());
        verify(tagService).findPopularTag();
    }
}