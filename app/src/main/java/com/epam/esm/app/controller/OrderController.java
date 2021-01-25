package com.epam.esm.app.controller;

import com.epam.esm.app.exception.ControllerException;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.PurchaseParam;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.QueryParam;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/orders",
        produces = APPLICATION_JSON_VALUE)
public class OrderController {

    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public @ResponseBody
    ResponseEntity<OrderDto> purchaseCertificate(@Valid @RequestBody PurchaseParam purchaseParam) throws ControllerException {
        try {
            return orderService
                    .purchaseCertificate(purchaseParam)
                    .map(orderDtoResponse -> new ResponseEntity<>(orderDtoResponse, HttpStatus.CREATED))
                    .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }
    @GetMapping(value = "/queryUserOrder")
    public @ResponseBody
    ResponseEntity<List<OrderDto>> findCertificateByUser(
            @Valid @QueryParam("idUser") long idUser,
            @Valid @RequestParam(value = "page", required = false, defaultValue = "1")
                    //TODO @Min(value = 1,message =  "page must not be negative")
                    int page,
            @Valid @RequestParam(value = "size", required = false, defaultValue = "25")
                    //TODO @Min(value = 1,message =  "size must not be negative")
                    int size) throws ControllerException {
        try {
            return orderService
                    .findAllByUser(idUser,page,size)
                    .map(orderDtoResponse -> new ResponseEntity<>(orderDtoResponse, HttpStatus.CREATED))
                    .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }
    @GetMapping(value = "/{id}")
    public @ResponseBody
    ResponseEntity<Map<String,Object>> findCertificateById(@Valid @PathVariable("id") long id)
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
    public ResponseEntity<List<OrderDto>> findAllOrders(
            @Valid @RequestParam(value = "page", required = false, defaultValue = "1")
                    //TODO @Min(value = 1,message =  "page must not be negative")
                    int page,
            @Valid @RequestParam(value = "size", required = false, defaultValue = "25")
                    //TODO @Min(value = 1,message =  "size must not be negative")
                    int size) throws ControllerException {
        try {
            return orderService
                    .findAll(page, size)
                    .map(orderDto -> new ResponseEntity<>(orderDto, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }
}

