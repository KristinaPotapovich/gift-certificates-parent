package com.epam.esm.service.services.impl;

import com.epam.esm.core.entity.Order;
import com.epam.esm.core.repository.OrderRepository;
import com.epam.esm.service.dto.*;
import com.epam.esm.service.mapper.OrderConverter;
import com.epam.esm.service.services.GiftCertificateService;
import com.epam.esm.service.services.OrderService;
import com.epam.esm.service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Order service.
 */
@Service
public class OrderServiceImpl implements OrderService {
    private GiftCertificateService giftCertificateService;
    private UserService userService;
    private OrderRepository orderRepository;

    /**
     * Instantiates a new Order service.
     *
     * @param giftCertificateService the gift certificate service
     * @param userService            the user service
     * @param orderRepository        the order repository
     */
    @Autowired
    public OrderServiceImpl(GiftCertificateService giftCertificateService, UserService userService,
                            OrderRepository orderRepository) {
        this.giftCertificateService = giftCertificateService;
        this.userService = userService;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Optional<OrderDto> purchaseCertificate(PurchaseParam purchaseParam) {
        OrderDto orderDto = new OrderDto();
        List<GiftCertificateDto> certificates = purchaseParam.getCertificatesIds()
                .stream()
                .map(idCertificate -> giftCertificateService.findCertificateById(idCertificate))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        userService.findUserById(purchaseParam.getUserId())
                .ifPresent(orderDto::setUser);
        orderDto.setCertificates(certificates);
        BigDecimal fullPrice = certificates
                .stream()
                .map(GiftCertificateDto::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        orderDto.setPrice(fullPrice);
        Order order = orderRepository.create(OrderConverter.mapToOrder(orderDto));
        return Optional.ofNullable(OrderConverter.mapToOrderDto(order));
    }

    @Override
    public Optional<OrderDto> create(OrderDto orderDto) {
        return Optional.empty();
    }

    @Override
    public Optional<OrderDto> update(OrderDto orderDto) {
        return Optional.empty();
    }

    @Override
    public void delete(long id) {

    }

    @Override
    public List<OrderDto> findAllOrders(int page, int size) {
        List<Order> orders;
        orders = orderRepository.findAllOrders(page, size);
        return orders.stream()
                .map(OrderConverter::mapToOrderDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Map<String, Object>> findOrderById(long id) {
        Order order = orderRepository.findOrderById(id);
        OrderDto orderDto = OrderConverter.mapToOrderDto(order);
        Map<String, Object> dataOrder = new HashMap<>();
        dataOrder.put("time_of_purchase", orderDto.getTimeOfPurchase());
        dataOrder.put("price", orderDto.getPrice());
        return Optional.ofNullable(dataOrder);
    }
}
