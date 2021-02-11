package com.epam.esm.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseTokenDto {
    private String token;
    private long tokenValidity;
}
