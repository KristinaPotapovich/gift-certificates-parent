package com.epam.esm.core.repository.impl;

import com.epam.esm.core.entity.Tag;
import com.epam.esm.core.exception.RepositoryException;
import com.epam.esm.core.repository.TagRepository;
import com.epam.esm.core.repository.impl.config.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@Component
@Transactional
@Sql({"classpath:drop-data-base.sql", "classpath:gift-certificates-parent.sql", "classpath:init-data_test.sql"})
@ContextConfiguration(classes = TestConfig.class)
public class TagRepositoryImplTest {

    @Autowired
    public TagRepository tagRepository;

    @Test
    public void create() throws RepositoryException {
        Tag tag = new Tag();
        tag.setId(7);
        tag.setName("new tag");
        Tag expected = tagRepository.create(tag);
        List<Tag> tags = tagRepository.findAll(1, 7);
        Tag tagFromDB = tags.get(6);
        assertEquals(expected, tagFromDB);
    }

    @Test
    public void delete() throws RepositoryException {
        Tag tag = new Tag();
        tag.setId(5);
        int expectedSizeOfList = tagRepository.findAll(1, 7).size() - 1;
        tagRepository.delete(tag);
        int actualSizeOfList = tagRepository.findAll(1, 7).size();
        assertEquals(expectedSizeOfList, actualSizeOfList);
    }

    @Test
    public void findTagById() throws RepositoryException {
        Optional<Tag> tag = tagRepository.findTagById(4);
        assertTrue(tag.isPresent() && tag.get().getId() == 4);
    }

    @Test
    public void findAll() throws RepositoryException {
        List<Tag> tags = tagRepository.findAll(1, 5);
        assertEquals("tag one", tags.get(0).getName());
    }

    @Test
    public void update() throws RepositoryException {
        Tag tag = new Tag();
        tag.setId(3);
        tag.setName("new name");
        tag = tagRepository.update(tag);
        assertEquals(tag.getName(), "new name");
    }

    @Test
    public void findAllTagsByCertificateId() throws RepositoryException {
        List<Tag> tags = tagRepository.findAllTagsByCertificateId(1, 1, 5);
        assertFalse(tags.isEmpty());
    }

    @Test
    public void findPopularTag() throws RepositoryException {
        Tag tag = tagRepository.findPopularTag();
        assertNotNull(tag);
    }
}