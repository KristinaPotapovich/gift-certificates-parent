package com.epam.esm.service.services.impl;

import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.exception.RepositoryException;
import com.epam.esm.core.repository.GiftCertificateRepository;
import com.epam.esm.core.repository.specification.SortingSpecification;
import com.epam.esm.core.repository.specification.impl.SortingDateSpecification;
import com.epam.esm.core.repository.specification.impl.SortingNameSpecification;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.exception.ServiceException;
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
    public Optional<GiftCertificateDto> create(GiftCertificateDto giftCertificateDto) throws ServiceException {
        GiftCertificate giftCertificate;
        try {
            giftCertificate = GiftCertificateConverter.mapToGiftCertificate(giftCertificateDto);
            GiftCertificate createdGiftCertificate = giftCertificateRepositoryImpl.create(giftCertificate);
            giftCertificateDto = GiftCertificateConverter
                    .mapToGiftCertificateDto(createdGiftCertificate);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
        return Optional.of(giftCertificateDto);
    }

    @Transactional
    @Override
    public Optional<GiftCertificateDto> update(GiftCertificateDto giftCertificateDto) throws ServiceException {
        GiftCertificate giftCertificate;
        try {
            giftCertificate = GiftCertificateConverter.mapToGiftCertificate(giftCertificateDto);
            giftCertificate = giftCertificateRepositoryImpl.update(giftCertificate);
            return Optional.ofNullable(GiftCertificateConverter.mapToGiftCertificateDto(giftCertificate));
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Transactional
    @Override
    public void delete(long id) throws ServiceException {
        try {
            GiftCertificate giftCertificate = new GiftCertificate();
            giftCertificate.setId(id);
            giftCertificateRepositoryImpl.delete(giftCertificate);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<GiftCertificateDto> findAll(int page, int size) throws ServiceException {
        List<GiftCertificate> giftCertificates;
        try {
            giftCertificates = giftCertificateRepositoryImpl.findAll(page, size);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
        return giftCertificates.stream()
                .map(GiftCertificateConverter::mapToGiftCertificateDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Optional<List<GiftCertificateDto>> findCertificateByParam(String param, int page,
                                                                     int size) throws ServiceException {
        List<GiftCertificateDto> giftCertificateDtos;
        try {
            List<GiftCertificate> giftCertificates = giftCertificateRepositoryImpl
                    .findCertificateByParam(param, page, size);
            giftCertificateDtos = giftCertificates.stream()
                    .map(GiftCertificateConverter::mapToGiftCertificateDto)
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
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
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
        try {
            return giftCertificateRepositoryImpl
                    .searchAllCertificatesByTagName(tagName, page, size);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<List<GiftCertificateDto>> sortByParam(String paramForSorting, String order, int page, int size)
            throws ServiceException {
        List<GiftCertificateDto> giftCertificateDtos;
        SortingSpecification<GiftCertificate> orderBySpecification = null;
        List<GiftCertificate> giftCertificates;
        try {
            if (paramForSorting != null && !paramForSorting.isEmpty() && order != null && !order.isEmpty()) {
                if (paramForSorting.equals("name")) {
                    orderBySpecification = new SortingNameSpecification(order);
                }
                if (paramForSorting.equals("date")) {
                    orderBySpecification = new SortingDateSpecification(order);
                }
            } else {
                throw new ServiceException();
            }
            giftCertificates = giftCertificateRepositoryImpl.sortByParam(orderBySpecification, page, size);
            giftCertificateDtos = giftCertificates.stream()
                    .map(GiftCertificateConverter::mapToGiftCertificateDto)
                    .collect(Collectors.toList());
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
        return Optional.of(giftCertificateDtos);
    }

    @Transactional
    @Override
    public Optional<GiftCertificateDto> patch(GiftCertificateDto giftCertificateDto) throws ServiceException {
        try {
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

        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public Optional<List<GiftCertificateDto>> findAllBySeveralTags(List<Long> tags, int page, int size)
            throws ServiceException {
        List<GiftCertificate> giftCertificates;
        try {
            giftCertificates = giftCertificateRepositoryImpl.findAllBySeveralTags(tags, page, size);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
        return Optional.of(giftCertificates.stream()
                .map(GiftCertificateConverter::mapToGiftCertificateDto)
                .collect(Collectors.toList()));
    }
}
