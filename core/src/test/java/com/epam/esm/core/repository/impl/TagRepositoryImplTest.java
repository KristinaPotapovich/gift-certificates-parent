package com.epam.esm.core.repository.impl;

import com.epam.esm.core.config.TestConfig;
import com.epam.esm.core.entity.Tag;
import com.epam.esm.core.exception.RepositoryException;
import com.epam.esm.core.repository.TagRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@Component
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
        List<Tag>tags = tagRepository.findAll();
        Tag tagFromDB = tags.get(6);
        Assertions.assertEquals(expected, tagFromDB);
    }

    @Test
    public void delete() throws RepositoryException {
        int expectedSizeOfList = tagRepository.findAll().size() -1;
        boolean result = tagRepository.delete(5);
        int actualSizeOfList = tagRepository.findAll().size();
        Assertions.assertTrue(result);
        Assertions.assertEquals(expectedSizeOfList, actualSizeOfList);
    }

    @Test
    public void findTagByName() throws RepositoryException {
        Optional<Tag>tag = tagRepository.findTagByName("tag two");
        Assertions.assertTrue(tag.isPresent() && tag.get().getName().equals("tag two"));
    }

    @Test
    public void findAll() throws RepositoryException {
        List<Tag> tags = tagRepository.findAll();
        Assertions.assertEquals("tag one", tags.get(0).getName());
    }

    @Test
    public void findAllTagsByCertificateId() throws RepositoryException {
        List<Tag> tags = tagRepository.findAllTagsByCertificateId(2);
        Assertions.assertFalse(tags.isEmpty());
    }
}