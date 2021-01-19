package com.epam.esm.service.services.impl;

import com.epam.esm.core.exception.RepositoryException;
import com.epam.esm.core.repository.GiftCertificateRepository;
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

/**
 * The type Tag service.
 */
@Service
public class TagServiceImpl implements TagService {

    private TagRepository tagRepository;

    private Validation<TagDto> tagValidation;
    private GiftCertificateRepository giftCertificateRepository;

    /**
     * Instantiates a new Tag service.
     *
     * @param tagRepository             the tag repository
     * @param tagValidation             the tag validation
     * @param giftCertificateRepository the gift certificate repository
     */
    @Autowired
    public TagServiceImpl(TagRepository tagRepository, Validation<TagDto> tagValidation,
                          GiftCertificateRepository giftCertificateRepository) {
        this.tagRepository = tagRepository;
        this.tagValidation = tagValidation;
        this.giftCertificateRepository = giftCertificateRepository;
    }


    @Override
    public Optional<TagDto> create(TagDto tagDto) throws ServiceException {
        Tag tag;
        try {
            tagValidation.validate(tagDto);
            tag = TagConverter.mapToTag(tagDto);
            tagDto = TagConverter.mapToTagDto(tagRepository.create(tag));
            return Optional.ofNullable(tagDto);
        } catch (RepositoryException | ValidationException e) {
            throw new ServiceException(e.getMessage());
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
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public boolean delete(long id) throws ServiceException {
        try {
            giftCertificateRepository.deleteCertificateAndTagRelation(id);
            return tagRepository.delete(id);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
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
            throw new ServiceException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<TagDto>> findAll() throws ServiceException {
        List<Tag> tags;
        try {
            tags = tagRepository.findAll();
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
        return Optional.of(tags.stream()
                .map(TagConverter::mapToTagDto)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<List<TagDto>> findAllTagsByCertificateId(long idCertificate) throws ServiceException {
        List<Tag> tags;
        try {
            tags = tagRepository.findAllTagsByCertificateId(idCertificate);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
        return Optional.of(tags.stream()
                .map(TagConverter::mapToTagDto)
                .collect(Collectors.toList()));
    }
}

