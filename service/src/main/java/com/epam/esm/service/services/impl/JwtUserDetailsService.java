package com.epam.esm.service.services.impl;

import com.epam.esm.core.entity.User;
import com.epam.esm.core.repository.UserRepository;
import com.epam.esm.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private static final String USER_NOT_FOUND_MESSAGE = "user_not_found";

    @Autowired
    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) {
        List<User> users = userRepository.findUserByLogin(login);
        if (users.isEmpty()) {
            throw new ServiceException(USER_NOT_FOUND_MESSAGE);
        }
        User user = users.get(0);
        return new org.springframework.security.core.userdetails.User(
                user.getLogin(),
                user.getPassword(),
                getAuthority(user)
        );
    }

    private List<SimpleGrantedAuthority> getAuthority(User user) {
        return Collections.singletonList(new SimpleGrantedAuthority(user.getUserRole().toString()));
    }
}
