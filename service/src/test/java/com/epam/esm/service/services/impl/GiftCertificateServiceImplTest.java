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
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GiftCertificateServiceImplTest {
    GiftCertificateService giftCertificateService;
    GiftCertificateRepository giftCertificateRepository;
    GiftCertificateDto giftCertificateDto1;
    GiftCertificateDto giftCertificateDto2;
    TagService tagService;
    TagRepository tagRepository;
    TagDto tagDto;
    List<GiftCertificateDto> giftCertificateDtos;
    List<TagDto> tagDtos;
    GiftCertificate giftCertificate;
    GiftCertificate giftCertificate3;
    List<GiftCertificate> giftCertificates;
    Tag tag;

    @BeforeAll
    void setUp() {
        giftCertificateRepository = mock(GiftCertificateRepositoryImpl.class);
        tagRepository = mock(TagRepositoryImpl.class);
        tagService = new TagServiceImpl(tagRepository, giftCertificateRepository);
        giftCertificateService = new GiftCertificateServiceImpl(giftCertificateRepository,
               tagService);
        giftCertificateDto1 = new GiftCertificateDto();
        giftCertificateDto1.setId(1);
        giftCertificateDto1.setName("testCertificate1");
        giftCertificateDto1.setDescription("testDescription1");
        giftCertificateDto1.setPrice(BigDecimal.valueOf(15.22));
        giftCertificateDto1.setDurationInDays(5);
        giftCertificateDto1.setCreateDate(LocalDateTime.of(2021, 1, 16, 19, 6));
        giftCertificateDto1.setLastUpdateDate(LocalDateTime.of(2021, 1, 16, 19, 10));
        giftCertificateDto2 = new GiftCertificateDto();
        giftCertificateDto2.setId(2);
        giftCertificateDto2.setName("testCertificate2");
        giftCertificateDto2.setDescription("testDescription2");
        giftCertificateDto2.setPrice(BigDecimal.valueOf(22));
        giftCertificateDto2.setDurationInDays(2);
        giftCertificateDto2.setCreateDate(LocalDateTime.of(2021, 1, 16, 19, 8));
        giftCertificateDto2.setLastUpdateDate(LocalDateTime.of(2021, 1, 16, 19, 15));
        tagDto = new TagDto();
        tagDto.setId(1);
        tagDto.setName("testTag");
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
    void update() throws RepositoryException, ServiceException {
        doNothing().when(giftCertificateRepository).update(giftCertificate);
        when(tagRepository.findTagByName("testTag")).thenReturn(Optional.of(TagConverter.mapToTag(tagDto)));
        giftCertificateService.update(giftCertificateDto1);
        verify(giftCertificateRepository).update(giftCertificate);
    }

    @Test
    void delete() throws RepositoryException, ServiceException {
        doNothing().when(giftCertificateRepository).delete(giftCertificate);
        giftCertificateService.delete(1);
        verify(giftCertificateRepository).delete(giftCertificate);
    }

    @Test
    void findAll() throws RepositoryException, ServiceException {
        giftCertificates.add(giftCertificate);
        giftCertificates.add(giftCertificate3);
        when(giftCertificateRepository.findAll(5,6)).thenReturn(giftCertificates);
        Optional<List<GiftCertificateDto>> actual = giftCertificateService.findAll(5,6);
        verify(giftCertificateRepository).findAll(5,6);
        Assertions.assertTrue(actual.isPresent());
    }

    @Test
    void findCertificateByParam() throws RepositoryException, ServiceException {
        GiftCertificate giftCertificate = GiftCertificateConverter.mapToGiftCertificate(giftCertificateDto1);
        GiftCertificate giftCertificate3 = GiftCertificateConverter.mapToGiftCertificate(giftCertificateDto2);
        List<GiftCertificate> giftCertificates = new ArrayList<>();
        giftCertificates.add(giftCertificate);
        giftCertificates.add(giftCertificate3);
        when(giftCertificateRepository.findCertificateByParam(anyString(),anyInt(),anyInt())).thenReturn(giftCertificates);
        Optional<List<GiftCertificateDto>> actual = giftCertificateService.findCertificateByParam(anyString(),anyInt(),anyInt());
        verify(giftCertificateRepository).findCertificateByParam(anyString(),anyInt(),anyInt());
        Assertions.assertTrue(actual.isPresent());
    }

    @Test
    void findCertificateById() throws ServiceException, RepositoryException {
        Tag tag = TagConverter.mapToTag(tagDto);
        GiftCertificate giftCertificate = GiftCertificateConverter.mapToGiftCertificate(giftCertificateDto1);
        when(tagRepository.findTagByName(giftCertificate.getName())).thenReturn(Optional.of(TagConverter.mapToTag(tagDto)));
        when(tagRepository.create(TagConverter.mapToTag(tagDto))).thenReturn(tag);
        when(giftCertificateRepository.findCertificateById(anyLong()))
                .thenReturn(giftCertificate);
        Optional<GiftCertificateDto> actual = giftCertificateService.findCertificateById(giftCertificateDto1.getId());
        verify(giftCertificateRepository).findCertificateById(anyLong());
        Assertions.assertNotNull(actual);
    }

    @Test
    void searchAllCertificatesByTagName() throws RepositoryException {
        when(giftCertificateRepository.searchAllCertificatesByTagName(tag.getName(),5,6)).thenReturn(giftCertificates);
        when(tagRepository.findTagByName(giftCertificate.getName()))
                .thenReturn(Optional.of(TagConverter.mapToTag(tagDto)));
        when(tagRepository.create(TagConverter.mapToTag(tagDto))).thenReturn(tag);
        Optional<List<GiftCertificateDto>> actual = giftCertificateService
                .searchAllCertificatesByTagName(tag.getName(),5,6);
        verify(giftCertificateRepository).searchAllCertificatesByTagName(tag.getName(),5,6);
        Assertions.assertTrue(actual.isPresent());
    }

//    @Test
//    void sortByParamPositiveTest() throws RepositoryException, ServiceException {
//        OrderBySpecification orderBySpecification =
//        when(giftCertificateRepository.sortByParam(sortByParamSpecification)).thenReturn(giftCertificates);
//        when(tagRepository.findTagByName(giftCertificate.getName()))
//                .thenReturn(Optional.of(TagConverter.mapToTag(tagDto)));
//        when(tagRepository.create(TagConverter.mapToTag(tagDto))).thenReturn(tag);
//        Optional<List<GiftCertificateDto>> actual = giftCertificateService
//                .sortByParam("name","desc");
//        Assertions.assertTrue(actual.isPresent());
//    }
}