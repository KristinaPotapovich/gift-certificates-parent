package com.epam.esm.app.controller;

import com.epam.esm.service.dto.TagDto;
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
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<EntityModel<TagDto>> findTagById(@Valid @PathVariable(VALUE_ID) long id) {
        Optional<TagDto> tagDto = tagServiceImpl.findTagById(id);
        return tagDto.map(dto -> new ResponseEntity<>(EntityModel.of(dto, linkTo(methodOn(TagController.class)
                        .findTagById(id)).withSelfRel(),
                linkTo(methodOn(TagController.class)
                        .createTag(dto)).withRel(CREATE_TAG)
                        .withType(HttpMethod.POST.name()),
                linkTo(methodOn(TagController.class)
                        .deleteTag(dto.getId())).withRel(DELETE_TAG)
                        .withType(HttpMethod.DELETE.name()),
                linkTo(methodOn(TagController.class)
                        .updateTag(dto.getId(), dto)).withRel(UPDATE_TAG)
                        .withType(HttpMethod.PUT.name())), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }


    /**
     * Find all tags.
     *
     * @param page page
     * @param size size
     * @return response entity
     */
    @GetMapping
    public ResponseEntity<List<TagDto>> findAllTags(
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL)
                    int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL)
                    int size) {
        List<TagDto> tagDtos = tagServiceImpl.findAll(page, size);
        processExceptionForFindTagByName(tagDtos);
        return new ResponseEntity<>(tagDtos, HttpStatus.OK);
    }

    private void processExceptionForFindTagByName(List<TagDto> tagDtos) {
        tagDtos.forEach(tag -> tag.add(linkTo(methodOn(TagController.class)
                .findTagById(tag.getId())).withSelfRel()));
    }

    /**
     * Find all tags by certificate id.
     *
     * @param idCertificate id certificate
     * @param page          page
     * @param size          size
     * @return response entity
     */
    @GetMapping(value = "/{id}/certificates")
    public ResponseEntity<List<TagDto>> findAllTagsByCertificateId(
            @Valid @PathVariable(VALUE_ID) long idCertificate,
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL)
                    int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL)
                    int size) {
        Optional<List<TagDto>> tagDtosOptional = tagServiceImpl.findAllTagsByCertificateId(idCertificate, page, size);
        if (tagDtosOptional.isPresent()) {
            processExceptionForFindTagByName(tagDtosOptional.get());
            return new ResponseEntity<>(tagDtosOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Create tag.
     *
     * @param tagDto tag dto
     * @return response entity
     */
    @PostMapping
    public ResponseEntity<EntityModel<TagDto>> createTag(@Valid @RequestBody TagDto tagDto) {
        Optional<TagDto> tagDtos = tagServiceImpl.create(tagDto);
        return tagDtos.map(dto -> new ResponseEntity<>(EntityModel.of(dto, linkTo(methodOn(TagController.class)
                        .createTag(tagDto)).withSelfRel(),
                linkTo(methodOn(TagController.class)
                        .findTagById(dto.getId())).withRel(CURRENT_TAG)
                        .withType(HttpMethod.GET.name()),
                linkTo(methodOn(TagController.class)
                        .deleteTag(dto.getId())).withRel(DELETE_TAG)
                        .withType(HttpMethod.DELETE.name()),
                linkTo(methodOn(TagController.class)
                        .updateTag(dto.getId(), dto)).withRel(UPDATE_TAG)
                        .withType(HttpMethod.PUT.name())), HttpStatus.CREATED)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    /**
     * Update tag.
     *
     * @param id     id
     * @param tagDto tag dto
     * @return response entity
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<EntityModel<TagDto>> updateTag(
            @Valid @PathVariable(VALUE_ID) long id,
            @Valid @RequestBody TagDto tagDto) {
        tagDto.setId(id);
        Optional<TagDto> tagDtoOptional = tagServiceImpl.update(tagDto);
        return tagDtoOptional.map(dto -> new ResponseEntity<>(EntityModel.of(dto, linkTo(methodOn(TagController.class)
                        .updateTag(id, tagDto)).withSelfRel(),
                linkTo(methodOn(TagController.class)
                        .findTagById(dto.getId())).withRel(CURRENT_TAG)
                        .withType(HttpMethod.GET.name()),
                linkTo(methodOn(TagController.class)
                        .deleteTag(dto.getId())).withRel(DELETE_TAG)
                        .withType(HttpMethod.DELETE.name()),
                linkTo(methodOn(TagController.class)
                        .createTag(dto)).withRel(CREATE_TAG)
                        .withType(HttpMethod.POST.name())), HttpStatus.CREATED)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    /**
     * Delete tag.
     *
     * @param id id
     * @return response entity
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deleteTag(
            @Valid @PathVariable(VALUE_ID) long id) {
        if (tagServiceImpl.findTagById(id).isPresent()) {
            tagServiceImpl.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Find popular tag.
     *
     * @return response entity
     */
    @GetMapping(value = "/tag")
    public ResponseEntity<EntityModel<TagDto>> findPopularTag() {
        Optional<TagDto> tagDtoOpt = tagServiceImpl.findPopularTag();
        return tagDtoOpt.map(tagDto -> new ResponseEntity<>(EntityModel.of(tagDto, linkTo(methodOn(TagController.class)
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
                        .withType(HttpMethod.PUT.name())), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }
}
