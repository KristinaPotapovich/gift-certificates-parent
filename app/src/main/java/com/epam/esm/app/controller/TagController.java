package com.epam.esm.app.controller;


import com.epam.esm.app.exception.ControllerException;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * The type Tag controller.
 */
@RestController
@RequestMapping(path = "/tags",
        produces = APPLICATION_JSON_VALUE)
public class TagController {

    private TagService tagServiceImpl;
    private static final String CURRENT_TAG = "current tag";
    private static final String DELETE_TAG = "delete tag";
    private static final String UPDATE_TAG = "update tag";
    private static final String CREATE_TAG = "create tag";
    private static final String DEFAULT_PAGE = "1";
    private static final String DEFAULT_SIZE = "25";
    private static final String VALUE_PAGE = "page";
    private static final String VALUE_SIZE = "size";
    private static final String VALUE_ID = "id";
    private static final String VALIDATION_FAIL = "validation_fail";

    /**
     * Instantiates a new Tag controller.
     *
     * @param tagServiceImpl the tag service
     */
    @Autowired
    public TagController(TagService tagServiceImpl) {
        this.tagServiceImpl = tagServiceImpl;
    }

    /**
     * Find tag by name response entity.
     *
     * @return the response entity
     * @throws ControllerException the controller exception
     */
    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<TagDto> findTagById(@Valid @PathVariable(VALUE_ID) long id) throws ControllerException {
        try {
            TagDto tagDto = tagServiceImpl.findTagById(id).get();
            return EntityModel.of(tagDto, linkTo(methodOn(TagController.class)
                            .findTagById(id)).withSelfRel(),
                    linkTo(methodOn(TagController.class)
                            .createTag(tagDto)).withRel(CREATE_TAG)
                            .withType(HttpMethod.POST.name()),
                    linkTo(methodOn(TagController.class)
                            .deleteTag(tagDto.getId())).withRel(DELETE_TAG)
                            .withType(HttpMethod.DELETE.name()),
                    linkTo(methodOn(TagController.class)
                            .updateTag(tagDto.getId(), tagDto)).withRel(UPDATE_TAG)
                            .withType(HttpMethod.PUT.name()));
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }


    /**
     * Find all tags response entity.
     *
     * @return the response entity
     * @throws ControllerException the controller exception
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    List<TagDto> findAllTags(
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL)
                    int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL)
                    int size)
            throws ControllerException {
        try {
            List<TagDto> tagDtos = tagServiceImpl.findAll(page, size);
            processExceptionForFindTagByName(tagDtos);
            return tagDtos;
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    private void processExceptionForFindTagByName(List<TagDto> tagDtos) {
        tagDtos.forEach(tag ->
        {
            try {
                tag.add(linkTo(methodOn(TagController.class).findTagById(tag.getId())).withSelfRel());
            } catch (ControllerException e) {
                throw new RuntimeException(e.getMessage());
            }
        });
    }

    @GetMapping(value = "/certificates/{id}/tags")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    List<TagDto> findAllTagsByCertificateId(
            @Valid @PathVariable(VALUE_ID) long idCertificate,
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL)
                    int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL)
                    int size)
            throws ControllerException {
        try {
            Optional<List<TagDto>> tagDtosOptional = tagServiceImpl.findAllTagsByCertificateId(idCertificate, page, size);
            List<TagDto> tagDtos = new ArrayList<>();
            if (tagDtosOptional.isPresent()) {
                tagDtos = tagDtosOptional.get();
                processExceptionForFindTagByName(tagDtos);
            }
            return tagDtos;
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    /**
     * Create tag response entity.
     *
     * @param tagDto the tag dto
     * @return the response entity
     * @throws ControllerException the controller exception
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    EntityModel<TagDto> createTag(@Valid @RequestBody TagDto tagDto) throws ControllerException {
        try {
            tagDto = tagServiceImpl.create(tagDto).get();
            return EntityModel.of(tagDto, linkTo(methodOn(TagController.class)
                            .createTag(tagDto)).withSelfRel(),
                    linkTo(methodOn(TagController.class)
                            .findTagById(tagDto.getId())).withRel(CURRENT_TAG)
                            .withType(HttpMethod.GET.name()),
                    linkTo(methodOn(TagController.class)
                            .deleteTag(tagDto.getId())).withRel(DELETE_TAG)
                            .withType(HttpMethod.DELETE.name()),
                    linkTo(methodOn(TagController.class)
                            .updateTag(tagDto.getId(), tagDto)).withRel(UPDATE_TAG)
                            .withType(HttpMethod.PUT.name()));
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    /**
     * Update tag.
     *
     * @param id     the id
     * @param tagDto the tag dto
     * @throws ControllerException the controller exception
     */
    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<TagDto> updateTag(
            @Valid @PathVariable(VALUE_ID) long id,
            @Valid @RequestBody TagDto tagDto) throws ControllerException {
        try {
            tagDto.setId(id);
            tagDto = tagServiceImpl.update(tagDto).get();
            return EntityModel.of(tagDto, linkTo(methodOn(TagController.class)
                            .updateTag(id, tagDto)).withSelfRel(),
                    linkTo(methodOn(TagController.class)
                            .findTagById(tagDto.getId())).withRel(CURRENT_TAG)
                            .withType(HttpMethod.GET.name()),
                    linkTo(methodOn(TagController.class)
                            .deleteTag(tagDto.getId())).withRel(DELETE_TAG)
                            .withType(HttpMethod.DELETE.name()),
                    linkTo(methodOn(TagController.class)
                            .createTag(tagDto)).withRel(CREATE_TAG)
                            .withType(HttpMethod.POST.name()));
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    /**
     * Delete tag response entity.
     *
     * @param id the id
     * @return the response entity
     * @throws ControllerException the controller exception
     */

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deleteTag(
            @Valid @PathVariable(VALUE_ID) long id) throws ControllerException {
        try {
            tagServiceImpl.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    @GetMapping(value = "/queryPopularTag")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<TagDto> findPopularTag() throws ControllerException {

        TagDto tagDto = tagServiceImpl.findPopularTag().get();
        return EntityModel.of(tagDto, linkTo(methodOn(TagController.class)
                        .findPopularTag()).withSelfRel(),
                linkTo(methodOn(TagController.class)
                        .findTagById(tagDto.getId())).withRel(CURRENT_TAG)
                        .withType(HttpMethod.GET.name()),
                linkTo(methodOn(TagController.class)
                        .deleteTag(tagDto.getId())).withRel(DELETE_TAG)
                        .withType(HttpMethod.DELETE.name()),
                linkTo(methodOn(TagController.class)
                        .createTag(tagDto)).withRel(CREATE_TAG)
                        .withType(HttpMethod.POST.name()),
                linkTo(methodOn(TagController.class)
                        .updateTag(tagDto.getId(), tagDto)).withRel(UPDATE_TAG)
                        .withType(HttpMethod.PUT.name()));
    }
}
