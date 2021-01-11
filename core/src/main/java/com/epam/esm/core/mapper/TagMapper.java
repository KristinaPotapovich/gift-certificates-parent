package com.epam.esm.core.mapper;

import com.epam.esm.core.entity.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.epam.esm.core.repository.ColumnName.ID_TAG;
import static com.epam.esm.core.repository.ColumnName.NAME;

public class TagMapper implements RowMapper<Tag> {
    @Override
    public Tag mapRow(ResultSet resultSet, int i) throws SQLException {
        Tag tag = new Tag();
        tag.setId(resultSet.getLong(ID_TAG));
        tag.setName(resultSet.getString(NAME));
        return tag;
    }
}
