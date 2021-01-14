package com.epam.esm.core.repository.impl;

import com.epam.esm.core.entity.Tag;
import com.epam.esm.core.exception.RepositoryException;
import com.epam.esm.core.mapper.TagMapper;
import com.epam.esm.core.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;

@Repository("tagRepository")
public class TagRepositoryImpl implements TagRepository {
    private JdbcTemplate jdbcTemplate;
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
    public TagRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Tag create(Tag tag) throws RepositoryException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        CREATE_TAG,
                        new String[]{"id"});
                ps.setString(1, tag.getName());
                return ps;
            }, keyHolder);
        } catch (DataAccessException e) {
            throw new RepositoryException("Tag creation failed");
        }
        tag.setName(tag.getName());
        tag.setId(keyHolder.getKey().intValue());
        return tag;
    }

    @Override
    public Tag update(Tag tag) throws RepositoryException {
        try {
            jdbcTemplate.update(UPDATE_TAG, tag.getName(), tag.getId());
            tag = findTagByName(tag.getName());
        } catch (DataAccessException e) {
            throw new RepositoryException("Tag update failed");
        }
        return tag;
    }

    @Override
    public boolean delete(long id) throws RepositoryException {
        try {
            return jdbcTemplate.update(DELETE_TAG, id) > 0;
        } catch (DataAccessException e) {
            throw new RepositoryException("Tag delete failed");
        }
    }

    public Tag findTagByName(String name) throws RepositoryException {
        try {
            return jdbcTemplate.queryForObject(SELECT_TAG_BY_NAME, new TagMapper(), name);
        } catch (DataAccessException e) {
            throw new RepositoryException("Tag by name not found" + name);
        }
    }

    public List<Tag> findAll() throws RepositoryException {
        try {
            return jdbcTemplate.query(SELECT_ALL_TAGS, new TagMapper());
        } catch (DataAccessException e) {
            throw new RepositoryException("Tags not found");
        }
    }

    public boolean isTagExistByName(String name) throws RepositoryException {
        try {
            return jdbcTemplate.queryForObject(SELECT_COUNT_TAG_BY_NAME, Boolean.class, name);
        } catch (DataAccessException e) {
            throw new RepositoryException("Incorrect data");
        }
    }
}

