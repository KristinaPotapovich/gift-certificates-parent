package com.epam.esm.app.controller;

import com.epam.esm.app.exception.ControllerException;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.QueryParam;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/users",
        produces = APPLICATION_JSON_VALUE)
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/query")
    public ResponseEntity<UserDto> findUserByLogin(@Valid @QueryParam("login") String login)
            throws ControllerException {
        try {
            return userService.findUserByLogin(login)
                    .map(userDtoResponse -> new ResponseEntity<>(userDtoResponse, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    @GetMapping
    public @ResponseBody
    ResponseEntity<List<UserDto>> findAllUsers() throws ControllerException {
        try {
            return userService
                    .findAll()
                    .map(userDto -> new ResponseEntity<>(userDto, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDto> findUserById(@Valid @PathVariable("id") long id)
            throws ControllerException {
        try {
            return userService
                    .findUserById(id)
                    .map(userDto -> new ResponseEntity<>(userDto, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }
}
