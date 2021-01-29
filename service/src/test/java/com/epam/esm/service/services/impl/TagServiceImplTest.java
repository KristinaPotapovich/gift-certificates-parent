package com.epam.esm.service.services.impl;

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
        tagService = new TagServiceImpl(tagRepository, giftCertificateRepository);
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
        when(tagRepository.update(tag)).thenReturn(tag);
        Optional<TagDto> actual = tagService.update(tagDto);
        verify(tagRepository).update(tag);
        assertNotNull(actual);
    }

    @Test
    void delete() throws RepositoryException, ServiceException {
        Tag tagForDelete = new Tag();
        tagForDelete.setId(1);
        doNothing().when(tagRepository).delete(tagForDelete);
        tagService.delete(tagForDelete.getId());
        verify(tagRepository).delete(tagForDelete);
    }

    @Test
    void findAll() throws RepositoryException, ServiceException {
        when(tagRepository.findAll(5, 6)).thenReturn(tags);
        List<TagDto> actual = tagService.findAll(5, 6);
        verify(tagRepository).findAll(5, 6);
        assertFalse(actual.isEmpty());
    }

    @Test
    void findTagById() throws RepositoryException, ServiceException {
        when(tagRepository.findTagById(1)).thenReturn(Optional.of(tag));
        Optional<TagDto> actual = tagService.findTagById(1);
        verify(tagRepository).findTagById(1);
        assertNotNull(actual);
    }

    @Test
    void findPopularTag() throws RepositoryException, ServiceException {
        when(tagRepository.findPopularTag()).thenReturn(tag);
        Optional<TagDto> actual = tagService.findPopularTag();
        verify(tagRepository).findPopularTag();
        assertNotNull(actual);
    }
    @Test
    void findAllTagsByCertificateId() throws RepositoryException, ServiceException {
        when(tagRepository.findAllTagsByCertificateId(1,1,3))
                .thenReturn(tags);
        Optional<List<TagDto>> actual = tagService.findAllTagsByCertificateId(1,1,3);
        verify(tagRepository).findAllTagsByCertificateId(1,1,3);
        assertTrue(actual.isPresent());
    }
}