package com.epam.esm.service.services.impl;

import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.entity.Tag;
import com.epam.esm.core.exception.RepositoryException;
import com.epam.esm.core.repository.GiftCertificateRepository;
import com.epam.esm.core.repository.TagRepository;
import com.epam.esm.core.repository.impl.GiftCertificateRepositoryImpl;
import com.epam.esm.core.repository.impl.TagRepositoryImpl;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.mapper.GiftCertificateConverter;
import com.epam.esm.service.mapper.TagConverter;
import com.epam.esm.service.services.GiftCertificateService;
import com.epam.esm.service.services.TagService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestInstance;

import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GiftCertificateServiceImplTest {

    GiftCertificateService giftCertificateService;
    GiftCertificateRepository giftCertificateRepository;
    TagService tagService;
    TagRepository tagRepository;
    TagDto tagDto;
    GiftCertificateDto giftCertificateDto1;
    GiftCertificateDto giftCertificateDto2;
    List<GiftCertificateDto> giftCertificateDtos;
    List<TagDto> tagDtos;
    GiftCertificate giftCertificate;
    GiftCertificate giftCertificate3;
    List<GiftCertificate> giftCertificates;
    Tag tag;
    Validator validator;

    @BeforeAll
    void setUp() {
        giftCertificateRepository = mock(GiftCertificateRepositoryImpl.class);
        tagRepository = mock(TagRepositoryImpl.class);
        tagService = new TagServiceImpl(tagRepository, giftCertificateRepository);
        giftCertificateService = new GiftCertificateServiceImpl(giftCertificateRepository,
                tagService);
        giftCertificateDto1 = new GiftCertificateDto(1L,"testCertificate1","testDescription1",
                BigDecimal.valueOf(15.22), 5,
                LocalDateTime.of(2021, 1, 16, 19, 10),
                LocalDateTime.of(2021, 1, 16, 19, 10),tagDtos);
        giftCertificateDto2 = new GiftCertificateDto(2L,"testCertificate1","testDescription2",
                BigDecimal.valueOf(22), 2,
                LocalDateTime.of(2021, 1, 16, 19, 8),
                LocalDateTime.of(2021, 1, 16, 19, 15),tagDtos);
        tagDto = new TagDto(1L,"testTag");
        giftCertificateDtos = new ArrayList<>();
        giftCertificateDtos.add(giftCertificateDto1);
        giftCertificateDtos.add(giftCertificateDto2);
        tagDtos = new ArrayList<>();
        tagDtos.add(tagDto);
        giftCertificateDto1.setTags(tagDtos);
        giftCertificateDto2.setTags(tagDtos);
        giftCertificate = GiftCertificateConverter.mapToGiftCertificate(giftCertificateDto1);
        giftCertificate3 = GiftCertificateConverter.mapToGiftCertificate(giftCertificateDto2);
        giftCertificates = new ArrayList<>();
        giftCertificates.add(giftCertificate);
        giftCertificates.add(giftCertificate3);
        tag = TagConverter.mapToTag(tagDto);
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @AfterAll
    void tearDown() {
        giftCertificateService = null;
        giftCertificateDto1 = null;
        giftCertificateDto2 = null;
        giftCertificateRepository = null;
        giftCertificateDtos = null;
        tagDto = null;
        tagService = null;
        tagRepository = null;
        tagDtos = null;
        giftCertificate = null;
        giftCertificate3 = null;
        giftCertificates = null;
        tag = null;
    }

    @Test
    void createPositiveTest() throws RepositoryException, ServiceException {
        Optional<GiftCertificateDto> expected = Optional.of(giftCertificateDto1);
        when(giftCertificateRepository.create(giftCertificate)).thenReturn(giftCertificate);
        Optional<GiftCertificateDto> actual = giftCertificateService.create(giftCertificateDto1);
        verify(giftCertificateRepository).create(giftCertificate);
        assertEquals(actual, expected);
    }

    @Test
    void updatePositiveTest() throws RepositoryException, ServiceException {
        Optional<GiftCertificateDto> expected = Optional.of(giftCertificateDto1);
        when(giftCertificateRepository.update(giftCertificate)).thenReturn(giftCertificate);
        Optional<GiftCertificateDto> actual = giftCertificateService.update(giftCertificateDto1);
        assertEquals(actual, expected);
    }

    @Test
    void deletePositiveTest() throws RepositoryException, ServiceException {
        GiftCertificate giftCertificateForDelete = new GiftCertificate();
        giftCertificateForDelete.setId(1);
        doNothing().when(giftCertificateRepository).delete(giftCertificateForDelete);
        giftCertificateService.delete(giftCertificateForDelete.getId());
        verify(giftCertificateRepository).delete(giftCertificateForDelete);
    }

    @Test
    void findAllPositiveTest() throws RepositoryException, ServiceException {
        when(giftCertificateRepository.findAll(1, 3)).thenReturn(giftCertificates);
        List<GiftCertificateDto> expected = giftCertificateDtos;
        List<GiftCertificateDto> actual = giftCertificateService.findAll(1, 3);
        assertEquals(actual, expected);
        verify(giftCertificateRepository).findAll(1, 3);
    }

    @Test
    void findCertificateByParamPositiveTest() throws RepositoryException, ServiceException {
        when(giftCertificateRepository.findCertificateByParam("test", 1, 3))
                .thenReturn(giftCertificates);
        Optional<List<GiftCertificateDto>> expected = Optional.of(giftCertificateDtos);
        Optional<List<GiftCertificateDto>> actual = giftCertificateService
                .findCertificateByParam("test", 1, 3);
        assertEquals(actual, expected);
        verify(giftCertificateRepository).findCertificateByParam("test", 1, 3);
    }

    @Test
    void findCertificateByIdPositiveTest() throws RepositoryException, ServiceException {
        when(giftCertificateRepository.findCertificateById(1)).thenReturn(giftCertificate);
        Optional<GiftCertificateDto> expected = Optional.of(giftCertificateDto1);
        Optional<GiftCertificateDto> actual = giftCertificateService.findCertificateById(1);
        assertEquals(actual, expected);
    }

    @Test
    void searchAllCertificatesByTagNamePositiveTest() throws RepositoryException {
        when(giftCertificateRepository.searchAllCertificatesByTagName("testTag", 1, 3))
                .thenReturn(giftCertificates);
        Optional<List<GiftCertificateDto>> expected = Optional.of(giftCertificateDtos);
        Optional<List<GiftCertificateDto>> actual = giftCertificateService
                .searchAllCertificatesByTagName("testTag", 1, 3);
        assertEquals(actual, expected);
        verify(giftCertificateRepository).searchAllCertificatesByTagName("testTag", 1, 3);
    }

    @Test
    void patchPositiveTest() throws RepositoryException, ServiceException {
        when(giftCertificateRepository.findCertificateById(1L)).thenReturn(giftCertificate);
        giftCertificate.setPrice(BigDecimal.valueOf(55));
        when(giftCertificateRepository.update(giftCertificate)).thenReturn(giftCertificate);
        giftCertificateDto1.setPrice(BigDecimal.valueOf(55));
        Optional<GiftCertificateDto> expected = Optional.of(giftCertificateDto1);
        Optional<GiftCertificateDto> actual = giftCertificateService
                .patch(GiftCertificateConverter.mapToGiftCertificateDto(giftCertificate));
        verify(giftCertificateRepository).update(giftCertificate);
        assertEquals(actual,expected);
    }

    @Test
    void findAllBySeveralTagsPositiveTest() throws RepositoryException, ServiceException {
        List<Long> tags = new ArrayList<>();
        tags.add(1L);
        when(giftCertificateRepository.findAllBySeveralTags(tags, 1, 3)).thenReturn(giftCertificates);
        Optional<List<GiftCertificateDto>> expected = Optional.of(giftCertificateDtos);
        Optional<List<GiftCertificateDto>> actual = giftCertificateService.findAllBySeveralTags(tags, 1, 3);
        assertEquals(actual, expected);
        verify(giftCertificateRepository).findAllBySeveralTags(tags, 1, 3);
    }
}