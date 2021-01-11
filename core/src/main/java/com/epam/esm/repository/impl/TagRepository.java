package com.epam.esm.repository.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.repository.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class TagRepository implements BaseRepository<Tag> {
    JdbcTemplate jdbcTemplate;
    private static final String CREATE_TAG =
            "INSERT INTO tag(name) VALUES (?)";
    private static final String UPDATE_TAG =
            "UPDATE tag SET name = ? WHERE id_tag = ?";
    private static final String DELETE_TAG =
            "DELETE FROM tag WHERE id_tag = ?";
    private static final String SELECT_TAG_BY_NAME =
            "SELECT id_tag, name FROM tag WHERE name = ?";
    private static final String SELECT_ALL_TAGS =
            "SELECT id_tag, name FROM tag";
    private static final String SELECT_TAG_BY_ID =
            "SELECT COUNT(*) FROM tag WHERE id_tag = ?";
    private static final String SELECT_COUNT_TAG_BY_NAME =
            "SELECT COUNT(*) FROM tag WHERE name = ?";

    @Autowired
    public TagRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Tag create(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    CREATE_TAG,
                    new String[]{"id"});
            ps.setString(1, tag.getName());
            return ps;
        }, keyHolder);
        tag.setName(tag.getName());
        tag.setId(keyHolder.getKey().intValue());
        return tag;
    }

    @Override
    public void update(Tag tag) {
        jdbcTemplate.update(UPDATE_TAG, tag.getName(), tag.getId());
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update(DELETE_TAG, id);
    }

    public Tag findTagByName(String name) {
        return jdbcTemplate.queryForObject(SELECT_TAG_BY_NAME, new TagMapper(), name);
    }

    public List<Tag> findAllTags() {
        return jdbcTemplate.query(SELECT_ALL_TAGS, new TagMapper());
    }

    public boolean isTagExistById(long id) {
        return jdbcTemplate.queryForObject(SELECT_TAG_BY_ID, Boolean.class, id);
    }
    public boolean isTagExistByName(String name){
        return jdbcTemplate.queryForObject(SELECT_COUNT_TAG_BY_NAME, Boolean.class, name);
    }
}

