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
            if (tagRepository.isTagExistByName(tag.getName())) {
                tagDto = TagConverter.mapToTagDto(tagRepository.create(tag));
            } else {
                throw new ServiceException("Tag is exists");
            }
        } catch (RepositoryException | ValidationException e) {
            throw new ServiceException("TagDto creation failed");
        }
        return Optional.ofNullable(tagDto);
    }

    @Override
    public Optional<TagDto> update(TagDto tagDto) throws ServiceException {
        Tag tag;
        try {
            tagValidation.validate(tagDto);
            tag = TagConverter.mapToTag(tagDto);
            tag = tagRepository.update(tag);
            tagDto = TagConverter.mapToTagDto(tag);
        } catch (RepositoryException | ValidationException e) {
            throw new ServiceException("Tag update failed");
        }
        return Optional.ofNullable(tagDto);
    }

    @Override
    public boolean delete(long id) throws ServiceException {
        try {
            return tagRepository.delete(id);
        } catch (RepositoryException e) {
            throw new ServiceException("Tag not found");
        }
    }

    public Optional<TagDto> findTagByName(String name) throws ServiceException {
        TagDto tagDto = new TagDto();
        try {
            tagDto.setName(name);
            tagValidation.validate(tagDto);
            tagDto = TagConverter.mapToTagDto(tagRepository.findTagByName(name));
        } catch (RepositoryException | ValidationException e) {
            throw new ServiceException("Tag not found" + name);
        }
        return Optional.ofNullable(tagDto);
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

