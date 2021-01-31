package com.epam.esm.service.services;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.PurchaseParam;
import com.epam.esm.service.exception.ServiceException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The interface Order service.
 */
public interface OrderService extends BaseService<OrderDto> {
    /**
     * Purchase certificate optional.
     *
     * @param purchaseParam the purchase param
     * @return the optional
     * @throws ServiceException the service exception
     */
    Optional<OrderDto> purchaseCertificate(PurchaseParam purchaseParam) throws ServiceException;

    /**
     * Find order by id optional.
     *
     * @param id the id
     * @return the optional
     * @throws ServiceException the service exception
     */
    Optional<Map<String, Object>> findOrderById(long id) throws ServiceException;

    /**
     * Find all orders by user optional.
     *
     * @param id   the id
     * @param page the page
     * @param size the size
     * @return the optional
     * @throws ServiceException the service exception
     */
    Optional<List<OrderDto>> findAllOrdersByUser(long id, int page, int size) throws ServiceException;

}
