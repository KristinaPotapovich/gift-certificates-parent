package com.epam.esm.service.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "orders")
public class OrderDto extends RepresentationModel<OrderDto> {
    private long id;
    @DecimalMin("0.0")
    private BigDecimal price;
    private LocalDateTime timeOfPurchase;
    private List<GiftCertificateDto> certificates;
    private UserDto user;
}
