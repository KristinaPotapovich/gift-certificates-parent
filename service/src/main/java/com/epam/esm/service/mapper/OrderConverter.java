package com.epam.esm.service.mapper;

import com.epam.esm.core.entity.GiftCertificate;
import com.epam.esm.core.entity.Order;
import com.epam.esm.core.entity.User;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Order converter.
 */
public class OrderConverter {
    /**
     * Map to order dto order dto.
     *
     * @param order the order
     * @return the order dto
     */
    public static OrderDto mapToOrderDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setPrice(order.getPrice());
        if (order.getTimeOfPurchase() != null) {
            orderDto.setTimeOfPurchase(order.getTimeOfPurchase());
        }
        List<GiftCertificate> certificates = order.getCertificates();
        if (certificates != null) {
            List<GiftCertificateDto> certificateDtos = certificates
                    .stream()
                    .filter(Objects::nonNull)
                    .map(GiftCertificateConverter::mapToGiftCertificateDto)
                    .collect(Collectors.toList());
            orderDto.setCertificates(certificateDtos);
            UserDto userDto = UserConverter.mapToUserDto(order.getUser());
            orderDto.setUser(userDto);
        }
        return orderDto;
    }

    /**
     * Map to order order.
     *
     * @param orderDto the order dto
     * @return the order
     */
    public static Order mapToOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setId(orderDto.getId());
        order.setPrice(orderDto.getPrice());
        if (order.getTimeOfPurchase() != null) {
            order.setTimeOfPurchase(orderDto.getTimeOfPurchase());
        }
        List<GiftCertificateDto> certificateDtos = orderDto.getCertificates();
        if (certificateDtos != null) {
            List<GiftCertificate> certificates = certificateDtos
                    .stream()
                    .filter(Objects::nonNull)
                    .map(GiftCertificateConverter::mapToGiftCertificate)
                    .collect(Collectors.toList());
            order.setCertificates(certificates);
            User user = UserConverter.mapToUser(orderDto.getUser());
            order.setUser(user);
        }
        return order;
    }
}
