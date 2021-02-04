package com.epam.esm.service.services.impl;

import com.epam.esm.core.repository.TagRepository;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.core.entity.Tag;
import com.epam.esm.service.mapper.TagConverter;
import com.epam.esm.service.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Tag service.
 */
@Service
public class TagServiceImpl implements TagService {

    private TagRepository tagRepository;


    /**
     * Instantiates a new Tag service.
     *
     * @param tagRepository the tag repository
     */
    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }


    @Override
    public Optional<TagDto> create(TagDto tagDto) {
        Tag tag = TagConverter.mapToTag(tagDto);
        tagDto = TagConverter.mapToTagDto(tagRepository.create(tag));
        return Optional.ofNullable(tagDto);
    }

    @Override
    public Optional<TagDto> update(TagDto tagDto) {
        Tag tag = TagConverter.mapToTag(tagDto);
        tag = tagRepository.update(tag);
        return Optional.ofNullable(TagConverter.mapToTagDto(tag));
    }

    @Override
    public void delete(long id) {
        Tag tag = new Tag();
        tag.setId(id);
        tagRepository.delete(tag);
    }

    @Override
    public Optional<TagDto> findTagById(long id) {
        Optional<Tag> tagOptional = tagRepository.findTagById(id);
        if (tagOptional.isPresent()) {
            return tagOptional.map(TagConverter::mapToTagDto);
        }
        return Optional.empty();
    }

    @Override
    public List<TagDto> findAllTags(int page, int size) {
        List<Tag> tags;
        tags = tagRepository.findAllTags(page, size);
        return tags.stream()
                .map(TagConverter::mapToTagDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TagDto> findPopularTag() {
        Tag tag = tagRepository.findPopularTag();
        TagDto tagDto = TagConverter.mapToTagDto(tag);
        return Optional.ofNullable(tagDto);
    }

    @Override
    public Optional<List<TagDto>> findAllTagsByCertificateId(long idCertificate, int page, int size) {
        List<Tag> tags;
        tags = tagRepository.findAllTagsByCertificateId(idCertificate, page, size);
        return Optional.of(tags.stream()
                .map(TagConverter::mapToTagDto)
                .collect(Collectors.toList()));
    }
}

