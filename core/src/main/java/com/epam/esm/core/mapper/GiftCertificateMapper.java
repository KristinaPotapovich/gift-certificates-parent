package com.epam.esm.core.mapper;

import com.epam.esm.core.entity.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;


/**
 * The type Gift certificate mapper.
 */
public class GiftCertificateMapper implements RowMapper<GiftCertificate> {
    private static final String ID_CERTIFICATE = "id_certificate";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String DURATION_IN_DAYS = "duration";
    private static final String CREATE_DATE = "create_date";
    private static final String LAST_UPDATE_DATE = "last_update_date";

    @Override
    public GiftCertificate mapRow(ResultSet resultSet, int i) throws SQLException {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(resultSet.getLong(ID_CERTIFICATE));
        giftCertificate.setName(resultSet.getString(NAME));
        giftCertificate.setDescription(resultSet.getString(DESCRIPTION));
        giftCertificate.setPrice(resultSet.getDouble(PRICE));
        giftCertificate.setDurationInDays(resultSet.getInt(DURATION_IN_DAYS));
        giftCertificate.setCreateDate(resultSet.getObject(CREATE_DATE, LocalDateTime.class));
        giftCertificate.setLastUpdateDate(resultSet.getObject(LAST_UPDATE_DATE, LocalDateTime.class));
        return giftCertificate;
    }
}
