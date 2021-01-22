package com.epam.esm.core.repository.impl;

import com.epam.esm.core.config.TestConfig;
import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.exception.RepositoryException;
import com.epam.esm.core.repository.GiftCertificateRepository;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@Component
@ContextConfiguration(classes = TestConfig.class)
public class GiftCertificateRepositoryImplTest {

    @Autowired
    GiftCertificateRepository giftCertificateRepository;

    @Test
    public void create() throws RepositoryException {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(5);
        giftCertificate.setName("certificate five");
        giftCertificate.setDescription("description");
        giftCertificate.setPrice(12.5);
        giftCertificate.setDurationInDays(6);
        GiftCertificate expected = giftCertificateRepository.create(giftCertificate);
        List<GiftCertificate> certificates = giftCertificateRepository.findAll();
        GiftCertificate giftCertificateFromDB = certificates.get(4);
        Assertions.assertEquals(expected, giftCertificateFromDB);
    }

    @Test
    public void update() throws RepositoryException {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(3);
        giftCertificate.setName("new name");
        giftCertificate.setDescription("description");
        giftCertificate.setPrice(14.5);
        giftCertificate.setDurationInDays(6);
        giftCertificateRepository.update(giftCertificate);
        List<GiftCertificate> certificates = giftCertificateRepository.findAll();
        GiftCertificate updated = certificates.get(2);
        Assertions.assertEquals(updated.getName(), "new name");
        Assertions.assertEquals(updated.getPrice(), 14.5);

    }

    @Test
    public void delete() throws RepositoryException {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(3);
        int expectedSizeOfList = giftCertificateRepository.findAll().size() - 1;
        giftCertificateRepository.delete(giftCertificate);
        int actualSizeOfList = giftCertificateRepository.findAll().size();
        Assertions.assertEquals(expectedSizeOfList, actualSizeOfList);
    }

    @Test
    public void findAll() throws RepositoryException {
        List<GiftCertificate> certificates = giftCertificateRepository.findAll();
        Assertions.assertEquals("certificate one", certificates.get(0).getName());
    }

    @Test
    public void findCertificateByParam() throws RepositoryException {
        List<GiftCertificate> certificates = giftCertificateRepository.findCertificateByParam("%tw%");
        Assertions.assertFalse(certificates.isEmpty());
    }

    @Test
    public void searchAllCertificatesByTagName() throws RepositoryException {
        List<GiftCertificate> certificates = giftCertificateRepository.searchAllCertificatesByTagName("tag one");
        Assertions.assertFalse(certificates.isEmpty());
    }
}