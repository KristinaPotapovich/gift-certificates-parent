package com.epam.esm.service.dto;

import com.epam.esm.core.entity.UserRole;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "users")
public class UserDto extends RepresentationModel<UserDto> {
    private long id;
    @NonNull
    @NotBlank
    @Size(min = 3,max = 20)
    @Pattern(regexp = "^[a-zA-Zа-яА-ЯёЁ0-9\\s?!,.:'\\-]+$")
    private String login;
    @NonNull
    @NotBlank
    @Size(min = 3,max = 20)
    private String password;
    private UserRole userRole;
}
