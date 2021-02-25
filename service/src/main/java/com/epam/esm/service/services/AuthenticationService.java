package com.epam.esm.service.services;

import com.epam.esm.service.dto.RequestTokenDto;
import com.epam.esm.service.dto.ResponseTokenDto;

/**
 * The interface Authentication service.
 */
public interface AuthenticationService {
    /**
     * Gets authentication result.
     *
     * @param requestTokenDto the request token dto
     * @return the authentication result
     */
    ResponseTokenDto getAuthenticationResult(RequestTokenDto requestTokenDto);
}
