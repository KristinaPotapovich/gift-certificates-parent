package com.epam.esm.core.repository.impl;


import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.exception.RepositoryException;
import com.epam.esm.core.mapper.GiftCertificateMapper;
import com.epam.esm.core.repository.GiftCertificateRepository;
import com.epam.esm.core.repository.SearchHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    private static final String SELECT_ALL_CERTIFICATES =
            "SELECT id_certificate, name, description, price,duration,create_date, last_update_date " +
                    "FROM gift_certificate";
    private static final String SELECT_CERTIFICATE_BY_ID =
            "SELECT id_certificate, name, description, price,duration,create_date, last_update_date " +
                    "FROM gift_certificate WHERE id_certificate = ?";
    private static final String CREATE_GIFT_CERTIFICATE =
            "INSERT INTO gift_certificate(name,description,price,duration) VALUES (:name,:description,:price,:duration)";
    private static final String UPDATE_CERTIFICATE =
            "UPDATE gift_certificate SET name = :name,description = :description,price = :price,duration = :duration " +
                    "WHERE id_certificate = :id_certificate";
    private static final String DELETE_CERTIFICATE =
            "DELETE FROM gift_certificate WHERE id_certificate = ?";
    private static final String SELECT_CERTIFICATE_BY_PARAM =
            "SELECT DISTINCT id_certificate, name, description, price,duration,create_date, last_update_date " +
                    "FROM gift_certificate WHERE name LIKE ? OR description LIKE ?";
    private static final String CREATE_CERTIFICATE_TAGS =
            "INSERT INTO certificates_tags (id_certificate, id_tag) VALUES (?,?)";


    @Autowired
    public GiftCertificateRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Transactional
    @Override
    public GiftCertificate create(GiftCertificate giftCertificate) throws RepositoryException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("name", giftCertificate.getName(), Types.VARCHAR)
                .addValue("description", giftCertificate.getDescription(), Types.VARCHAR)
                .addValue("price", giftCertificate.getPrice(), Types.DOUBLE)
                .addValue("duration", giftCertificate.getDurationInDays(), Types.INTEGER);
        try {
            namedParameterJdbcTemplate.update(CREATE_GIFT_CERTIFICATE, sqlParameterSource,
                    keyHolder, new String[]{"id"});
        } catch (DataAccessException e) {
            throw new RepositoryException("Gift certificate creation failed");
        }
        giftCertificate.setName(giftCertificate.getName());
        giftCertificate.setDescription(giftCertificate.getDescription());
        giftCertificate.setPrice(giftCertificate.getPrice());
        giftCertificate.setDurationInDays(giftCertificate.getDurationInDays());
        giftCertificate.setCreateDate(LocalDateTime.now());
        giftCertificate.setId(keyHolder.getKey().intValue());
        return giftCertificate;
    }

    @Override
    public GiftCertificate update(GiftCertificate giftCertificate) throws RepositoryException {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("name", giftCertificate.getName())
                .addValue("description", giftCertificate.getDescription())
                .addValue("price", giftCertificate.getPrice())
                .addValue("duration", giftCertificate.getDurationInDays())
                .addValue("id_certificate", giftCertificate.getId());
        try {
            namedParameterJdbcTemplate.update(UPDATE_CERTIFICATE, sqlParameterSource);
        } catch (DataAccessException e) {
            throw new RepositoryException("Gift certificate update failed");
        }
        giftCertificate = findCertificateById(giftCertificate.getId());
        return giftCertificate;
    }

    @Override
    public boolean delete(long id) throws RepositoryException {
        try {
            return jdbcTemplate.update(DELETE_CERTIFICATE, id) > 0;
        } catch (DataAccessException e) {
            throw new RepositoryException("Gift certificate delete failed");
        }
    }

    @Override
    public List<GiftCertificate> findAll() throws RepositoryException {
        try {
            return jdbcTemplate.query(SELECT_ALL_CERTIFICATES, new GiftCertificateMapper());
        } catch (DataAccessException e) {
            throw new RepositoryException("Gift certificates not found");
        }
    }

    @Override
    public List<GiftCertificate> findCertificateByParam(String param) throws RepositoryException {
        try {
            return jdbcTemplate.query(SELECT_CERTIFICATE_BY_PARAM,
                    new GiftCertificateMapper(), SearchHelper.buildAppealForDataBase(param),
                    SearchHelper.buildAppealForDataBase(param));
        } catch (DataAccessException e) {
            throw new RepositoryException("Gift certificate not found" + param);
        }
    }

    @Override
    public void createCertificateAndTagRelationship(long idCertificate, long idTag) throws RepositoryException {
        try {
            jdbcTemplate.update(CREATE_CERTIFICATE_TAGS, idCertificate, idTag);
        } catch (DataAccessException e) {
            throw new RepositoryException("Creation in certificates_tags failed");
        }
    }

    @Override
    public GiftCertificate findCertificateById(long id) throws RepositoryException {
        try {
            return jdbcTemplate.queryForObject(SELECT_CERTIFICATE_BY_ID,
                    new GiftCertificateMapper(), id);
        } catch (DataAccessException e) {
            throw new RepositoryException("Gift certificate not found" + id);
        }
    }
}
