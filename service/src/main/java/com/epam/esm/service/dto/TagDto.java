package com.epam.esm.service.dto;


import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * The type Tag dto.
 */
@Getter
@Setter
public class TagDto {
    private long id;
    @NonNull
    @NotBlank
    @Size(min = 3,max = 50)
    @Pattern(regexp = "^[a-zA-Zа-яА-ЯёЁ0-9\\s?!,.:'\\-]+$")
    private String name;
}
