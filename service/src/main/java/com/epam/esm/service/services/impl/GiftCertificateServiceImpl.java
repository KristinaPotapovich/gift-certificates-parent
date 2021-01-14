package com.epam.esm.service.services.impl;

import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.entity.Tag;
import com.epam.esm.core.exception.RepositoryException;
import com.epam.esm.core.repository.GiftCertificateRepository;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.mapper.GiftCertificateConverter;
import com.epam.esm.service.mapper.TagConverter;
import com.epam.esm.service.services.GiftCertificateService;
import com.epam.esm.service.services.TagService;
import com.epam.esm.service.util.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private GiftCertificateRepository giftCertificateRepositoryImpl;
    private Validation<GiftCertificateDto> giftCertificateValidation;
    private TagService tagService;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository giftCertificateRepositoryImpl,
                                      Validation<GiftCertificateDto> giftCertificateValidation,
                                      TagService tagService) {
        this.giftCertificateRepositoryImpl = giftCertificateRepositoryImpl;
        this.giftCertificateValidation = giftCertificateValidation;
        this.tagService = tagService;
    }

    @Transactional
    @Override
    public Optional<GiftCertificateDto> create(GiftCertificateDto giftCertificateDto) throws ServiceException {
        GiftCertificate giftCertificate;
        try {
            giftCertificateValidation.validate(giftCertificateDto);
            giftCertificate = GiftCertificateConverter.mapToGiftCertificate(giftCertificateDto);
            GiftCertificate createdGiftCertificate = giftCertificateRepositoryImpl.create(giftCertificate);
            giftCertificateDto = GiftCertificateConverter
                    .mapToGiftCertificateDto(createdGiftCertificate);
            List<Tag> tags = giftCertificate.getTags();
            List<TagDto> tagDtos = createTagsAndRelations(giftCertificateDto, tags);
            giftCertificateDto.setTags(tagDtos);
        } catch (RepositoryException | ValidationException e) {
            throw new ServiceException("Gift certificate creation failed");
        }
        return Optional.of(giftCertificateDto);
    }

    private List<TagDto> createTagsAndRelations(GiftCertificateDto giftCertificateDto, List<Tag> tags) throws ServiceException, RepositoryException {
        List<TagDto> tagDtos = new ArrayList<>();
        for (Tag tag : tags) {
            Optional<TagDto> tagDtoOptional = tagService.findTagByName(tag.getName());
            if (!tagDtoOptional.isPresent()) {
                tagDtoOptional = tagService.create(TagConverter.mapToTagDto(tag));
                TagDto tagDto = tagDtoOptional.orElseThrow(() -> new ServiceException("Tag creation error"));
                giftCertificateRepositoryImpl
                        .createCertificateAndTagRelationship(giftCertificateDto.getId(), tagDto.getId());
                tagDtos.add(tagDto);
            } else {
                tagDtos.add(tagDtoOptional.get());
            }
        }
        return tagDtos;
    }

    @Override
    public void update(GiftCertificateDto giftCertificateDto) throws ServiceException {
        GiftCertificate giftCertificate;
        try {
            giftCertificateValidation.validate(giftCertificateDto);
            giftCertificate = GiftCertificateConverter.mapToGiftCertificate(giftCertificateDto);
            giftCertificateRepositoryImpl.update(giftCertificate);
        } catch (ValidationException | RepositoryException e) {
            throw new ServiceException("Gift certificate update failed");
        }
    }

    @Override
    public boolean delete(long id) throws ServiceException {
        try {
            return giftCertificateRepositoryImpl.delete(id);
        } catch (RepositoryException e) {
            throw new ServiceException("Gift certificates delete failed");
        }
    }

    @Override
    public Optional<List<GiftCertificateDto>> findAll() throws ServiceException {
        List<GiftCertificate> giftCertificates;
        try {
            giftCertificates = giftCertificateRepositoryImpl.findAll();
        } catch (RepositoryException e) {
            throw new ServiceException("Gift certificates not found");
        }
        return Optional.of(giftCertificates.stream()
                .map(GiftCertificateConverter::mapToGiftCertificateDto)
                .collect(Collectors.toList()));
    }

    public Optional<List<GiftCertificateDto>> findCertificateByParam(String param) throws ServiceException {
        List<GiftCertificate> giftCertificates;
        try {
            giftCertificates = giftCertificateRepositoryImpl.findCertificateByParam(param);
        } catch (RepositoryException e) {
            throw new ServiceException("Gift certificate not found" + param);
        }
        return Optional.of(giftCertificates.stream()
                .map(GiftCertificateConverter::mapToGiftCertificateDto)
                .collect(Collectors.toList()));
    }

    public Optional<GiftCertificateDto> findCertificateById(long id) throws ServiceException {
        GiftCertificateDto giftCertificateDto;
        try {
            giftCertificateDto = GiftCertificateConverter
                    .mapToGiftCertificateDto(giftCertificateRepositoryImpl.findCertificateById(id));
        } catch (RepositoryException e) {
            throw new ServiceException("Gift certificate not found");
        }
        return Optional.of(giftCertificateDto);
    }
}
