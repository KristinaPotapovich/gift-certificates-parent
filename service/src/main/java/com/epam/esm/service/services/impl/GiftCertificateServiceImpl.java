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
            giftCertificateDto = GiftCertificateConverter
                    .mapToGiftCertificateDto(giftCertificateRepositoryImpl.create(giftCertificate));
            List<Tag> tags = giftCertificate.getTags();
            List<TagDto> tagDtos = new ArrayList<>();
            for (Tag tag : tags) {
                TagDto tagDto = TagConverter.mapToTagDto(tag);
                Optional<TagDto> tagDtoOptional = Optional.empty();
                if (!tagService.isTagExistByName(tagDto.getName())) {
                    tagDtoOptional = tagService.create(tagDto);
                }
                if (tagService.isTagExistByName(tagDto.getName())) {
                    tagDtoOptional = tagService.findTagByName(tagDto.getName());
                }
                tagDto = tagDtoOptional.orElseThrow(() -> new ServiceException("Tag not found"));
                giftCertificateRepositoryImpl
                        .createCertificateAndTagRelationship(giftCertificateDto.getId(), tagDto.getId());
                tagDtos.add(tagDto);
            }
            giftCertificateDto.setTags(tagDtos);
        } catch (RepositoryException | ValidationException e) {
            throw new ServiceException("Gift certificate creation failed");
        }
        return Optional.ofNullable(giftCertificateDto);
    }

    @Override
    public Optional<GiftCertificateDto> update(GiftCertificateDto giftCertificateDto) throws ServiceException {
        GiftCertificate giftCertificate;
        try {
            giftCertificateValidation.validate(giftCertificateDto);
            giftCertificate = GiftCertificateConverter.mapToGiftCertificate(giftCertificateDto);
            giftCertificate = giftCertificateRepositoryImpl.update(giftCertificate);
            giftCertificateDto = GiftCertificateConverter.mapToGiftCertificateDto(giftCertificate);
        } catch (ValidationException | RepositoryException e) {
            throw new ServiceException("Gift certificate update failed");
        }
        return Optional.ofNullable(giftCertificateDto);
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
