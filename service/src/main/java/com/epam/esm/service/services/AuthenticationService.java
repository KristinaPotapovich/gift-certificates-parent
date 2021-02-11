package com.epam.esm.service.services;

import com.epam.esm.service.dto.RequestTokenDto;
import com.epam.esm.service.dto.ResponseTokenDto;

public interface AuthenticationService {
    ResponseTokenDto getAuthenticationResult(RequestTokenDto requestTokenDto);
}
