package com.epam.esm.core.mapper;

import com.epam.esm.core.entity.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


import static com.epam.esm.core.repository.ColumnName.*;

public class GiftCertificateMapper implements RowMapper<GiftCertificate> {
    @Override
    public GiftCertificate mapRow(ResultSet resultSet, int i) throws SQLException {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(resultSet.getLong(ID_CERTIFICATE));
        giftCertificate.setName(resultSet.getString(NAME));
        giftCertificate.setDescription(resultSet.getString(DESCRIPTION));
        giftCertificate.setPrice(resultSet.getDouble(PRICE));
        giftCertificate.setDurationInDays(resultSet.getInt(DURATION_IN_DAYS));
        giftCertificate.setCreateDate(resultSet.getTimestamp(CREATE_DATE));
        giftCertificate.setLastUpdateDate(resultSet.getTimestamp(LAST_UPDATE_DATE));
        return giftCertificate;
    }
}
