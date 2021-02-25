package com.epam.esm.service.mapper;

import com.epam.esm.core.entity.User;
import com.epam.esm.service.dto.FullInfoUserDto;

/**
 * User converter.
 */
public class UserFullInfoConverter {
    /**
     * Map to user dto user dto.
     *
     * @param user the user
     * @return the user dto
     */
    public static FullInfoUserDto mapToUserDto(User user) {
        FullInfoUserDto fullInfoUserDto = new FullInfoUserDto();
        fullInfoUserDto.setId(user.getId());
        fullInfoUserDto.setLogin(user.getLogin());
        fullInfoUserDto.setPassword(user.getPassword());
        fullInfoUserDto.setUserRole(user.getUserRole());
        return fullInfoUserDto;
    }

    /**
     * Map to user user.
     *
     * @param fullInfoUserDto the user dto
     * @return the user
     */
    public static User mapToUser(FullInfoUserDto fullInfoUserDto) {
        User user = new User();
        user.setId(fullInfoUserDto.getId());
        user.setLogin(fullInfoUserDto.getLogin());
        user.setPassword(fullInfoUserDto.getPassword());
        user.setUserRole(fullInfoUserDto.getUserRole());
        return user;
    }

}
