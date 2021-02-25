package com.epam.esm.app.controller;

import com.epam.esm.service.dto.RequestTokenDto;
import com.epam.esm.service.dto.ResponseTokenDto;
import com.epam.esm.service.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Valid
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(value = "/login")
    public ResponseTokenDto login(@RequestBody @Valid RequestTokenDto requestDto) {
        return authenticationService.getAuthenticationResult(requestDto);
    }
}