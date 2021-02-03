package com.epam.esm.app.controller;

import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;


import java.util.List;
import java.util.Optional;


/**
 * User rest controller.
 */
@RestController
@RequestMapping(path = "/users")
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL_FORMS)
public class UserController {
    private UserService userService;
    private static final String CURRENT_USER = "current user";
    private static final String ORDERS = "orders";
    private static final String DEFAULT_PAGE = "1";
    private static final String DEFAULT_SIZE = "25";
    private static final String VALUE_PAGE = "page";
    private static final String VALUE_SIZE = "size";
    private static final String VALUE_ID = "id";
    private static final String VALIDATION_FAIL = "validation_fail";

    /**
     * Instantiates a new User controller.
     *
     * @param userService the user service
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Find all users.
     *
     * @param page page
     * @param size size
     * @return response entity
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> findAllUsers(
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL) int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL) int size) {
        List<UserDto> userDtos = userService.findAllUsers(page, size);
        userDtos.forEach(userDto -> processExceptionByFindUser(page, size, userDto));
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    private void processExceptionByFindUser(int page, int size, UserDto userDto) {
        userDto
                .add(linkTo(methodOn(UserController.class).findUserById(userDto.getId(), page, size))
                        .withRel(CURRENT_USER)
                        .withType(HttpMethod.GET.name()));
        userDto.add(linkTo(methodOn(OrderController.class).findCertificateByUser(userDto.getId(), page, size))
                .withRel(ORDERS)
                .withType(HttpMethod.GET.name()));
    }

    /**
     * Find user by id.
     *
     * @param id   id
     * @param page page
     * @param size size
     * @return response entity
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<EntityModel<UserDto>> findUserById(
            @Valid @PathVariable(VALUE_ID) long id,
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL) int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL) int size) {
        Optional<UserDto> userDtoOpt = userService.findUserById(id);
        return userDtoOpt.map(userDto -> new ResponseEntity<>(EntityModel.of(userDto, linkTo(methodOn(UserController.class)
                        .findUserById(userDto.getId(), page, size))
                        .withSelfRel(),
                linkTo(methodOn(OrderController.class).findCertificateByUser(userDto.getId(),
                        page, size))
                        .withRel(ORDERS)
                        .withType(HttpMethod.GET.name())), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }
}
