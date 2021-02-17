package com.epam.esm.service.services.impl;

import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.entity.Tag;
import com.epam.esm.core.repository.GiftCertificateRepository;
import com.epam.esm.core.repository.impl.GiftCertificateRepositoryImpl;
import com.epam.esm.core.repository.specification.BaseSpecificationForSorting;
import com.epam.esm.core.repository.specification.ResolverForSearchParams;
import com.epam.esm.core.repository.specification.impl.SortingDateSpecification;
import com.epam.esm.core.repository.specification.impl.SortingNameSpecification;
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
    }

    @Test
    void findCertificateByIdPositiveTest() {
        when(giftCertificateRepository.findCertificateById(1)).thenReturn(giftCertificate);
        Optional<GiftCertificateDto> actual = giftCertificateService.findCertificateById(1);
        assertTrue(actual.isPresent());
    }

    @Test
    void findAllCertificatesSortByName() {
        List<String> tags = new ArrayList<>();
        tags.add("testTag");
        ResolverForSearchParams resolverForSearchParams = new ResolverForSearchParams(tags,"test");
        BaseSpecificationForSorting<GiftCertificate> baseSpecificationForSorting =
                new SortingNameSpecification("asc");
        when(giftCertificateRepository
                .findAllCertificates(resolverForSearchParams, baseSpecificationForSorting, 1, 5))
                .thenReturn(giftCertificates);
        List<GiftCertificateDto> certificateDtos = giftCertificateService
                .findAllCertificates("test", "name", tags, "asc", 1, 5);
        assertNotNull(certificateDtos);
    }
    @Test
    void findAllCertificatesSortByDate() {
        List<String> tags = new ArrayList<>();
        tags.add("testTag");
        ResolverForSearchParams resolverForSearchParams = new ResolverForSearchParams(tags,"test");
        BaseSpecificationForSorting<GiftCertificate> baseSpecificationForSorting =
                new SortingDateSpecification("asc");
        when(giftCertificateRepository
                .findAllCertificates(resolverForSearchParams, baseSpecificationForSorting, 1, 5))
                .thenReturn(giftCertificates);
        List<GiftCertificateDto> certificateDtos = giftCertificateService
                .findAllCertificates("test", "date", tags, "asc", 1, 5);
        assertNotNull(certificateDtos);
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
    void getInformationAboutCertificatesTags() {
        Tag tag = new Tag();
        tagDto.setId(1);
        tag.setName("testTag");
        List<Tag> tags = new ArrayList<>();
        tags.add(tag);
        when(giftCertificateRepository.getInformationAboutCertificatesTags(1, 1, 3))
                .thenReturn(tags);
        Optional<List<TagDto>> actual =
                giftCertificateService.getInformationAboutCertificatesTags(1, 1, 3);
        verify(giftCertificateRepository).getInformationAboutCertificatesTags(1, 1, 3);
        assertTrue(actual.isPresent());
    }
}