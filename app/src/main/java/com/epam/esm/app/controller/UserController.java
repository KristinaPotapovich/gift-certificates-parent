package com.epam.esm.app.controller;

import com.epam.esm.app.exception.ControllerException;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;


import java.util.List;


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

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    List<UserDto> findAllUsers(
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL)
                    int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL)
                    int size)
            throws ControllerException {
        try {
            List<UserDto> userDtos = userService.findAll(page, size);
            userDtos.forEach(userDto -> processExceptionByFindUser(page, size, userDto));
            return userDtos;
        } catch (
                ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    private void processExceptionByFindUser(int page, int size, UserDto userDto) {
        try {
            userDto
                    .add(linkTo(methodOn(UserController.class).findUserById(userDto.getId(), page, size))
                            .withRel(CURRENT_USER)
                            .withType(HttpMethod.GET.name()));
            userDto.add(linkTo(methodOn(OrderController.class).findCertificateByUser(userDto.getId(), page, size))
                    .withRel(ORDERS)
                    .withType(HttpMethod.GET.name()));
        } catch (ControllerException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<UserDto> findUserById(
            @Valid @PathVariable(VALUE_ID) long id,
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL)
                    int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL)
                    int size)
            throws ControllerException {
        try {
            UserDto userDto = userService.findUserById(id).get();
            return EntityModel.of(userDto, linkTo(methodOn(UserController.class).findUserById(userDto.getId(), page, size))
                            .withSelfRel(),
                    linkTo(methodOn(OrderController.class).findCertificateByUser(userDto.getId(), page, size))
                            .withRel(ORDERS)
                            .withType(HttpMethod.GET.name()));
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }
}
