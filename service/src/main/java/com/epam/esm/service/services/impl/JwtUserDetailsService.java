package com.epam.esm.service.services.impl;

import com.epam.esm.core.entity.User;
import com.epam.esm.core.repository.UserRepository;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.jwt.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("jwtUserDetailsService")
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
        return new JwtUser(
                user.getId(),
                user.getLogin(),
                user.getPassword(),
                getAuthority(user)
        );
    }

    private List<GrantedAuthority> getAuthority(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getUserRole().toString()));
        return authorities;
    }
}
