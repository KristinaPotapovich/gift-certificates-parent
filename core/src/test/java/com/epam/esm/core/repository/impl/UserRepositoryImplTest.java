package com.epam.esm.core.repository.impl;

import com.epam.esm.core.entity.User;
import com.epam.esm.core.entity.UserRole;
import com.epam.esm.core.exception.RepositoryException;
import com.epam.esm.core.repository.UserRepository;
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

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@Component
@Transactional
@Sql({"classpath:drop-data-base.sql", "classpath:gift-certificates-parent.sql", "classpath:init-data_test.sql"})
@ContextConfiguration(classes = TestConfig.class)
public class UserRepositoryImplTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void findAll() throws RepositoryException {
        List<User> users = userRepository.findAll(1, 5);
        assertFalse(users.isEmpty());
    }

    @Test
    public void findUserById() throws RepositoryException {
        User user = userRepository.findUserById(1);
        assertNotNull(user);
    }

    @Test
    public void create() throws RepositoryException {
        User user = new User();
        user.setLogin("user");
        user.setPassword("123456");
        user.setUserRole(UserRole.USER);
        user = userRepository.create(user);
        assertNotNull(user);
    }
}