package com.epam.esm.repository.impl;


import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.repository.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class GiftCertificateRepository implements BaseRepository<GiftCertificate> {

    JdbcTemplate jdbcTemplate;

    private static final String SELECT_CERTIFICATE_BY_ID =
            "SELECT id_certificate, name, description, price,duration,create_date, last_update_date FROM gift_certificate " +
                    "WHERE id_certificate = ?";

    @Autowired
    public GiftCertificateRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public GiftCertificate create(GiftCertificate giftCertificate) {
        return null;
    }

    @Override
    public GiftCertificate update(GiftCertificate giftCertificate) {
        return null;
    }

    @Override
    public void delete(GiftCertificate giftCertificate) {

    }

    public GiftCertificate findCertificate(long id) {
        return jdbcTemplate.queryForObject(SELECT_CERTIFICATE_BY_ID,
                new GiftCertificateMapper(), id);
    }
}
