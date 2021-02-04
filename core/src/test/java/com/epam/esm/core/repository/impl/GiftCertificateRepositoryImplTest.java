package com.epam.esm.core.repository.impl;


import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.entity.Tag;
import com.epam.esm.core.exception.UnsupportedParametersForSorting;
import com.epam.esm.core.repository.GiftCertificateRepository;

import com.epam.esm.core.repository.impl.config.TestConfig;
import com.epam.esm.core.repository.specification.ResolverForSearchParams;
import com.epam.esm.core.repository.specification.impl.SortingNameSpecification;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
    public void createPositiveTest() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(5);
        giftCertificate.setName("certificate five");
        giftCertificate.setDescription("description");
        giftCertificate.setPrice(BigDecimal.valueOf(12.5));
        giftCertificate.setDurationInDays(6);
        GiftCertificate expected = giftCertificateRepository.create(giftCertificate);
        assertTrue(expected.getId() > 0);
        assertNotNull(expected.getCreateDate());
    }

    @Test
    public void updatePositiveTest() {
        GiftCertificate expected = new GiftCertificate();
        expected.setId(3);
        expected.setName("new name");
        expected.setDescription("description");
        expected.setPrice(BigDecimal.valueOf(14.5));
        expected.setDurationInDays(6);
        expected = giftCertificateRepository.update(expected);
        assertEquals(expected.getName(), "new name");
        assertNotNull(expected.getLastUpdateDate());
        assertEquals(expected.getPrice(), new BigDecimal("14.5"));
    }

    @Test
    public void deletePositiveTest() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1);
        List<String> tags = new ArrayList<>();
        tags.add("tag one");
        int expectedSizeOfList = giftCertificateRepository
                .findAllCertificates(new ResolverForSearchParams(tags, ""),
                        new SortingNameSpecification("asc"), 1, 5).size() - 1;
        giftCertificateRepository.delete(giftCertificate);
        int actualSizeOfList = giftCertificateRepository
                .findAllCertificates(new ResolverForSearchParams(tags, ""),
                        new SortingNameSpecification("asc"), 1, 5).size();
        assertEquals(expectedSizeOfList, actualSizeOfList);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void deleteNegativeTest() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(7);
        giftCertificateRepository.delete(giftCertificate);
    }

    @Test
    public void findAllCertificatesPositiveTest() {
        List<String> tags = new ArrayList<>();
        tags.add("tag one");
        List<GiftCertificate> certificates = giftCertificateRepository
                .findAllCertificates(new ResolverForSearchParams(tags, ""),
                        new SortingNameSpecification("asc"), 1, 5);
        assertFalse(certificates.isEmpty());
        assertNotNull(certificates.get(0).getName());
        assertEquals("certificate one", certificates.get(0).getName());
    }

    @Test
    public void findAllTagsByCertificateIdPositiveTest() {
        List<Tag> tags = giftCertificateRepository.getInformationAboutCertificatesTags(1, 1, 5);
        assertFalse(tags.isEmpty());
        assertEquals(1, tags.get(0).getId());
    }

    @Test
    public void findAllTagsByCertificateIdNegativeTest() {
        List<Tag> tags = giftCertificateRepository.getInformationAboutCertificatesTags(0, 1, 5);
        assertTrue(tags.isEmpty());
    }

    @Test(expected = UnsupportedParametersForSorting.class)
    public void findAllNegativeTest() {
        List<String> tags = new ArrayList<>();
        tags.add("tag one");
        giftCertificateRepository
                .findAllCertificates(new ResolverForSearchParams(tags, ""),
                        new SortingNameSpecification("ddd"), 1, 5);
    }

    @Test
    public void findCertificateByIdPositiveTest() {
        GiftCertificate giftCertificate = giftCertificateRepository.findCertificateById(1);
        assertNotNull(giftCertificate);
        assertEquals("certificate one", giftCertificate.getName());
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void findCertificateByIdNegativeTest() {
        giftCertificateRepository.findCertificateById(6);
    }
}