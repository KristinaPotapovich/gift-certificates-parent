package com.epam.esm.core.repository.impl;


import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.exception.RepositoryException;
import com.epam.esm.core.repository.GiftCertificateRepository;

import com.epam.esm.core.repository.impl.config.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@Component
@Transactional
@Sql({"classpath:drop-data-base.sql", "classpath:gift-certificates-parent.sql", "classpath:init-data_test.sql"})
@ContextConfiguration(classes = TestConfig.class)
public class GiftCertificateRepositoryImplTest {

    @Autowired
    private GiftCertificateRepository giftCertificateRepository;

    @Test
    public void create() throws RepositoryException {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(5);
        giftCertificate.setName("certificate five");
        giftCertificate.setDescription("description");
        giftCertificate.setPrice(BigDecimal.valueOf(12.5));
        giftCertificate.setDurationInDays(6);
        GiftCertificate expected = giftCertificateRepository.create(giftCertificate);
        List<GiftCertificate> certificates = giftCertificateRepository.findAll(1, 5);
        GiftCertificate giftCertificateFromDB = certificates.get(4);
        assertEquals(expected, giftCertificateFromDB);
    }

    @Test
    public void update() throws RepositoryException {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(3);
        giftCertificate.setName("new name");
        giftCertificate.setDescription("description");
        giftCertificate.setPrice(BigDecimal.valueOf(14.5));
        giftCertificate.setDurationInDays(6);
        giftCertificate = giftCertificateRepository.update(giftCertificate);
        assertEquals(giftCertificate.getName(), "new name");

    }

    @Test
    public void delete() throws RepositoryException {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(3);
        int expectedSizeOfList = giftCertificateRepository.findAll(1, 5).size() - 1;
        giftCertificateRepository.delete(giftCertificate);
        int actualSizeOfList = giftCertificateRepository.findAll(1, 5).size();
        assertEquals(expectedSizeOfList, actualSizeOfList);
    }

    @Test
    public void findAll() throws RepositoryException {
        List<GiftCertificate> certificates = giftCertificateRepository.findAll(1, 5);
        assertEquals("certificate one", certificates.get(0).getName());
    }

    @Test
    public void findCertificateByParam() throws RepositoryException {
        List<GiftCertificate> certificates = giftCertificateRepository
                .findCertificateByParam("%tw%", 1, 5);
        assertFalse(certificates.isEmpty());
    }

    @Test
    public void searchAllCertificatesByTagName() throws RepositoryException {
        List<GiftCertificate> certificates = giftCertificateRepository
                .searchAllCertificatesByTagName("tag one", 1, 5);
        assertFalse(certificates.isEmpty());
    }

    @Test
    public void findAllBySeveralTags() throws RepositoryException {
        List<Long> tagsId = new ArrayList<>();
        tagsId.add(1L);
        tagsId.add(2L);
        List<GiftCertificate> certificates = giftCertificateRepository.findAllBySeveralTags(tagsId,1,5);
        assertFalse(certificates.isEmpty());
    }
    @Test
    public void findCertificateById() throws RepositoryException{
        GiftCertificate giftCertificate = giftCertificateRepository.findCertificateById(1);
        assertNotNull(giftCertificate);
    }
}