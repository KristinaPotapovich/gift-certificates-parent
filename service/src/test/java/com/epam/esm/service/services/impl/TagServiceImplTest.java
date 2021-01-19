package com.epam.esm.service.services.impl;

import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.entity.Tag;
import com.epam.esm.core.exception.RepositoryException;
import com.epam.esm.core.repository.GiftCertificateRepository;
import com.epam.esm.core.repository.TagRepository;
import com.epam.esm.core.repository.impl.GiftCertificateRepositoryImpl;
import com.epam.esm.core.repository.impl.TagRepositoryImpl;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.mapper.TagConverter;
import com.epam.esm.service.services.TagService;
import com.epam.esm.service.util.impl.TagValidation;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TagServiceImplTest {
    TagService tagService;
    TagRepository tagRepository;
    TagValidation tagValidation;
    TagDto tagDto;
    TagDto tagDto2;
    List<TagDto> tagDtos;
    GiftCertificateRepository giftCertificateRepository;
    Tag tag;
    Tag tag1;
    List<Tag> tags;

    @BeforeAll
    void setUp() {
        giftCertificateRepository = mock(GiftCertificateRepositoryImpl.class);
        tagRepository = mock(TagRepositoryImpl.class);
        tagValidation = new TagValidation();
        tagService = new TagServiceImpl(tagRepository, tagValidation, giftCertificateRepository);
        tagDto = new TagDto();
        tagDto.setId(1);
        tagDto.setName("testTag");
        tagDto2 = new TagDto();
        tagDto2.setId(2);
        tagDto2.setName("testTag2");
        tag = TagConverter.mapToTag(tagDto);
        tag1 = TagConverter.mapToTag(tagDto2);
        tagDtos = new ArrayList<>();
        tagDtos.add(tagDto);
        tagDtos.add(tagDto2);
        tags = new ArrayList<>();
        tags.add(tag);
        tags.add(tag1);
    }

    @AfterAll
    void tearDown() {
        giftCertificateRepository = null;
        tagRepository = null;
        tagValidation = null;
        tagService = null;
    }

    @Test
    void create() throws RepositoryException, ServiceException {
        when(tagRepository.create(tag)).thenReturn(tag);
        Optional<TagDto> actual = tagService.create(tagDto);
        verify(tagRepository).create(tag);
        assertNotNull(actual);
    }

    @Test
    void update() throws RepositoryException, ServiceException {
        when(tagRepository.update(tag)).thenReturn(true);
        tagService.update(tagDto);
        verify(tagRepository).update(tag);
    }

    @Test
    void delete() throws RepositoryException, ServiceException {
        when(giftCertificateRepository.delete(anyLong())).thenReturn(true);
        when(tagRepository.delete(tag.getId())).thenReturn(true);
        tagService.delete(tag.getId());
        verify(tagRepository).delete(tag.getId());
    }

    @Test
    void findTagByName() throws RepositoryException, ServiceException {
        when(tagRepository.findTagByName(tag.getName())).thenReturn(Optional.of(tag));
        Optional<TagDto>actual = tagService.findTagByName(tagDto.getName());
        verify(tagRepository).findTagByName(tag.getName());
        assertNotNull(actual.isPresent());
    }

    @Test
    void findAll() throws RepositoryException, ServiceException {
        when(tagRepository.findAll()).thenReturn(tags);
        Optional<List<TagDto>>actual = tagService.findAll();
        verify(tagRepository).findAll();
        assertTrue(actual.isPresent());
    }

    @Test
    void findAllTagsByCertificateId() throws RepositoryException, ServiceException {
        when(tagRepository.findAllTagsByCertificateId(anyLong())).thenReturn(tags);
        Optional<List<TagDto>>actual = tagService.findAllTagsByCertificateId(anyLong());
        verify(tagRepository).findAllTagsByCertificateId(anyLong());
        assertTrue(actual.isPresent());
    }
}