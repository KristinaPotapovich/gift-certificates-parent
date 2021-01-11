package com.epam.esm.service.services.impl;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.mapper.GiftCertificateConverter;
import com.epam.esm.core.repository.impl.GiftCertificateRepository;
import com.epam.esm.service.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GiftCertificateService implements BaseService<GiftCertificateDto> {

    private final GiftCertificateRepository giftCertificateRepository;

    @Autowired
    public GiftCertificateService(GiftCertificateRepository giftCertificateRepository) {
        this.giftCertificateRepository = giftCertificateRepository;
    }

    @Override
    public Optional<GiftCertificateDto> create(GiftCertificateDto giftCertificateDto) {
        return Optional.empty();
    }

    @Override
    public Optional<GiftCertificateDto> update(GiftCertificateDto giftCertificateDto) {
        return Optional.empty();
    }

    @Override
    public boolean delete(long id) {
        return false;
    }

    public Optional<GiftCertificateDto> findCertificate(long id) {
        GiftCertificateDto giftCertificateDto = GiftCertificateConverter.mapToGiftCertificateDto(giftCertificateRepository.findCertificate(id));
        return Optional.of(giftCertificateDto);
    }
}
