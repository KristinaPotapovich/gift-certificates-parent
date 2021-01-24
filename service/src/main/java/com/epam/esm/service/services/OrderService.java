package com.epam.esm.service.services;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.PurchaseParam;
import com.epam.esm.service.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface OrderService extends BaseService<OrderDto> {
    Optional<OrderDto> purchaseCertificate(PurchaseParam purchaseParam) throws ServiceException;

    Optional<List<OrderDto>> findAllByUser(long id) throws ServiceException;
}
