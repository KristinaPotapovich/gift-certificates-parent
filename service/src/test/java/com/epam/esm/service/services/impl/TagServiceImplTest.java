package com.epam.esm.service.services.impl;

import com.epam.esm.core.entity.Tag;
import com.epam.esm.core.repository.TagRepository;
import com.epam.esm.core.repository.impl.TagRepositoryImpl;
import com.epam.esm.service.dto.TagDto;
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
    private TagService tagService;
    private TagRepository tagRepository;
    private TagDto tagDto;
    private Tag tag;
    private List<Tag> tags;

    @BeforeAll
    public void setUp() {
        tagRepository = mock(TagRepositoryImpl.class);
        tagService = new TagServiceImpl(tagRepository);
        tagDto = new TagDto();
        tagDto.setId(1);
        tagDto.setName("testTag");
        tag = TagConverter.mapToTag(tagDto);
        tags = new ArrayList<>();
        tags.add(tag);
    }

    @AfterAll
    public void tearDown() {
        tagRepository = null;
        tagService = null;
        tagDto = null;
        tag = null;
        tags = null;
    }

    @Test
    void create() {
        when(tagRepository.create(tag)).thenReturn(tag);
        Optional<TagDto> actual = tagService.create(tagDto);
        verify(tagRepository).create(tag);
        assertTrue(actual.isPresent());
        assertTrue(actual.get().getId() > 0);
    }

    @Test
    void update() {
        when(tagRepository.update(tag)).thenReturn(tag);
        Optional<TagDto> actual = tagService.update(tagDto);
        verify(tagRepository).update(tag);
        assertTrue(actual.isPresent());
    }

    @Test
    void delete() {
        Tag tagForDelete = new Tag();
        tagForDelete.setId(1);
        doNothing().when(tagRepository).delete(tagForDelete);
        tagService.delete(tagForDelete.getId());
        verify(tagRepository).delete(tagForDelete);
    }

    @Test
    void findAll() {
        when(tagRepository.findAllTags(1, 5)).thenReturn(tags);
        List<TagDto> actual = tagService.findAllTags(1, 5);
        verify(tagRepository).findAllTags(1, 5);
        assertFalse(actual.isEmpty());
        assertEquals(1, actual.get(0).getId());
    }

    @Test
    void findTagById() {
        when(tagRepository.findTagById(1)).thenReturn(Optional.of(tag));
        Optional<TagDto> actual = tagService.findTagById(1);
        verify(tagRepository).findTagById(1);
        assertTrue(actual.isPresent());
    }

    @Test
    void findPopularTag() {
        when(tagRepository.findPopularTag()).thenReturn(tag);
        Optional<TagDto> actual = tagService.findPopularTag();
        verify(tagRepository).findPopularTag();
        assertTrue(actual.isPresent());
    }

    @Test
    void findAllTagsByCertificateId() {
        when(tagRepository.findAllTagsByCertificateId(1, 1, 3))
                .thenReturn(tags);
        Optional<List<TagDto>> actual = tagService.findAllTagsByCertificateId(1, 1, 3);
        verify(tagRepository).findAllTagsByCertificateId(1, 1, 3);
        assertTrue(actual.isPresent());
    }
}