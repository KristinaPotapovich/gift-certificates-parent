package com.epam.esm.service.dto;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * The type Tag dto.
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "tags")
public class TagDto extends RepresentationModel<TagDto> {
    private long id;
    @NonNull
    @NotBlank
    @Size(min = 3,max = 50)
    @Pattern(regexp = "^[a-zA-Zа-яА-ЯёЁ0-9\\s?!,.:'\\-]+$")
    private String name;
}
