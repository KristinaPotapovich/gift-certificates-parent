package com.epam.esm.service.services.impl;

import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.entity.Tag;
import com.epam.esm.core.repository.GiftCertificateRepository;
import com.epam.esm.core.repository.specification.BaseSpecificationForSorting;
import com.epam.esm.core.repository.specification.ParamForSorting;
import com.epam.esm.core.repository.specification.ResolverForSearchParams;
import com.epam.esm.core.repository.specification.impl.*;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.mapper.GiftCertificateConverter;
import com.epam.esm.service.mapper.TagConverter;
import com.epam.esm.service.services.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Gift certificate service.
 */
@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private GiftCertificateRepository giftCertificateRepositoryImpl;
    private static final String CERTIFICATE_IS_EXIST_MESSAGE = "certificate_is_exists";


    /**
     * Instantiates a new Gift certificate service.
     *
     * @param giftCertificateRepositoryImpl the gift certificate repository
     */
    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository giftCertificateRepositoryImpl) {
        this.giftCertificateRepositoryImpl = giftCertificateRepositoryImpl;
    }

    @Transactional
    @Override
    public Optional<GiftCertificateDto> create(GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificate;
        giftCertificate = GiftCertificateConverter.mapToGiftCertificate(giftCertificateDto);
        List<GiftCertificate> certificates = giftCertificateRepositoryImpl
                .findCertificateByName(giftCertificate.getName());
        certificates.forEach(created -> {
            if (created != null && created.getName().equals(giftCertificate.getName())
                    && created.getDescription().equals(giftCertificate.getDescription())) {
                throw new ServiceException(CERTIFICATE_IS_EXIST_MESSAGE);
            }
        });
        GiftCertificate createdGiftCertificate = giftCertificateRepositoryImpl.create(giftCertificate);
        giftCertificateDto = GiftCertificateConverter
                .mapToGiftCertificateDto(createdGiftCertificate);
        return Optional.of(giftCertificateDto);
    }

    @Transactional
    @Override
    public Optional<GiftCertificateDto> update(GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificate;
        giftCertificate = GiftCertificateConverter.mapToGiftCertificate(giftCertificateDto);
        giftCertificate = giftCertificateRepositoryImpl.update(giftCertificate);
        return Optional.ofNullable(GiftCertificateConverter.mapToGiftCertificateDto(giftCertificate));
    }

    @Transactional
    @Override
    public void delete(long id) {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(id);
        giftCertificateRepositoryImpl.delete(giftCertificate);
    }

    @Override
    public List<GiftCertificateDto> findAllCertificates(String param, String paramForSorting,
                                                        List<String> tags, String order,
                                                        int page, int size) {
        List<GiftCertificate> giftCertificates;
        BaseSpecificationForSorting<GiftCertificate> orderBySpecification = buildSortingParams(paramForSorting, order);
        giftCertificates = giftCertificateRepositoryImpl.findAllCertificates(new ResolverForSearchParams(tags, param),
                orderBySpecification, page, size);
        return giftCertificates
                .stream().map(GiftCertificateConverter::mapToGiftCertificateDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<GiftCertificateDto> findCertificateById(long id) {
        GiftCertificateDto giftCertificateDto;
        giftCertificateDto = GiftCertificateConverter
                .mapToGiftCertificateDto(giftCertificateRepositoryImpl.findCertificateById(id));
        return Optional.of(giftCertificateDto);
    }

    @Transactional
    @Override
    public Optional<GiftCertificateDto> patch(GiftCertificateDto giftCertificateDto) {
        long id = giftCertificateDto.getId();
        String nameUpdate = giftCertificateDto.getName();
        String descriptionUpdate = giftCertificateDto.getDescription();
        BigDecimal priceUpdate = giftCertificateDto.getPrice();
        int durationUpdate = giftCertificateDto.getDurationInDays();
        GiftCertificate giftCertificate = giftCertificateRepositoryImpl
                .findCertificateById(id);
        if (nameUpdate != null) {
            giftCertificate.setName(nameUpdate);
        }
        if (descriptionUpdate != null) {
            giftCertificate.setDescription(descriptionUpdate);
        }
        if (priceUpdate != null) {
            giftCertificate.setPrice(priceUpdate);
        }
        if (durationUpdate != 0) {
            giftCertificate.setDurationInDays(durationUpdate);
        }

        return Optional
                .ofNullable(GiftCertificateConverter
                        .mapToGiftCertificateDto(giftCertificateRepositoryImpl.update(giftCertificate)));
    }

    @Override
    public Optional<List<TagDto>> getInformationAboutCertificatesTags(long idCertificate, int page, int size) {
        List<Tag> tags;
        tags = giftCertificateRepositoryImpl.getInformationAboutCertificatesTags(idCertificate, page, size);
        return Optional.of(tags.stream()
                .map(TagConverter::mapToTagDto)
                .collect(Collectors.toList()));
    }

    @Nullable
    private BaseSpecificationForSorting<GiftCertificate> buildSortingParams(String paramForSorting, String order) {
        BaseSpecificationForSorting<GiftCertificate> orderBySpecification = null;
        ValidationService.validateParamForSearch(paramForSorting);
        if (ParamForSorting.NAME.name().equalsIgnoreCase(paramForSorting)) {
            orderBySpecification = new SortingNameSpecification(order);
        } else if (ParamForSorting.DATE.name().equalsIgnoreCase(paramForSorting)) {
            orderBySpecification = new SortingDateSpecification(order);
        }
        return orderBySpecification;
    }
}
