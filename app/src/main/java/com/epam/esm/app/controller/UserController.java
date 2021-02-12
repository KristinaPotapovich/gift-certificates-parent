package com.epam.esm.app.controller;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;


import java.util.List;
import java.util.Optional;


/**
 * User rest controller.
 */
@Validated
@RestController
@RequestMapping(path = "/users")
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL_FORMS)
public class UserController {
    private UserService userService;
    private static final String VALIDATION_FAIL_PAGE_MESSAGE = "validation_fail_page";
    private static final String VALIDATION_FAIL_SIZE_MESSAGE = "validation_fail_size";
    private static final String CURRENT_USER = "current user";
    private static final String ORDERS = "orders";
    private static final String DEFAULT_PAGE = "1";
    private static final String DEFAULT_SIZE = "25";
    private static final int PAGE = 1;
    private static final int SIZE = 25;
    private static final String VALUE_PAGE = "page";
    private static final String VALUE_SIZE = "size";
    private static final String VALUE_ID = "id";
    private static final String CURRENT_TAG = "current tag";
    private static final String CURRENT_ORDER = "current order";
    private static final String CURRENT_CERTIFICATE = "current certificate";

    /**
     * Instantiates a new User controller.
     *
     * @param userService the user service
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<EntityModel<UserDto>> create(@RequestBody UserDto userDto) {
        Optional<UserDto> optionalUserDto = userService.create(userDto);
        return optionalUserDto.map(dto -> new ResponseEntity<>(EntityModel.of(dto,
                linkTo(methodOn(UserController.class)
                        .create(userDto)).withSelfRel(),
                linkTo(methodOn(UserController.class).findUserById(dto.getId()))
                        .withRel(CURRENT_USER)
                        .withType(HttpMethod.GET.name())), HttpStatus.CREATED)).orElseGet(() ->
                new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    /**
     * Find all users.
     *
     * @param page page
     * @param size size
     * @return response entity
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDto>> findAllUsers(
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL_PAGE_MESSAGE) int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL_SIZE_MESSAGE) int size) {
        List<UserDto> userDtos = userService.findAllUsers(page, size);
        userDtos.forEach(userDto -> buildLinkForUser(page, size, userDto));
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    /**
     * Find certificate by user.
     *
     * @param id   id
     * @param page page
     * @param size size
     * @return response entity
     */
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @GetMapping(value = "/{id}/orders")
    public ResponseEntity<List<OrderDto>> getInformationAboutUsersOrders(
            @Valid @PathVariable(VALUE_ID) Long id,
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL_PAGE_MESSAGE) int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL_SIZE_MESSAGE) int size) {
        Optional<List<OrderDto>> optionalUserDto = userService.getInformationAboutUsersOrders(id, page, size);
        if (optionalUserDto.isPresent()) {
            optionalUserDto.get().forEach(orderDto -> buildLinkForOrder(orderDto, page, size));
            return new ResponseEntity<>(optionalUserDto.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Find user by id.
     *
     * @param id id
     * @return response entity
     */
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<EntityModel<UserDto>> findUserById(
            @Valid @PathVariable(VALUE_ID) long id) {
        Optional<UserDto> userDtoOpt = userService.findUserById(id);
        return userDtoOpt.map(userDto -> new ResponseEntity<>(EntityModel.of(userDto,
                linkTo(methodOn(UserController.class)
                        .findUserById(userDto.getId()))
                        .withSelfRel(),
                linkTo(methodOn(UserController.class).getInformationAboutUsersOrders(userDto.getId(),
                        PAGE, SIZE))
                        .withRel(ORDERS)
                        .withType(HttpMethod.GET.name())), HttpStatus.OK)).orElseGet(() ->
                new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    private void buildLinkForUser(int page, int size, UserDto userDto) {
        userDto
                .add(linkTo(methodOn(UserController.class).findUserById(userDto.getId()))
                        .withRel(CURRENT_USER)
                        .withType(HttpMethod.GET.name()));
        userDto.add(linkTo(methodOn(UserController.class).getInformationAboutUsersOrders(userDto.getId(), page, size))
                .withRel(ORDERS)
                .withType(HttpMethod.GET.name()));
    }

    private void buildLinkForOrder(OrderDto orderDto, int page, int size) {

        orderDto.add(linkTo(methodOn(OrderController.class)
                .findOrderById(orderDto.getId())).withRel(CURRENT_ORDER)
                .withType(HttpMethod.GET.name()));
        orderDto.getUser().add(linkTo(methodOn(UserController.class)
                .findUserById(orderDto.getUser().getId()))
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

