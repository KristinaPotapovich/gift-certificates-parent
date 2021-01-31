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
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Tag rest controller.
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
     * Find tag by name.
     *
     * @param id id
     * @return response entity
     * @throws ControllerException controller exception
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<EntityModel<TagDto>> findTagById(@Valid @PathVariable(VALUE_ID) long id) throws ControllerException {
        try {
            Optional<TagDto> tagDto = tagServiceImpl.findTagById(id);
            if (tagDto.isPresent()) {
                return new ResponseEntity<>(EntityModel.of(tagDto.get(), linkTo(methodOn(TagController.class)
                                .findTagById(id)).withSelfRel(),
                        linkTo(methodOn(TagController.class)
                                .createTag(tagDto.get())).withRel(CREATE_TAG)
                                .withType(HttpMethod.POST.name()),
                        linkTo(methodOn(TagController.class)
                                .deleteTag(tagDto.get().getId())).withRel(DELETE_TAG)
                                .withType(HttpMethod.DELETE.name()),
                        linkTo(methodOn(TagController.class)
                                .updateTag(tagDto.get().getId(), tagDto.get())).withRel(UPDATE_TAG)
                                .withType(HttpMethod.PUT.name())), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }


    /**
     * Find all tags.
     *
     * @param page page
     * @param size size
     * @return response entity
     * @throws ControllerException the controller exception
     */
    @GetMapping
    public @ResponseBody
    ResponseEntity<List<TagDto>> findAllTags(
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL)
                    int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL)
                    int size)
            throws ControllerException {
        try {
            List<TagDto> tagDtos = tagServiceImpl.findAll(page, size);
            if (!tagDtos.isEmpty()) {
                processExceptionForFindTagByName(tagDtos);
                return new ResponseEntity<>(tagDtos, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
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

    /**
     * Find all tags by certificate id.
     *
     * @param idCertificate id certificate
     * @param page          page
     * @param size          size
     * @return response entity
     * @throws ControllerException controller exception
     */
    @GetMapping(value = "/certificates/{id}/tags")
    public @ResponseBody
    ResponseEntity<List<TagDto>> findAllTagsByCertificateId(
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
            if (tagDtosOptional.isPresent()) {
                processExceptionForFindTagByName(tagDtosOptional.get());
                return new ResponseEntity<>(tagDtosOptional.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    /**
     * Create tag.
     *
     * @param tagDto tag dto
     * @return response entity
     * @throws ControllerException controller exception
     */
    @PostMapping
    public @ResponseBody
    ResponseEntity<EntityModel<TagDto>> createTag(@Valid @RequestBody TagDto tagDto)
            throws ControllerException {
        try {
            Optional<TagDto> tagDtos = tagServiceImpl.create(tagDto);
            if (tagDtos.isPresent()) {
                return new ResponseEntity<>(EntityModel.of(tagDtos.get(), linkTo(methodOn(TagController.class)
                                .createTag(tagDto)).withSelfRel(),
                        linkTo(methodOn(TagController.class)
                                .findTagById(tagDtos.get().getId())).withRel(CURRENT_TAG)
                                .withType(HttpMethod.GET.name()),
                        linkTo(methodOn(TagController.class)
                                .deleteTag(tagDtos.get().getId())).withRel(DELETE_TAG)
                                .withType(HttpMethod.DELETE.name()),
                        linkTo(methodOn(TagController.class)
                                .updateTag(tagDtos.get().getId(), tagDtos.get())).withRel(UPDATE_TAG)
                                .withType(HttpMethod.PUT.name())), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    /**
     * Update tag.
     *
     * @param id     id
     * @param tagDto tag dto
     * @return response entity
     * @throws ControllerException controller exception
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<EntityModel<TagDto>> updateTag(
            @Valid @PathVariable(VALUE_ID) long id,
            @Valid @RequestBody TagDto tagDto) throws ControllerException {
        try {
            tagDto.setId(id);
            Optional<TagDto> tagDtoOptional = tagServiceImpl.update(tagDto);
            if (tagDtoOptional.isPresent()) {
                return new ResponseEntity<>(EntityModel.of(tagDtoOptional.get(), linkTo(methodOn(TagController.class)
                                .updateTag(id, tagDto)).withSelfRel(),
                        linkTo(methodOn(TagController.class)
                                .findTagById(tagDtoOptional.get().getId())).withRel(CURRENT_TAG)
                                .withType(HttpMethod.GET.name()),
                        linkTo(methodOn(TagController.class)
                                .deleteTag(tagDtoOptional.get().getId())).withRel(DELETE_TAG)
                                .withType(HttpMethod.DELETE.name()),
                        linkTo(methodOn(TagController.class)
                                .createTag(tagDtoOptional.get())).withRel(CREATE_TAG)
                                .withType(HttpMethod.POST.name())), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    /**
     * Delete tag.
     *
     * @param id id
     * @return response entity
     * @throws ControllerException controller exception
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deleteTag(
            @Valid @PathVariable(VALUE_ID) long id) throws ControllerException {
        try {
            if (tagServiceImpl.findTagById(id).isPresent()) {
                tagServiceImpl.delete(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    /**
     * Find popular tag.
     *
     * @return response entity
     * @throws ControllerException controller exception
     */
    @GetMapping(value = "/byTag")
    public ResponseEntity<EntityModel<TagDto>> findPopularTag() throws ControllerException {

        Optional<TagDto> tagDtoOpt = tagServiceImpl.findPopularTag();
        if (tagDtoOpt.isPresent()) {
            return new ResponseEntity<>(EntityModel.of(tagDtoOpt.get(), linkTo(methodOn(TagController.class)
                            .findPopularTag()).withSelfRel(),
                    linkTo(methodOn(TagController.class)
                            .findTagById(tagDtoOpt.get().getId())).withRel(CURRENT_TAG)
                            .withType(HttpMethod.GET.name()),
                    linkTo(methodOn(TagController.class)
                            .deleteTag(tagDtoOpt.get().getId())).withRel(DELETE_TAG)
                            .withType(HttpMethod.DELETE.name()),
                    linkTo(methodOn(TagController.class)
                            .createTag(tagDtoOpt.get())).withRel(CREATE_TAG)
                            .withType(HttpMethod.POST.name()),
                    linkTo(methodOn(TagController.class)
                            .updateTag(tagDtoOpt.get().getId(), tagDtoOpt.get())).withRel(UPDATE_TAG)
                            .withType(HttpMethod.PUT.name())), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
