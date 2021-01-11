package com.epam.esm.service.impl;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.mapper.TagConverter;
import com.epam.esm.repository.impl.TagRepository;
import com.epam.esm.service.BaseService;
import com.epam.esm.util.TagValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagService implements BaseService<TagDto> {

    private final TagRepository tagRepository;
    private final TagValidation tagValidation;

    @Autowired
    public TagService(TagRepository tagRepository, TagValidation tagValidation) {
        this.tagRepository = tagRepository;
        this.tagValidation = tagValidation;
    }

    @Override
    public Optional<TagDto> create(TagDto tagDto) {
        Tag tag;
        tagValidation.isRightTagName(tagDto.getName());
        tagValidation.isEmptyName(tagDto.getName());
        tag = TagConverter.mapToTag(tagDto);
        tagDto = TagConverter.mapToTagDto(tagRepository.create(tag));
        return Optional.ofNullable(tagDto);
    }

    @Override
    public Optional<TagDto> update(TagDto tagDto) {
        Tag tag;
        tagValidation.isRightTagName(tagDto.getName());
        tagValidation.isEmptyName(tagDto.getName());
        tag = TagConverter.mapToTag(tagDto);
        if (isTagExistById(tagDto.getId())) {
            tagRepository.update(tag);
            tagDto = TagConverter.mapToTagDto(tag);
        }
        return Optional.ofNullable(tagDto);
    }

    @Override
    public boolean delete(long id) {
        boolean result;
        if (isTagExistById(id)) {
            tagRepository.delete(id);
            result = true;
        } else {
            throw new ServiceException("Tag not found");
        }
        return result;
    }

    public Optional<TagDto> findTagByName(String name) {
        TagDto tagDto = null;
        tagValidation.isRightTagName(name);
        tagValidation.isEmptyName(name);
       if (isTagExistByName(name)) {
           tagDto = TagConverter.mapToTagDto(tagRepository.findTagByName(name));
       }
        return Optional.ofNullable(tagDto);
    }

    public Optional<List<TagDto>> findAllTags() {
        List<Tag> tags = tagRepository.findAllTags();
        return Optional.of(tags.stream()
                .map(TagConverter::mapToTagDto)
                .collect(Collectors.toList()));
    }

    public boolean isTagExistById(long id) {
        return tagRepository.isTagExistById(id);
    }
    public boolean isTagExistByName(String name) {
        return tagRepository.isTagExistByName(name);
    }
}

