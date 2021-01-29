package com.epam.esm.service.services.impl;

import com.epam.esm.core.exception.RepositoryException;
import com.epam.esm.core.repository.GiftCertificateRepository;
import com.epam.esm.core.repository.TagRepository;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.core.entity.Tag;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.mapper.TagConverter;
import com.epam.esm.service.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private GiftCertificateRepository giftCertificateRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, GiftCertificateRepository giftCertificateRepository) {
        this.tagRepository = tagRepository;
        this.giftCertificateRepository = giftCertificateRepository;
    }


    @Override
    public Optional<TagDto> create(TagDto tagDto) throws ServiceException {
        Tag tag;
        try {
            tag = TagConverter.mapToTag(tagDto);
            tagDto = TagConverter.mapToTagDto(tagRepository.create(tag));
            return Optional.ofNullable(tagDto);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Optional<TagDto> update(TagDto tagDto) throws ServiceException {
        Tag tag;
        try {
            tag = TagConverter.mapToTag(tagDto);
            tag = tagRepository.update(tag);
            return Optional.ofNullable(TagConverter.mapToTagDto(tag));
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void delete(long id) throws ServiceException {
        try {
            Tag tag = new Tag();
            tag.setId(id);
            tagRepository.delete(tag);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Optional<TagDto> findTagById(long id) throws ServiceException {
        try {
            Optional<Tag> tagOptional = tagRepository.findTagById(id);
            if (tagOptional.isPresent()) {
                return tagOptional.map(TagConverter::mapToTagDto);
            }
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<TagDto> findAll(int page, int size) throws ServiceException {
        List<Tag> tags;
        try {
            tags = tagRepository.findAll(page, size);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
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
    public Optional<List<TagDto>> findAllTagsByCertificateId(long idCertificate,int page,int size)
            throws ServiceException {
        List<Tag> tags;
        try {
            tags = tagRepository.findAllTagsByCertificateId(idCertificate,page,size);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
        return Optional.of(tags.stream()
                .map(TagConverter::mapToTagDto)
                .collect(Collectors.toList()));
    }

}

