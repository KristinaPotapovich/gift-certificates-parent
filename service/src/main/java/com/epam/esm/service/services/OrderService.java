package com.epam.esm.service.services;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.PurchaseParam;

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
     */
    Optional<OrderDto> purchaseCertificate(PurchaseParam purchaseParam);

    /**
     * Find order by id optional.
     *
     * @param id the id
     * @return the optional
     */
    Optional<Map<String, Object>> findOrderById(long id);

    /**
     * Find all orders by user optional.
     *
     * @param id   the id
     * @param page the page
     * @param size the size
     * @return the optional
     */
    Optional<List<OrderDto>> findAllOrdersByUser(long id, int page, int size);
    List<OrderDto> findAllOrders(int page, int size);
}
