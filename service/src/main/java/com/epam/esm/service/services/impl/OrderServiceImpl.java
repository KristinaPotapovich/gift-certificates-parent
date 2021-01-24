package com.epam.esm.service.services.impl;

import com.epam.esm.core.entity.Order;
import com.epam.esm.core.exception.RepositoryException;
import com.epam.esm.core.repository.OrderRepository;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.PurchaseParam;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.mapper.OrderConverter;
import com.epam.esm.service.services.GiftCertificateService;
import com.epam.esm.service.services.OrderService;
import com.epam.esm.service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private GiftCertificateService giftCertificateService;
    private UserService userService;
    private OrderRepository orderRepository;
    private static final String USER_NOT_FOUND = "user_find_by_id";

    @Autowired
    public OrderServiceImpl(GiftCertificateService giftCertificateService, UserService userService,
                            OrderRepository orderRepository) {
        this.giftCertificateService = giftCertificateService;
        this.userService = userService;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Optional<OrderDto> purchaseCertificate(PurchaseParam purchaseParam) throws ServiceException {
        try {
            OrderDto orderDto = new OrderDto();
            AtomicReference<BigDecimal> fullPrice = new AtomicReference<>(new BigDecimal(0));
            List<GiftCertificateDto> certificates = purchaseParam.getCertificatesIds()
                    .stream()
                    .map(this::processExceptionForFindByCertificateId)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
            UserDto user = userService.findUserById(purchaseParam.getUserId())
                    .orElseThrow(() -> new ServiceException(USER_NOT_FOUND));
            orderDto.setUser(user);
            orderDto.setCertificates(certificates);
            certificates.stream()
                    .map(GiftCertificateDto::getPrice)
                    .map(bigDecimal1 -> fullPrice.get().add(bigDecimal1))
                    .forEach(fullPrice::set);
            orderDto.setPrice(fullPrice.get());
            Order order = orderRepository.create(OrderConverter.mapToOrder(orderDto));
            return Optional.ofNullable(OrderConverter.mapToOrderDto(order));
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    private Optional<GiftCertificateDto> processExceptionForFindByCertificateId(Long aLong) {
        try {
            return giftCertificateService.findCertificateById(aLong);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<OrderDto> create(OrderDto orderDto) throws ServiceException {
        return Optional.empty();
    }

    @Override
    public void update(OrderDto orderDto) throws ServiceException {

    }

    @Override
    public void delete(long id) throws ServiceException {

    }

    @Override
    public Optional<List<OrderDto>> findAll() throws ServiceException {
        return Optional.empty();
    }


    public Optional<List<OrderDto>> findAllByUser(long id) throws ServiceException {
        try {
            List<OrderDto> orderDtos;
            List<Order> orders;
            orders = orderRepository.findAllOrdersByUser(id);
            orderDtos = orders.stream()
                    .map(OrderConverter::mapToOrderDto)
                    .collect(Collectors.toList());
            return Optional.of(orderDtos);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
