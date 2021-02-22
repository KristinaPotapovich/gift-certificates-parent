package com.epam.esm.service.services.impl;

import com.epam.esm.core.entity.User;
import com.epam.esm.core.repository.UserRepository;
import com.epam.esm.service.dto.RequestTokenDto;
import com.epam.esm.service.dto.ResponseTokenDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.jwt.JwtTokenProvider;
import com.epam.esm.service.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private UserRepository userRepository;
    @Value("${jwt.expired}")
    private long validityTokenMillis;
    private static final String USER_NOT_FOUND_MESSAGE = "user_not_found";

    @Autowired
    public AuthenticationServiceImpl(AuthenticationManager authenticationManager,
                                     JwtTokenProvider jwtTokenProvider,
                                     UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseTokenDto getAuthenticationResult(RequestTokenDto requestTokenDto) {
        try {
            String login = requestTokenDto.getLogin();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login,
                    requestTokenDto.getPassword()));
            User user = userRepository.findUserByLogin(login);
            if (user == null) {
                throw new ServiceException(USER_NOT_FOUND_MESSAGE);
            }
            String token = jwtTokenProvider.generateToken(login, user.getUserRole());
            return new ResponseTokenDto(token, validityTokenMillis);
        } catch (AuthenticationException e) {
            throw new ServiceException(USER_NOT_FOUND_MESSAGE);
        }
    }
}
