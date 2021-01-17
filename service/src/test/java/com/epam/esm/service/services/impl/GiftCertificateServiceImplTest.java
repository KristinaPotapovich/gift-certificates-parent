package com.epam.esm.service.services.impl;

import com.epam.esm.core.entity.GiftCertificate;
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
import com.epam.esm.service.util.impl.GiftCertificateValidation;
import com.epam.esm.service.util.impl.TagValidation;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GiftCertificateServiceImplTest {
    GiftCertificateService giftCertificateService;
    GiftCertificateRepository giftCertificateRepository;
    GiftCertificateValidation giftCertificateValidation;
    GiftCertificateDto giftCertificate1;
    GiftCertificateDto giftCertificate2;
    TagService tagService;
    TagRepository tagRepository;
    TagValidation tagValidation;
    TagDto tag;
    List<GiftCertificateDto> certificates;
    List<TagDto> tags;

    @BeforeAll
    void setUp() {
        giftCertificateRepository = mock(GiftCertificateRepositoryImpl.class);
        tagRepository = mock(TagRepositoryImpl.class);
        tagValidation = new TagValidation();
        tagService = new TagServiceImpl(tagRepository, tagValidation,giftCertificateRepository);
        giftCertificateValidation = new GiftCertificateValidation();
        giftCertificateService = new GiftCertificateServiceImpl(giftCertificateRepository,
                giftCertificateValidation, tagService);
        giftCertificate1 = new GiftCertificateDto();
        giftCertificate1.setId(1);
        giftCertificate1.setName("testCertificate1");
        giftCertificate1.setDescription("testDescription1");
        giftCertificate1.setPrice(15.22);
        giftCertificate1.setDurationInDays(5);
        giftCertificate1.setCreateDate(LocalDateTime.of(2021, 1, 16, 19, 6));
        giftCertificate1.setLastUpdateDate(LocalDateTime.of(2021, 1, 16, 19, 10));
        giftCertificate2 = new GiftCertificateDto();
        giftCertificate2.setId(2);
        giftCertificate2.setName("testCertificate2");
        giftCertificate2.setDescription("testDescription2");
        giftCertificate2.setPrice(22);
        giftCertificate2.setDurationInDays(2);
        giftCertificate2.setCreateDate(LocalDateTime.of(2021, 1, 16, 19, 8));
        giftCertificate2.setLastUpdateDate(LocalDateTime.of(2021, 1, 16, 19, 15));
        tag = new TagDto();
        tag.setId(1);
        tag.setName("testTag");
        certificates = new ArrayList<>();
        certificates.add(giftCertificate1);
        certificates.add(giftCertificate2);
        tags = new ArrayList<>();
        tags.add(tag);
        giftCertificate1.setTags(tags);
        giftCertificate2.setTags(tags);
    }

    @AfterAll
    void tearDown() {
        giftCertificateService = null;
        giftCertificate1 = null;
        giftCertificateValidation = null;
        giftCertificate2 = null;
        giftCertificateRepository = null;
        certificates = null;
        tag = null;
        tagValidation = null;
        tagService = null;
        tagRepository = null;
        tags = null;
    }

    @Test
    void update() throws RepositoryException, ServiceException {
        long id = 1;
        GiftCertificate toGiftCertificate = GiftCertificateConverter.mapToGiftCertificate(giftCertificate1);
        when(giftCertificateRepository.update(toGiftCertificate)).thenReturn(true);
        when(tagRepository.findTagByName("testTag")).thenReturn(Optional.of(TagConverter.mapToTag(tag)));
        doNothing().when(giftCertificateRepository).deleteCertificateAndTagRelation(giftCertificate1.getId());
        giftCertificateService.update(giftCertificate1);
        verify(giftCertificateRepository).update(toGiftCertificate);
        verify(giftCertificateRepository).deleteCertificateAndTagRelation(id);
    }

    @Test
    void delete() throws RepositoryException, ServiceException {
        long id = 1;
        doNothing().when(giftCertificateRepository).deleteCertificateAndTagRelation(giftCertificate1.getId());
        when(giftCertificateRepository.delete(id)).thenReturn(true);
        giftCertificateService.delete(id);
        verify(giftCertificateRepository).delete(id);
    }

    @Test
    void findAll() throws RepositoryException, ServiceException {
        GiftCertificate giftCertificate = GiftCertificateConverter.mapToGiftCertificate(giftCertificate1);
        GiftCertificate giftCertificate3 = GiftCertificateConverter.mapToGiftCertificate(giftCertificate2);
        List<GiftCertificate> giftCertificates = new ArrayList<>();
        giftCertificates.add(giftCertificate);
        giftCertificates.add(giftCertificate3);
        when(giftCertificateRepository.findAll()).thenReturn(giftCertificates);
        Optional<List<GiftCertificateDto>> expected = giftCertificateService.findAll();
        verify(giftCertificateRepository).findAll();
        Assertions.assertTrue(expected.isPresent());
    }

    @Test
    void findCertificateByParam() throws RepositoryException, ServiceException {
        GiftCertificate giftCertificate = GiftCertificateConverter.mapToGiftCertificate(giftCertificate1);
        GiftCertificate giftCertificate3 = GiftCertificateConverter.mapToGiftCertificate(giftCertificate2);
        List<GiftCertificate> giftCertificates = new ArrayList<>();
        giftCertificates.add(giftCertificate);
        giftCertificates.add(giftCertificate3);
        when(giftCertificateRepository.findCertificateByParam(anyString())).thenReturn(giftCertificates);
        Optional<List<GiftCertificateDto>> expected = giftCertificateService.findCertificateByParam(anyString());
        verify(giftCertificateRepository).findCertificateByParam(anyString());
        Assertions.assertTrue(expected.isPresent());
    }
}