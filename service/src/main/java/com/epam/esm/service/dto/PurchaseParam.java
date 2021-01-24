package com.epam.esm.service.dto;



import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class PurchaseParam {
    private List<Long>certificatesIds;
    private long userId;
}
