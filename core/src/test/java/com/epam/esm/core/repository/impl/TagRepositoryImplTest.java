package com.epam.esm.core.repository.impl;

import com.epam.esm.core.entity.Tag;
import com.epam.esm.core.repository.TagRepository;
import com.epam.esm.core.repository.impl.config.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
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
    public void createPositiveTest() {
        Tag tag = new Tag();
        tag.setName("new tag");
        Tag actual = tagRepository.create(tag);
        assertNotNull(tag);
        assertTrue(actual.getId() > 0);
        assertEquals("new tag", actual.getName());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void createNegativeTest() {
        Tag tag = new Tag();
        tag.setName("tag one");
        tagRepository.create(tag);
    }

    @Test
    public void deletePositiveTest() {
        Tag tag = new Tag();
        tag.setId(5);
        int expectedSizeOfList = tagRepository.findAllTags(1, 7).size() - 1;
        tagRepository.delete(tag);
        int actualSizeOfList = tagRepository.findAllTags(1, 7).size();
        assertNotEquals(0, actualSizeOfList);
        assertEquals(expectedSizeOfList, actualSizeOfList);
    }

    @Test
    public void findTagByIdPositiveTest()  {
        Optional<Tag> tag = tagRepository.findTagById(4);
        assertTrue(tag.isPresent() && tag.get().getId() == 4);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void findTagByIdNegativeTest() {
        tagRepository.findTagById(7);
    }

    @Test
    public void findAllPositiveTest()  {
        List<Tag> tags = tagRepository.findAllTags(1, 5);
        assertFalse(tags.isEmpty());
        assertEquals(1, tags.get(0).getId());
        assertEquals("tag one", tags.get(0).getName());
    }

    @Test
    public void updatePositiveTest() {
        Tag tag = new Tag();
        tag.setId(3);
        tag.setName("new name");
        tag = tagRepository.update(tag);
        assertNotNull(tag);
        assertTrue(tag.getId() > 0);
        assertEquals(tag.getName(), "new name");
    }

    @Test
    public void findAllTagsByCertificateIdPositiveTest() {
        List<Tag> tags = tagRepository.findAllTagsByCertificateId(1, 1, 5);
        assertFalse(tags.isEmpty());
        assertEquals(1, tags.get(0).getId());
    }

    @Test
    public void findAllTagsByCertificateIdNegativeTest() {
        List<Tag> tags = tagRepository.findAllTagsByCertificateId(0, 1, 5);
        assertTrue(tags.isEmpty());
    }

    @Test
    public void findPopularTagPositiveTest() {
        Tag tag = tagRepository.findPopularTag();
        assertNotNull(tag);
    }
}