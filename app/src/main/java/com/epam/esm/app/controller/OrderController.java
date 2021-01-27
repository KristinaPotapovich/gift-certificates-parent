package com.epam.esm.app.controller;

import com.epam.esm.app.exception.ControllerException;
import com.epam.esm.service.dto.*;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@RestController
@RequestMapping(path = "/orders")
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL_FORMS)
public class OrderController {

    private OrderService orderService;
    private static final String CURRENT_ORDER = "current order";
    private static final String DEFAULT_PAGE = "1";
    private static final String DEFAULT_SIZE = "25";
    private static final String VALUE_PAGE = "page";
    private static final String VALUE_SIZE = "size";
    private static final String VALUE_ID = "id";
    private static final String VALIDATION_FAIL = "validation_fail";
    private static final String CURRENT_CERTIFICATE = "current certificate";
    private static final String CURRENT_TAG = "current tag";
    private static final String CURRENT_USER = "current user";

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    EntityModel<OrderDto> purchaseCertificate(@Valid @RequestBody PurchaseParam purchaseParam) throws ControllerException {
        try {
            OrderDto orderDto = orderService.purchaseCertificate(purchaseParam).get();
            return EntityModel.of(orderDto, linkTo(methodOn(OrderController.class)
                            .purchaseCertificate(purchaseParam)).withSelfRel(),
                    linkTo(methodOn(OrderController.class).findOrderById(orderDto.getId()))
                            .withRel(CURRENT_ORDER)
                            .withType(HttpMethod.GET.name()));
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    @GetMapping(value = "/users/{id}/orders")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    List<OrderDto> findCertificateByUser(
            @Valid @PathVariable(VALUE_ID) Long id,
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL)
                    int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL)
                    int size) throws ControllerException {
        try {
            Optional<List<OrderDto>> optionalUserDto = orderService.findAllOrdersByUser(id, page, size);
            List<OrderDto> orderDtos = new ArrayList<>();
            if (optionalUserDto.isPresent()) {
                orderDtos = optionalUserDto.get();
                orderDtos.forEach(orderDto -> buildLinkForOrder(orderDto,page,size));
            }
            return orderDtos;
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    ResponseEntity<Map<String, Object>> findOrderById(@Valid @PathVariable(VALUE_ID) long id)
            throws ControllerException {
        try {
            return orderService.findOrderById(id)
                    .map(orderDtoResponse -> new ResponseEntity<>(orderDtoResponse, HttpStatus.CREATED))
                    .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDto> findAllOrders(
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL)
                    int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL)
                    int size) throws ControllerException {
        try {
            List<OrderDto> orderDtos = orderService.findAll(page, size);
            orderDtos.forEach(orderDto -> buildLinkForOrder(orderDto,page,size));
            return orderDtos;
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    private void buildLinkForOrder(OrderDto orderDto, int page, int size) {
        try {
            orderDto.add(linkTo(methodOn(OrderController.class)
                    .findOrderById(orderDto.getId())).withRel(CURRENT_ORDER)
                    .withType(HttpMethod.GET.name()));
            orderDto.getUser().add(linkTo(methodOn(UserController.class)
                    .findUserById(orderDto.getUser().getId(), page, size))
                    .withRel(CURRENT_USER));
            orderDto.getCertificates()
                    .forEach(giftCertificateDto ->
                    {
                        try {
                            giftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class)
                                    .findGiftCertificateById(giftCertificateDto.getId(), page, size))
                                    .withRel(CURRENT_CERTIFICATE));
                            giftCertificateDto.getTags()
                                    .forEach(this::buildTagsLinks);
                        } catch (ControllerException e) {
                            throw new RuntimeException(e.getMessage());
                        }
                    });
        } catch (ControllerException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void buildTagsLinks(TagDto tagDto) {
        try {
            tagDto.add(linkTo(methodOn(TagController.class)
                    .findTagById(tagDto.getId())).withRel(CURRENT_TAG)
                    .withType(HttpMethod.GET.name()));
        } catch (ControllerException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

