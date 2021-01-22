package com.epam.esm.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;


/**
 * The type Gift certificate dto.
 */
@Getter
@Setter
public class GiftCertificateDto {
    private long id;
    @NonNull
    @NotBlank
    @Size(min = 3,max = 50)
    @Pattern(regexp = "^[a-zA-Zа-яА-ЯёЁ0-9\\s?!,.:'\\-]+$")
    private String name;
    @NonNull
    @NotBlank
    @Size(min = 3,max = 250)
    @Pattern(regexp = "^[a-zA-Zа-яА-ЯёЁ0-9\\s?!,.:'\\-]+$")
    private String description;
    @NonNull
    @Min(1)
    private double price;
    @Min(1)
    private int durationInDays;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime lastUpdateDate;
    private List<TagDto> tags;
}
