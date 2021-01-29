package com.epam.esm.core.repository.impl;

import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.exception.RepositoryException;
import com.epam.esm.core.repository.GiftCertificateRepository;

import org.hibernate.Session;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Sql({"classpath:drop-data-base.sql", "classpath:gift-certificates-parent.sql", "classpath:init-data-test.sql"})
@DataJpaTest
@EnableAutoConfiguration
@ActiveProfiles("test")
public class GiftCertificateRepositoryImplTest {

   private GiftCertificateRepository giftCertificateRepository = new GiftCertificateRepositoryImpl();

    GiftCertificate giftCertificate1;
    GiftCertificate giftCertificate2;
    GiftCertificate giftCertificate3;
    List<GiftCertificate> certificates;

//    @BeforeAll
//    private void setUp() {
//        giftCertificateRepository =
//    }

    @Test
    public void create() throws RepositoryException {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(5);
        giftCertificate.setName("certificate five");
        giftCertificate.setDescription("description");
        giftCertificate.setPrice(BigDecimal.valueOf(12.5));
        giftCertificate.setDurationInDays(6);
        GiftCertificate expected = giftCertificateRepository.create(giftCertificate);
        List<GiftCertificate> certificates = giftCertificateRepository.findAll(5, 6);
        GiftCertificate giftCertificateFromDB = certificates.get(4);
        Assertions.assertEquals(expected, giftCertificateFromDB);
    }

    @Test
    public void update() throws RepositoryException {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(3);
        giftCertificate.setName("new name");
        giftCertificate.setDescription("description");
        giftCertificate.setPrice(BigDecimal.valueOf(14.5));
        giftCertificate.setDurationInDays(6);
        giftCertificateRepository.update(giftCertificate);
        List<GiftCertificate> certificates = giftCertificateRepository.findAll(5, 6);
        GiftCertificate updated = certificates.get(2);
        Assertions.assertEquals(updated.getName(), "new name");
        Assertions.assertEquals(updated.getPrice(), 14.5);

    }

    @Test
    public void delete() throws RepositoryException {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(3);
        int expectedSizeOfList = giftCertificateRepository.findAll(5, 6).size() - 1;
        giftCertificateRepository.delete(giftCertificate);
        int actualSizeOfList = giftCertificateRepository.findAll(5, 6).size();
        Assertions.assertEquals(expectedSizeOfList, actualSizeOfList);
    }

    @Test
    public void findAll() throws RepositoryException {
        List<GiftCertificate> certificates = giftCertificateRepository.findAll(5, 6);
        Assertions.assertEquals("certificate one", certificates.get(0).getName());
    }

    @Test
    public void findCertificateByParam() throws RepositoryException {
        List<GiftCertificate> certificates = giftCertificateRepository.findCertificateByParam("%tw%", 5, 6);
        Assertions.assertFalse(certificates.isEmpty());
    }

    @Test
    public void searchAllCertificatesByTagName() throws RepositoryException {
        List<GiftCertificate> certificates = giftCertificateRepository
                .searchAllCertificatesByTagName("tag one", 5, 6);
        Assertions.assertFalse(certificates.isEmpty());
    }
}