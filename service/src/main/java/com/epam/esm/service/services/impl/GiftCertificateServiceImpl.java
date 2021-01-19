package com.epam.esm.service.services.impl;

import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.entity.Tag;
import com.epam.esm.core.exception.RepositoryException;
import com.epam.esm.core.repository.GiftCertificateRepository;
import com.epam.esm.core.repository.specification.SortByParamSpecification;
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

import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Gift certificate service.
 */
@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private GiftCertificateRepository giftCertificateRepositoryImpl;
    private Validation<GiftCertificateDto> giftCertificateValidation;
    private TagService tagService;
    private static final String CREATE_CERTIFICATE_FAIL = "giftCertificate_create_fail";
    private static final String CERTIFICATE_IS_EXISTS = "certificate_is_exist";

    /**
     * Instantiates a new Gift certificate service.
     *
     * @param giftCertificateRepositoryImpl the gift certificate repository
     * @param giftCertificateValidation     the gift certificate validation
     * @param tagService                    the tag service
     */
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
            if (!giftCertificateRepositoryImpl.isCertificateExist(giftCertificate)) {
            GiftCertificate createdGiftCertificate = giftCertificateRepositoryImpl.create(giftCertificate);
                giftCertificateDto = GiftCertificateConverter
                        .mapToGiftCertificateDto(createdGiftCertificate);
                List<Tag> tags = giftCertificate.getTags();
                List<TagDto> tagDtos = createTagsAndRelations(giftCertificateDto, tags);
                giftCertificateDto.setTags(tagDtos);
            } else {
                throw new ServiceException(CERTIFICATE_IS_EXISTS);
            }
        } catch (RepositoryException | ValidationException e) {
            throw new ServiceException(e.getMessage());
        }
        return Optional.of(giftCertificateDto);
    }

    private List<TagDto> createTagsAndRelations(GiftCertificateDto giftCertificateDto, List<Tag> tags) {
        List<TagDto> tagDtos = new ArrayList<>();
        tags.forEach(tag -> createRelationsBetweenTagAndCertificate(giftCertificateDto, tagDtos, tag));
        return tagDtos;
    }

    private void createRelationsBetweenTagAndCertificate(GiftCertificateDto giftCertificateDto, List<TagDto> tagDtos, Tag tag) {
        Optional<TagDto> tagDtoOptional = processExceptionForFindTagByName(tag);
        if (!tagDtoOptional.isPresent()) {
            tagDtoOptional = processExceptionForCreate(tag);
        }
        TagDto tagDto = processExceptionForThrowException(tagDtoOptional);
        processExceptionForCreateRelationTagAndCertificate(giftCertificateDto, tagDto, tagDtos);
    }

    private Optional<TagDto> processExceptionForFindTagByName(Tag tag) {
        try {
            return tagService.findTagByName(tag.getName());
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

    private TagDto processExceptionForThrowException(Optional<TagDto> tagDtoOptional) {
        try {
            return tagDtoOptional.orElseThrow(() -> new ServiceException(CREATE_CERTIFICATE_FAIL));
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<TagDto> processExceptionForCreate(Tag tag) {
        try {
            return tagService.create(TagConverter.mapToTagDto(tag));
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

    private void processExceptionForCreateRelationTagAndCertificate(GiftCertificateDto giftCertificateDto,
                                                                    TagDto tagDto, List<TagDto> tagDtos) {
        try {
            giftCertificateRepositoryImpl
                    .createCertificateAndTagRelation(giftCertificateDto.getId(), tagDto.getId());
            tagDtos.add(tagDto);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }

    }

    @Transactional
    @Override
    public void update(GiftCertificateDto giftCertificateDto) throws ServiceException {
        GiftCertificate giftCertificate;
        try {
            giftCertificateValidation.validate(giftCertificateDto);
            giftCertificate = GiftCertificateConverter.mapToGiftCertificate(giftCertificateDto);
            giftCertificateRepositoryImpl.update(giftCertificate);
            giftCertificateRepositoryImpl.deleteCertificateAndTagRelation(giftCertificate.getId());
            List<TagDto> tagsResult = createTagsAndRelations(giftCertificateDto, giftCertificate.getTags());
            giftCertificateDto.setTags(tagsResult);
        } catch (ValidationException | RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Transactional
    @Override
    public boolean delete(long id) throws ServiceException {
        try {
            giftCertificateRepositoryImpl.deleteCertificateAndTagRelation(id);
            return giftCertificateRepositoryImpl.delete(id);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Optional<List<GiftCertificateDto>> findAll() throws ServiceException {
        List<GiftCertificate> giftCertificates;
        try {
            giftCertificates = giftCertificateRepositoryImpl.findAll();
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
        return Optional.of(giftCertificates.stream()
                .map(GiftCertificateConverter::mapToGiftCertificateDto)
                .collect(Collectors.toList()));
    }

    @Transactional
    @Override
    public Optional<List<GiftCertificateDto>> findCertificateByParam(String param) throws ServiceException {
        List<GiftCertificateDto> giftCertificateDtos;
        try {
            List<GiftCertificate> giftCertificates = giftCertificateRepositoryImpl
                    .findCertificateByParam(param);
            giftCertificateDtos = giftCertificates.stream()
                    .map(GiftCertificateConverter::mapToGiftCertificateDto)
                    .peek(this::injectTags)
                    .collect(Collectors.toList());
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
        return Optional.of(giftCertificateDtos);
    }

    @Override
    public Optional<GiftCertificateDto> findCertificateById(long id) throws ServiceException {
        GiftCertificateDto giftCertificateDto;
        try {
            giftCertificateDto = GiftCertificateConverter
                    .mapToGiftCertificateDto(giftCertificateRepositoryImpl.findCertificateById(id));
            injectTags(giftCertificateDto);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
        return Optional.of(giftCertificateDto);
    }

    @Transactional
    @Override
    public Optional<List<GiftCertificateDto>> searchAllCertificatesByTagName(String tagName) {
        List<GiftCertificateDto> giftCertificateDtos;
        List<GiftCertificate> giftCertificates = processExceptionSearchCertificate(tagName);
        giftCertificateDtos = giftCertificates.stream()
                .map(GiftCertificateConverter::mapToGiftCertificateDto)
                .peek(this::injectTags)
                .collect(Collectors.toList());

        return Optional.of(giftCertificateDtos);

    }

    private List<GiftCertificate> processExceptionSearchCertificate(String tagName) {
        try {
            return giftCertificateRepositoryImpl
                    .searchAllCertificatesByTagName(tagName);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    private void injectTags(GiftCertificateDto giftCertificateDto) {
        Optional<List<TagDto>> tagDtos = processExceptionForFindAllCertificates(giftCertificateDto);
        tagDtos.ifPresent(giftCertificateDto::setTags);
    }

    private Optional<List<TagDto>> processExceptionForFindAllCertificates(GiftCertificateDto giftCertificateDto) {
        try {
            return tagService.findAllTagsByCertificateId(giftCertificateDto.getId());
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<List<GiftCertificateDto>> sortByParam(String paramForSorting, String order)
            throws ServiceException {
        List<GiftCertificateDto> giftCertificateDtos;
        SortByParamSpecification sortByParamSpecification = new SortByParamSpecification(paramForSorting, order);
        List<GiftCertificate> giftCertificates;
        try {
            giftCertificates = giftCertificateRepositoryImpl.sortByParam(sortByParamSpecification);
            giftCertificateDtos = giftCertificates.stream()
                    .map(GiftCertificateConverter::mapToGiftCertificateDto)
                    .peek(this::injectTags)
                    .collect(Collectors.toList());
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
        return Optional.of(giftCertificateDtos);
    }
}
