package com.epam.esm.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.impl.GiftCertificateRepository;
import com.epam.esm.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GiftCertificateService implements BaseService<GiftCertificate> {

    private final GiftCertificateRepository giftCertificateRepository;

    @Autowired
    public GiftCertificateService(GiftCertificateRepository giftCertificateRepository) {
        this.giftCertificateRepository = giftCertificateRepository;
    }

    @Override
    public Optional<GiftCertificate> create(GiftCertificate giftCertificate) {
        return Optional.empty();
    }

    @Override
    public Optional<GiftCertificate> update(GiftCertificate giftCertificate) {
        return Optional.empty();
    }

    @Override
    public Optional<GiftCertificate> delete(GiftCertificate giftCertificate) {
        return Optional.empty();
    }
    public Optional<GiftCertificate> findCertificate(long id){
        return Optional.of(giftCertificateRepository.findCertificate(id));
    }
}
