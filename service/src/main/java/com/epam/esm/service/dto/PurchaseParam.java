package com.epam.esm.service.dto;


import lombok.*;

import java.util.List;

/**
 * Purchase param.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PurchaseParam {
    private List<Long> certificatesIds;
    private long userId;
}
