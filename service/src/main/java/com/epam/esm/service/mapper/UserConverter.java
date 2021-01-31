package com.epam.esm.service.mapper;

import com.epam.esm.core.entity.User;
import com.epam.esm.service.dto.UserDto;

/**
 * User converter.
 */
public class UserConverter {
    /**
     * Map to user dto user dto.
     *
     * @param user the user
     * @return the user dto
     */
    public static UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setLogin(user.getLogin());
        userDto.setPassword(user.getPassword());
        userDto.setUserRole(user.getUserRole());
        return userDto;
    }

    /**
     * Map to user user.
     *
     * @param userDto the user dto
     * @return the user
     */
    public static User mapToUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setLogin(userDto.getLogin());
        user.setPassword(userDto.getPassword());
        user.setUserRole(userDto.getUserRole());
        return user;
    }
}
