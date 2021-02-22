package com.epam.esm.service.services;


import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.FullInfoUserDto;
import com.epam.esm.service.dto.UserDto;

import java.util.List;
import java.util.Optional;

/**
 * The interface User service.
 */
public interface UserService extends BaseService<FullInfoUserDto> {
    /**
     * Find user by id optional.
     *
     * @param id the id
     * @return the optional
     */
    Optional<UserDto> findUserById(long id);

    List<FullInfoUserDto> findAllUsers(int page, int size);
    /**
     * Find all orders by user optional.
     *
     * @param id   the id
     * @param page the page
     * @param size the size
     * @return the optional
     */
    Optional<List<OrderDto>> getInformationAboutUsersOrders(long id, int page, int size);
}
