package com.epam.esm.core.repository.impl;

import org.junit.runner.RunWith;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@Component
public class TagRepositoryImplTest {

//    @Autowired
//    public TagRepository tagRepository;
//
//    @Test
//    public void create() throws RepositoryException {
//        Tag tag = new Tag();
//        tag.setId(7);
//        tag.setName("new tag");
//        Tag expected = tagRepository.create(tag);
//        List<Tag>tags = tagRepository.findAll(5,6);
//        Tag tagFromDB = tags.get(6);
//        Assertions.assertEquals(expected, tagFromDB);
//    }
//
//    @Test
//    public void delete() throws RepositoryException {
//        Tag tag = new Tag();
//        tag.setId(7);
//        int expectedSizeOfList = tagRepository.findAll(5,6).size() -1;
//        tagRepository.delete(tag);
//        int actualSizeOfList = tagRepository.findAll(5,6).size();
//        Assertions.assertEquals(expectedSizeOfList, actualSizeOfList);
//    }
//
//    @Test
//    public void findTagByName() throws RepositoryException {
//        Optional<Tag>tag = tagRepository.findTagByName("tag two");
//        Assertions.assertTrue(tag.isPresent() && tag.get().getName().equals("tag two"));
//    }
//
//    @Test
//    public void findAll() throws RepositoryException {
//        List<Tag> tags = tagRepository.findAll(5,6);
//        Assertions.assertEquals("tag one", tags.get(0).getName());
//    }
}