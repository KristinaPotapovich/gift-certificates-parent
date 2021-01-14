package com.epam.esm.service.services.impl;

import com.epam.esm.core.exception.RepositoryException;
import com.epam.esm.core.repository.TagRepository;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.core.entity.Tag;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.mapper.TagConverter;
import com.epam.esm.service.services.TagService;
import com.epam.esm.service.util.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private TagRepository tagRepository;

    private Validation<TagDto> tagValidation;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, Validation<TagDto> tagValidation) {
        this.tagRepository = tagRepository;
        this.tagValidation = tagValidation;
    }


    @Override
    public Optional<TagDto> create(TagDto tagDto) throws ServiceException {
        Tag tag;
        try {
            tagValidation.validate(tagDto);
            tag = TagConverter.mapToTag(tagDto);
            return Optional.ofNullable(TagConverter.mapToTagDto(tagRepository.create(tag)));
        } catch (RepositoryException | ValidationException e) {
            throw new ServiceException("TagDto creation failed");
        }
    }

    @Override
    public void update(TagDto tagDto) throws ServiceException {
        Tag tag;
        try {
            tagValidation.validate(tagDto);
            tag = TagConverter.mapToTag(tagDto);
            tagRepository.update(tag);
        } catch (RepositoryException | ValidationException e) {
            throw new ServiceException("Tag update failed");
        }
    }

    @Override
    public boolean delete(long id) throws ServiceException {
        try {
            return tagRepository.delete(id);
        } catch (RepositoryException e) {
            throw new ServiceException("Tag not found");
        }
    }

    @Nullable
    public Optional<TagDto> findTagByName(String name) throws ServiceException {
        TagDto tagDto = new TagDto();
        try {
            tagDto.setName(name);
            tagValidation.validate(tagDto);
            Optional<Tag> tagOptional = tagRepository.findTagByName(name);
            if (tagOptional.isPresent()) {
                return tagOptional.map(TagConverter::mapToTagDto);
            }
        } catch (RepositoryException | ValidationException e) {
            throw new ServiceException("Tag not found " + name);
        }
        return Optional.empty();
    }

    public Optional<List<TagDto>> findAll() throws ServiceException {
        List<Tag> tags;
        try {
            tags = tagRepository.findAll();
        } catch (RepositoryException e) {
            throw new ServiceException("Tags not found");
        }
        return Optional.of(tags.stream()
                .map(TagConverter::mapToTagDto)
                .collect(Collectors.toList()));
    }

    public boolean isTagExistByName(String name) throws ServiceException {
        try {
            return tagRepository.isTagExistByName(name);
        } catch (RepositoryException e) {
            throw new ServiceException("Incorrect data");
        }
    }
}

