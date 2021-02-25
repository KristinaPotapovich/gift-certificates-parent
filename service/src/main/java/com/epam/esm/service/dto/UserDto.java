package com.epam.esm.service.dto;

import com.epam.esm.core.entity.Role;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    @Relation(collectionRelation = "users")
    public class UserDto extends RepresentationModel<UserDto> {
        private long id;
        @NotBlank
        @Size(min = 3, max = 20)
        @Pattern(regexp = "^[a-zA-Zа-яА-ЯёЁ0-9\\s?!,.:'\\-]+$")
        private String login;
        @NotNull
        @Size(min = 3, max = 150)
        private Role userRole;
    }
