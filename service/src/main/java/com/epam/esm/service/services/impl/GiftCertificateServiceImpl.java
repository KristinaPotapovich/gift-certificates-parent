package com.epam.esm.service.services.impl;

import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.exception.RepositoryException;
import com.epam.esm.core.repository.GiftCertificateRepository;
import com.epam.esm.core.repository.specification.OrderBySpecification;
import com.epam.esm.core.repository.specification.impl.OrderByDateSpecification;
import com.epam.esm.core.repository.specification.impl.OrderByNameSpecification;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.mapper.GiftCertificateConverter;
import com.epam.esm.service.services.GiftCertificateService;
import com.epam.esm.service.services.TagService;
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
    private TagService tagService;
    private static final String CREATE_CERTIFICATE_FAIL = "giftCertificate_create_fail";
    private static final String CERTIFICATE_IS_EXISTS = "certificate_is_exist";

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository giftCertificateRepositoryImpl,
                                      TagService tagService) {
        this.giftCertificateRepositoryImpl = giftCertificateRepositoryImpl;
        this.tagService = tagService;
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
    public void update(GiftCertificateDto giftCertificateDto) throws ServiceException {
        GiftCertificate giftCertificate;
        try {
            giftCertificate = GiftCertificateConverter.mapToGiftCertificate(giftCertificateDto);
            giftCertificateRepositoryImpl.update(giftCertificate);
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
    public Optional<List<GiftCertificateDto>> searchAllCertificatesByTagName(String tagName) {
        List<GiftCertificateDto> giftCertificateDtos;
        List<GiftCertificate> giftCertificates = processExceptionSearchCertificate(tagName);
        giftCertificateDtos = giftCertificates.stream()
                .map(GiftCertificateConverter::mapToGiftCertificateDto)
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

    public Optional<List<GiftCertificateDto>> sortByParam(String paramForSorting, String order)
            throws ServiceException {
        List<GiftCertificateDto> giftCertificateDtos;
        OrderBySpecification<GiftCertificate> orderBySpecification = null;
        List<GiftCertificate> giftCertificates;
        try {
            if (paramForSorting != null && !paramForSorting.isEmpty() && order != null && !order.isEmpty()) {
                if (paramForSorting.equals("name")) {
                    orderBySpecification = new OrderByNameSpecification(order);
                }
                if (paramForSorting.equals("date")) {
                    orderBySpecification = new OrderByDateSpecification(order);
                }
            } else {
                throw new ServiceException();
            }
            giftCertificates = giftCertificateRepositoryImpl.sortByParam(orderBySpecification);
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
    public void patch(GiftCertificateDto giftCertificateDto) throws ServiceException {
        try {
            long id = giftCertificateDto.getId();
            String nameUpdate = giftCertificateDto.getName();
            String descriptionUpdate = giftCertificateDto.getDescription();
            double priceUpdate = giftCertificateDto.getPrice();
            int durationUpdate = giftCertificateDto.getDurationInDays();
            GiftCertificate giftCertificate = giftCertificateRepositoryImpl
                    .findCertificateById(id);
            if (nameUpdate != null) {
                giftCertificate.setName(nameUpdate);
            }
            if (descriptionUpdate != null) {
                giftCertificate.setDescription(descriptionUpdate);
            }
            if (priceUpdate != 0) {
                giftCertificate.setPrice(priceUpdate);
            }
            if (durationUpdate != 0) {
                giftCertificate.setDurationInDays(durationUpdate);
            }
            giftCertificateRepositoryImpl.update(giftCertificate);

        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
