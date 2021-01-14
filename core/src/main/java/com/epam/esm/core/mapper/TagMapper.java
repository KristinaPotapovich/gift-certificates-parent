package com.epam.esm.core.mapper;

import com.epam.esm.core.entity.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class TagMapper implements RowMapper<Tag> {
    private static final String NAME = "name";
    private static final String ID_TAG = "id_tag";

    @Override
    public Tag mapRow(ResultSet resultSet, int i) throws SQLException {
        Tag tag = new Tag();
        tag.setId(resultSet.getLong(ID_TAG));
        tag.setName(resultSet.getString(NAME));
        return tag;
    }
}
