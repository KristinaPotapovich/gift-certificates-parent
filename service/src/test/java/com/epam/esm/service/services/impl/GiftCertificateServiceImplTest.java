package com.epam.esm.service.services.impl;

import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.repository.GiftCertificateRepository;
import com.epam.esm.core.repository.impl.GiftCertificateRepositoryImpl;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.mapper.GiftCertificateConverter;
import com.epam.esm.service.services.GiftCertificateService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GiftCertificateServiceImplTest {

    private GiftCertificateService giftCertificateService;
    private GiftCertificateRepository giftCertificateRepository;
    private TagDto tagDto;
    private GiftCertificateDto giftCertificateDto1;
    private List<GiftCertificateDto> giftCertificateDtos;
    private List<TagDto> tagDtos;
    private GiftCertificate giftCertificate;
    private List<GiftCertificate> giftCertificates;

    @BeforeAll
    public void setUp() {
        giftCertificateRepository = mock(GiftCertificateRepositoryImpl.class);
        giftCertificateService = new GiftCertificateServiceImpl(giftCertificateRepository);
        giftCertificateDto1 = new GiftCertificateDto(1L, "testCertificate1", "testDescription1",
                BigDecimal.valueOf(15.22), 5,
                LocalDateTime.of(2021, 1, 16, 19, 10),
                LocalDateTime.of(2021, 1, 16, 19, 10), tagDtos);
        tagDto = new TagDto(1L, "testTag");
        giftCertificateDtos = new ArrayList<>();
        giftCertificateDtos.add(giftCertificateDto1);
        tagDtos = new ArrayList<>();
        tagDtos.add(tagDto);
        giftCertificateDto1.setTags(tagDtos);
        giftCertificate = GiftCertificateConverter.mapToGiftCertificate(giftCertificateDto1);
        giftCertificates = new ArrayList<>();
        giftCertificates.add(giftCertificate);
    }

    @AfterAll
    public void tearDown() {
        giftCertificateService = null;
        giftCertificateDto1 = null;
        giftCertificateRepository = null;
        giftCertificateDtos = null;
        tagDto = null;
        tagDtos = null;
        giftCertificate = null;
        giftCertificates = null;
    }

    @Test
    void createPositiveTest() {
        Optional<GiftCertificateDto> expected = Optional.of(giftCertificateDto1);
        when(giftCertificateRepository.create(giftCertificate)).thenReturn(giftCertificate);
        Optional<GiftCertificateDto> actual = giftCertificateService.create(giftCertificateDto1);
        verify(giftCertificateRepository).create(giftCertificate);
        assertTrue(actual.isPresent());
        assertEquals(actual, expected);
    }

    @Test
    void createNegativeTest() {
        when(giftCertificateRepository.create(any(GiftCertificate.class))).thenThrow(ServiceException.class);
        assertThrows(ServiceException.class,
                () -> giftCertificateService.create(giftCertificateDto1));
    }

    @Test
    void updatePositiveTest() {
        when(giftCertificateRepository.update(giftCertificate)).thenReturn(giftCertificate);
        Optional<GiftCertificateDto> actual = giftCertificateService.update(giftCertificateDto1);
        assertTrue(actual.isPresent());
    }

    @Test
    void deletePositiveTest() {
        GiftCertificate giftCertificateForDelete = new GiftCertificate();
        giftCertificateForDelete.setId(1);
        doNothing().when(giftCertificateRepository).delete(giftCertificateForDelete);
        giftCertificateService.delete(giftCertificateForDelete.getId());
        verify(giftCertificateRepository, times(1)).delete(giftCertificateForDelete);
    }

    @Test
    void findCertificateByIdPositiveTest() {
        when(giftCertificateRepository.findCertificateById(1)).thenReturn(giftCertificate);
        Optional<GiftCertificateDto> actual = giftCertificateService.findCertificateById(1);
        assertTrue(actual.isPresent());
    }

    @Test
    void patch() {
        when(giftCertificateRepository.findCertificateById(1)).thenReturn(giftCertificate);
        when(giftCertificateRepository.update(giftCertificate)).thenReturn(giftCertificate);
        GiftCertificateDto giftCertificateDto = new GiftCertificateDto();
        giftCertificateDto.setId(1);
        giftCertificateDto.setName("newCertificate");
        Optional<GiftCertificateDto> actual = giftCertificateService.patch(giftCertificateDto);
        assertTrue(actual.isPresent());
        assertEquals("newCertificate", actual.get().getName());
    }

    @Test
    void patchPositiveTest() {
        when(giftCertificateRepository.findCertificateById(1L)).thenReturn(giftCertificate);
        giftCertificate.setPrice(BigDecimal.valueOf(55));
        when(giftCertificateRepository.update(giftCertificate)).thenReturn(giftCertificate);
        giftCertificateDto1.setPrice(BigDecimal.valueOf(55));
        Optional<GiftCertificateDto> expected = Optional.of(giftCertificateDto1);
        Optional<GiftCertificateDto> actual = giftCertificateService
                .patch(GiftCertificateConverter.mapToGiftCertificateDto(giftCertificate));
        verify(giftCertificateRepository).update(giftCertificate);
        assertTrue(actual.isPresent());
        assertEquals(actual, expected);
    }

    @Test
    void findAllBySeveralTagsPositiveTest() {
        List<Long> tags = new ArrayList<>();
        tags.add(1L);
        when(giftCertificateRepository.findAllBySeveralTags(tags, 1, 3)).thenReturn(giftCertificates);
        Optional<List<GiftCertificateDto>> actual = giftCertificateService.findAllBySeveralTags(tags, 1, 3);
        verify(giftCertificateRepository).findAllBySeveralTags(tags, 1, 3);
        assertTrue(actual.isPresent());
    }
}