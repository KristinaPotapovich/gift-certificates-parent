package com.epam.esm.service.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderDto {
    private long id;
    @DecimalMin("0.0")
    private BigDecimal price;
    private LocalDateTime timeOfPurchase;
    private List<GiftCertificateDto> certificates;
    private UserDto user;
}
