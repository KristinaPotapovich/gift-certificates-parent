package com.epam.esm.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestTokenDto {
    @NotBlank
    @Size(min = 3, max = 20)
    @Pattern(regexp = "^[a-zA-Zа-яА-ЯёЁ0-9\\s?!,.:'\\-]+$")
    private String login;
    @NotBlank
    @Size(min = 3, max = 200)
    private String password;
}
