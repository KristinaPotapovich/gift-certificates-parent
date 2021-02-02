package com.epam.esm.service.services.impl;

import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.repository.GiftCertificateRepository;
import com.epam.esm.core.repository.specification.ParamForSorting;
import com.epam.esm.core.repository.specification.SortingSpecification;
import com.epam.esm.core.repository.specification.impl.SortingNameSpecification;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.core.exception.UnsupportedParametersForSorting;
import com.epam.esm.service.mapper.GiftCertificateConverter;
import com.epam.esm.service.services.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private static final String SORTING_FAIL_MASSAGE = "sorting_fail";


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
    public List<GiftCertificateDto> findAll(int page, int size) {
        List<GiftCertificate> giftCertificates;
        giftCertificates = giftCertificateRepositoryImpl.findAll(page, size);
        return giftCertificates.stream()
                .map(GiftCertificateConverter::mapToGiftCertificateDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Optional<List<GiftCertificateDto>> findCertificateByParam(String param, int page, int size) {
        List<GiftCertificateDto> giftCertificateDtos;
        List<GiftCertificate> giftCertificates = giftCertificateRepositoryImpl
                .findCertificateByParam(param, page, size);
        giftCertificateDtos = giftCertificates.stream()
                .map(GiftCertificateConverter::mapToGiftCertificateDto)
                .collect(Collectors.toList());
        return Optional.of(giftCertificateDtos);
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
    public Optional<List<GiftCertificateDto>> searchAllCertificatesByTagName(String tagName, int page, int size) {
        List<GiftCertificateDto> giftCertificateDtos;
        List<GiftCertificate> giftCertificates = processExceptionSearchCertificate(tagName, page, size);
        giftCertificateDtos = giftCertificates.stream()
                .map(GiftCertificateConverter::mapToGiftCertificateDto)
                .collect(Collectors.toList());
        return Optional.of(giftCertificateDtos);

    }

    private List<GiftCertificate> processExceptionSearchCertificate(String tagName, int page, int size) {
        return giftCertificateRepositoryImpl
                .searchAllCertificatesByTagName(tagName, page, size);
    }

    public Optional<List<GiftCertificateDto>> sortByParam(String paramForSorting, String order, int page, int size) {
        SortingSpecification<GiftCertificate> orderBySpecification = null;
        if (ParamForSorting.NAME.name().equalsIgnoreCase(paramForSorting)) {
            orderBySpecification = new SortingNameSpecification(order);
            return Optional.of(giftCertificateRepositoryImpl.sortByParam(orderBySpecification, page, size)
                    .stream().map(GiftCertificateConverter::mapToGiftCertificateDto)
                    .collect(Collectors.toList()));
        }
        if (ParamForSorting.DATE.name().equalsIgnoreCase(paramForSorting)) {
            return Optional.of(giftCertificateRepositoryImpl.sortByParam(orderBySpecification, page, size)
                    .stream().map(GiftCertificateConverter::mapToGiftCertificateDto)
                    .collect(Collectors.toList()));
        } else {
            throw new UnsupportedParametersForSorting(SORTING_FAIL_MASSAGE);
        }
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

    public Optional<List<GiftCertificateDto>> findAllBySeveralTags(List<Long> tags, int page, int size) {
        List<GiftCertificate> giftCertificates;
        giftCertificates = giftCertificateRepositoryImpl.findAllBySeveralTags(tags, page, size);
        return Optional.of(giftCertificates.stream()
                .map(GiftCertificateConverter::mapToGiftCertificateDto)
                .collect(Collectors.toList()));
    }
}
