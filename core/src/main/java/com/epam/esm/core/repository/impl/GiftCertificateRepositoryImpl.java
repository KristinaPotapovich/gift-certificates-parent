package com.epam.esm.core.repository.impl;


import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.exception.RepositoryException;
import com.epam.esm.core.mapper.GiftCertificateMapper;
import com.epam.esm.core.repository.GiftCertificateRepository;
import com.epam.esm.core.repository.specification.SortByParamSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.List;

@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final String CREATE_CERTIFICATE_FAIL = "giftCertificate_create_fail";
    private static final String UPDATE_CERTIFICATE_FAIL = "giftCertificate_update_fail";
    private static final String DELETE_CERTIFICATE_FAIL = "giftCertificate_delete_fail";
    private static final String FIND_ALL_CERTIFICATES_FAIL = "giftCertificate_find_all_certificates_fail";
    private static final String FIND_CERTIFICATE_BY_PARAM_FAIL = "giftCertificate_find_certificate_by_param_fail";
    private static final String CREATE_RELATION_CERTIFICATE_TAG_FAIL = "giftCertificate_create_relation_certificate_tag_fail";
    private static final String DELETE_RELATION_CERTIFICATE_TAG_FAIL = "giftCertificate_delete_relation_certificate_tag_fail";
    private static final String SORT_CERTIFICATE_FAIL = "giftCertificate_sort_certificate_fail";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String DURATION_IN_DAYS = "duration";
    private static final String ID_CERTIFICATE = "id_certificate";
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
            "SELECT DISTINCT gc.id_certificate,gc.name,gc.description,gc.price,gc.duration,gc.create_date," +
                    "gc.last_update_date FROM gift_certificate gc " +
                    "INNER JOIN certificates_tags ct ON gc.id_certificate = ct.id_certificate " +
                    "WHERE gc.name LIKE ? OR gc.description LIKE ? ";
    private static final String CREATE_CERTIFICATE_TAGS =
            "INSERT INTO certificates_tags (id_certificate, id_tag) VALUES (?,?)";
    private static final String RELATION_DELETE_BY_CERTIFICATE_ID =
            "DELETE FROM certificates_tags WHERE id_certificate = ?";
    private static final String SEARCH_ALL_CERTIFICATES_BY_TAG_NAME =
            "SELECT DISTINCT gc.id_certificate,gc.name,gc.description,gc.price,gc.duration,gc.create_date," +
                    "gc.last_update_date, t.name FROM gift_certificate gc " +
                    "INNER JOIN certificates_tags ct ON gc.id_certificate = ct.id_certificate " +
                    "INNER JOIN tag t ON t.id_tag = ct.id_tag " +
                    "WHERE t.name LIKE ? ";
    private static final String SORT_CERTIFICATE =
            "SELECT id_certificate, name, description, price,duration,create_date, last_update_date " +
                    "FROM gift_certificate " +
                    "ORDER BY ";

    @Autowired
    public GiftCertificateRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public GiftCertificate create(GiftCertificate giftCertificate) throws RepositoryException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue(NAME, giftCertificate.getName(), Types.VARCHAR)
                .addValue(DESCRIPTION, giftCertificate.getDescription(), Types.VARCHAR)
                .addValue(PRICE, giftCertificate.getPrice(), Types.DOUBLE)
                .addValue(DURATION_IN_DAYS, giftCertificate.getDurationInDays(), Types.INTEGER);
        try {
            namedParameterJdbcTemplate.update(CREATE_GIFT_CERTIFICATE, sqlParameterSource,
                    keyHolder, new String[]{ID_CERTIFICATE});
        } catch (DataAccessException e) {
            throw new RepositoryException(CREATE_CERTIFICATE_FAIL);
        }
        giftCertificate.setName(giftCertificate.getName());
        giftCertificate.setDescription(giftCertificate.getDescription());
        giftCertificate.setPrice(giftCertificate.getPrice());
        giftCertificate.setDurationInDays(giftCertificate.getDurationInDays());
        if (keyHolder.getKey() != null) {
            giftCertificate.setId(keyHolder.getKey().intValue());
        }
        return giftCertificate;
    }

    @Override
    public boolean update(GiftCertificate giftCertificate) throws RepositoryException {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue(NAME, giftCertificate.getName())
                .addValue(DESCRIPTION, giftCertificate.getDescription())
                .addValue(PRICE, giftCertificate.getPrice())
                .addValue(DURATION_IN_DAYS, giftCertificate.getDurationInDays())
                .addValue(ID_CERTIFICATE, giftCertificate.getId());
        try {
            return namedParameterJdbcTemplate.update(UPDATE_CERTIFICATE, sqlParameterSource) > 0;
        } catch (DataAccessException e) {
            throw new RepositoryException(UPDATE_CERTIFICATE_FAIL);
        }
    }

    @Override
    public boolean delete(long id) throws RepositoryException {
        try {
            return jdbcTemplate.update(DELETE_CERTIFICATE, id) > 0;
        } catch (DataAccessException e) {
            throw new RepositoryException(DELETE_CERTIFICATE_FAIL);
        }
    }

    @Override
    public List<GiftCertificate> findAll() throws RepositoryException {
        try {
            return jdbcTemplate.query(SELECT_ALL_CERTIFICATES, new GiftCertificateMapper());
        } catch (DataAccessException e) {
            throw new RepositoryException(FIND_ALL_CERTIFICATES_FAIL);
        }
    }

    @Override
    public List<GiftCertificate> findCertificateByParam(String param) throws RepositoryException {
        try {
            return jdbcTemplate.query(SELECT_CERTIFICATE_BY_PARAM,
                    new GiftCertificateMapper(), "%" + param + "%", "%" + param + "%");
        } catch (DataAccessException e) {
            throw new RepositoryException(FIND_CERTIFICATE_BY_PARAM_FAIL);
        }
    }

    @Override
    public void createCertificateAndTagRelation(long idCertificate, long idTag) throws RepositoryException {
        try {
            jdbcTemplate.update(CREATE_CERTIFICATE_TAGS, idCertificate, idTag);
        } catch (DataAccessException e) {
            throw new RepositoryException(CREATE_RELATION_CERTIFICATE_TAG_FAIL);
        }
    }

    @Override
    public GiftCertificate findCertificateById(long id) throws RepositoryException {
        try {
            return jdbcTemplate.queryForObject(SELECT_CERTIFICATE_BY_ID,
                    new GiftCertificateMapper(), id);
        } catch (DataAccessException e) {
            throw new RepositoryException(FIND_CERTIFICATE_BY_PARAM_FAIL);
        }
    }

    public void deleteCertificateAndTagRelation(long idCertificate) throws RepositoryException {
        try {
            jdbcTemplate.update(RELATION_DELETE_BY_CERTIFICATE_ID, idCertificate);
        } catch (DataAccessException e) {
            throw new RepositoryException(DELETE_RELATION_CERTIFICATE_TAG_FAIL);
        }
    }

    public List<GiftCertificate> searchAllCertificatesByTagName(String tagName) throws RepositoryException {
        try {
            return jdbcTemplate.query(SEARCH_ALL_CERTIFICATES_BY_TAG_NAME,
                    new GiftCertificateMapper(), "%" + tagName + "%");
        } catch (DataAccessException e) {
            throw new RepositoryException(FIND_CERTIFICATE_BY_PARAM_FAIL);
        }
    }

    public List<GiftCertificate> sortByParam(SortByParamSpecification sortByParamSpecification) throws
            RepositoryException {
        try {
            return jdbcTemplate.query(sortByParamSpecification.buildQueryForSorting(SORT_CERTIFICATE), new GiftCertificateMapper());
        } catch (DataAccessException e) {
            throw new RepositoryException(SORT_CERTIFICATE_FAIL);
        }
    }
}

