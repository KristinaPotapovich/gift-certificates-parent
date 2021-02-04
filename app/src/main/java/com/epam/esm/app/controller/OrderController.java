package com.epam.esm.app.controller;

import com.epam.esm.service.dto.*;
import com.epam.esm.service.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


/**
 * Order rest controller.
 */
@Validated
@RestController
@RequestMapping(path = "/orders")
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL_FORMS)
public class OrderController {

    private OrderService orderService;
    private static final String VALIDATION_FAIL_PAGE_MESSAGE = "validation_fail_page";
    private static final String VALIDATION_FAIL_SIZE_MESSAGE = "validation_fail_size";
    private static final String CURRENT_ORDER = "current order";
    private static final String DEFAULT_PAGE = "1";
    private static final String DEFAULT_SIZE = "25";
    private static final String VALUE_PAGE = "page";
    private static final String VALUE_SIZE = "size";
    private static final String VALUE_ID = "id";
    private static final String CURRENT_CERTIFICATE = "current certificate";
    private static final String CURRENT_TAG = "current tag";
    private static final String CURRENT_USER = "current user";

    /**
     * Instantiates a new Order controller.
     *
     * @param orderService the order service
     */
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Purchase certificate.
     *
     * @param purchaseParam purchase param
     * @return response entity
     */
    @PostMapping
    public ResponseEntity<EntityModel<OrderDto>> purchaseCertificate(@Valid @RequestBody PurchaseParam purchaseParam) {
        Optional<OrderDto> orderDto = orderService.purchaseCertificate(purchaseParam);
        return orderDto.map(dto -> new ResponseEntity<>(EntityModel.of(dto, linkTo(methodOn(OrderController.class)
                        .purchaseCertificate(purchaseParam)).withSelfRel(),
                linkTo(methodOn(OrderController.class).findOrderById(dto.getId()))
                        .withRel(CURRENT_ORDER)
                        .withType(HttpMethod.GET.name())), HttpStatus.CREATED)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    /**
     * Find certificate by user.
     *
     * @param id   id
     * @param page page
     * @param size size
     * @return response entity
     */
    @GetMapping(value = "/{id}/users")
    public ResponseEntity<List<OrderDto>> findCertificateByUser(
            @Valid @PathVariable(VALUE_ID) Long id,
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL_PAGE_MESSAGE) int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL_SIZE_MESSAGE) int size) {
        Optional<List<OrderDto>> optionalUserDto = orderService.findAllOrdersByUser(id, page, size);
        if (optionalUserDto.isPresent()) {
            optionalUserDto.get().forEach(orderDto -> buildLinkForOrder(orderDto, page, size));
            return new ResponseEntity<>(optionalUserDto.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Find order by id .
     *
     * @param id id
     * @return response entity
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Map<String, Object>> findOrderById(@Valid @PathVariable(VALUE_ID) long id) {
        return orderService.findOrderById(id)
                .map(orderDtoResponse -> new ResponseEntity<>(orderDtoResponse, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    /**
     * Find all orders.
     *
     * @param page page
     * @param size size
     * @return response entity
     */
    @GetMapping
    public ResponseEntity<List<OrderDto>> findAllOrders(
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL_PAGE_MESSAGE) int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL_SIZE_MESSAGE) int size) {
        List<OrderDto> orderDtos = orderService.findAllOrders(page, size);
        if (!orderDtos.isEmpty()) {
            orderDtos.forEach(orderDto -> buildLinkForOrder(orderDto, page, size));
            return new ResponseEntity<>(orderDtos, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private void buildLinkForOrder(OrderDto orderDto, int page, int size) {

        orderDto.add(linkTo(methodOn(OrderController.class)
                .findOrderById(orderDto.getId())).withRel(CURRENT_ORDER)
                .withType(HttpMethod.GET.name()));
        orderDto.getUser().add(linkTo(methodOn(UserController.class)
                .findUserById(orderDto.getUser().getId(), page, size))
                .withRel(CURRENT_USER));
        orderDto.getCertificates()
                .forEach(giftCertificateDto ->
                {
                    giftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class)
                            .findGiftCertificateById(giftCertificateDto.getId(), page, size))
                            .withRel(CURRENT_CERTIFICATE));
                    giftCertificateDto.getTags()
                            .forEach(this::buildTagsLinks);
                });
    }

    private void buildTagsLinks(TagDto tagDto) {
        tagDto.add(linkTo(methodOn(TagController.class)
                .findTagById(tagDto.getId())).withRel(CURRENT_TAG)
                .withType(HttpMethod.GET.name()));
    }
}

