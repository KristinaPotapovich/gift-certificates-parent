package com.epam.esm.app.controller;


import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.services.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


/**
 * Gift certificate rest controller.
 */
@Validated
@RestController
@RequestMapping(path = "/certificates")
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.COLLECTION_JSON)
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateServiceImpl;
    private static final String CURRENT_TAG = "current tag";
    private static final String CURRENT_CERTIFICATE = "current certificate";
    private static final String DELETE_CERTIFICATE = "delete certificate";
    private static final String UPDATE_CERTIFICATE = "update certificate";
    private static final String UPDATE_ONE_FIELD_CERTIFICATE = "update one field certificate";
    private static final String CREATE_CERTIFICATE = "create certificate";
    private static final String DEFAULT_PAGE = "1";
    private static final String DEFAULT_SIZE = "25";
    private static final String VALUE_PAGE = "page";
    private static final String VALUE_SIZE = "size";
    private static final String VALUE_ID = "id";
    private static final String VALIDATION_FAIL_PAGE_MESSAGE = "validation_fail_page";
    private static final String VALIDATION_FAIL_SIZE_MESSAGE = "validation_fail_size";

    /**
     * Instantiates a new Gift certificate controller.
     *
     * @param giftCertificateService gift certificate service
     */
    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateServiceImpl = giftCertificateService;
    }

    /**
     * Find gift certificate by id.
     *
     * @param id   id gift certificate
     * @param page number of page
     * @param size count certificate on page
     * @return response entity
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<EntityModel<GiftCertificateDto>> findGiftCertificateById(
            @Valid @PathVariable(VALUE_ID) long id,
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL_PAGE_MESSAGE) int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL_SIZE_MESSAGE) int size) {
        Optional<GiftCertificateDto> certificateDtoOptional =
                giftCertificateServiceImpl.findCertificateById(id);
        return certificateDtoOptional.map(giftCertificateDto -> new ResponseEntity<>(EntityModel.of(giftCertificateDto,
                linkTo(methodOn(GiftCertificateController.class)
                        .findGiftCertificateById(id, page, size)).withSelfRel(),
                linkTo(methodOn(GiftCertificateController.class)
                        .createGiftCertificate(giftCertificateDto, page, size))
                        .withRel(CREATE_CERTIFICATE)
                        .withType(HttpMethod.POST.name()),
                linkTo(methodOn(GiftCertificateController.class)
                        .deleteGiftCertificate(giftCertificateDto.getId()))
                        .withRel(DELETE_CERTIFICATE)
                        .withType(HttpMethod.DELETE.name()),
                linkTo(methodOn(GiftCertificateController.class)
                        .updateGiftCertificate(giftCertificateDto.getId(),
                                giftCertificateDto, page, size))
                        .withRel(UPDATE_CERTIFICATE)
                        .withType(HttpMethod.PUT.name()),
                linkTo(methodOn(GiftCertificateController.class)
                        .updateOneFieldGiftCertificate(giftCertificateDto.getId(),
                                giftCertificateDto, page, size))
                        .withRel(UPDATE_ONE_FIELD_CERTIFICATE)
                        .withType(HttpMethod.PATCH.name())), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    /**
     * Find all gift certificates.
     *
     * @param page number of page
     * @param size count certificate on page
     * @return the response entity
     */
    @GetMapping
    public ResponseEntity<List<GiftCertificateDto>> findAllGiftCertificates(
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL_PAGE_MESSAGE) int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL_SIZE_MESSAGE) int size,
            @Valid @RequestParam(value = "tags", required = false) List<String> tags,
            @Valid @RequestParam(value = "sort", required = false) String paramForSorting,
            @Valid @RequestParam(value = "order", required = false) String order,
            @Valid @RequestParam(value = "parameter", required = false) String param) {
        List<GiftCertificateDto> giftCertificateDtos = giftCertificateServiceImpl
                .findAllCertificates(param, paramForSorting,tags, order, page, size);
        buildCertificatesLink(page, size, giftCertificateDtos);
        return new ResponseEntity<>(giftCertificateDtos, HttpStatus.OK);
    }

    /**
     * Create gift certificate.
     *
     * @param giftCertificateDto gift certificate dto
     * @param page               page
     * @param size               size
     * @return the response entity
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<EntityModel<GiftCertificateDto>> createGiftCertificate(
            @Valid @RequestBody GiftCertificateDto giftCertificateDto,
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL_PAGE_MESSAGE) int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL_SIZE_MESSAGE) int size) {
        Optional<GiftCertificateDto> giftCertificateDtoOpt = giftCertificateServiceImpl.create(giftCertificateDto);
        return giftCertificateDtoOpt.map(certificateDto -> new ResponseEntity<>(EntityModel.of(certificateDto,
                linkTo(methodOn(GiftCertificateController.class)
                        .createGiftCertificate(giftCertificateDto, page, size)).withSelfRel(),
                linkTo(methodOn(GiftCertificateController.class)
                        .findGiftCertificateById(certificateDto.getId(), page, size))
                        .withRel(CURRENT_CERTIFICATE)
                        .withType(HttpMethod.GET.name()),
                linkTo(methodOn(GiftCertificateController.class)
                        .deleteGiftCertificate(certificateDto.getId()))
                        .withRel(DELETE_CERTIFICATE)
                        .withType(HttpMethod.DELETE.name()),
                linkTo(methodOn(GiftCertificateController.class)
                        .updateGiftCertificate(certificateDto.getId(),
                                certificateDto, page, size))
                        .withRel(UPDATE_CERTIFICATE)
                        .withType(HttpMethod.PUT.name()),
                linkTo(methodOn(GiftCertificateController.class)
                        .updateOneFieldGiftCertificate(certificateDto.getId(),
                                certificateDto, page, size))
                        .withRel(UPDATE_ONE_FIELD_CERTIFICATE)
                        .withType(HttpMethod.PATCH.name())), HttpStatus.CREATED)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    /**
     * Update gift certificate.
     *
     * @param id                 id
     * @param giftCertificateDto gift certificate dto
     * @param page               page
     * @param size               size
     * @return response entity
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<EntityModel<GiftCertificateDto>> updateGiftCertificate(
            @Valid @PathVariable(VALUE_ID) long id,
            @Valid @RequestBody GiftCertificateDto giftCertificateDto,
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL_PAGE_MESSAGE) int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL_SIZE_MESSAGE) int size) {
        giftCertificateDto.setId(id);
        Optional<GiftCertificateDto> certificateDtoOptional = giftCertificateServiceImpl
                .update(giftCertificateDto);
        return certificateDtoOptional.map(certificateDto -> new ResponseEntity<>(EntityModel.of(certificateDto,
                linkTo(methodOn(GiftCertificateController.class)
                        .updateGiftCertificate(giftCertificateDto.getId(),
                                certificateDto, page, size)).withSelfRel(),
                linkTo(methodOn(GiftCertificateController.class)
                        .findGiftCertificateById(certificateDto.getId(), page, size))
                        .withRel(CURRENT_CERTIFICATE)
                        .withType(HttpMethod.GET.name()),
                linkTo(methodOn(GiftCertificateController.class)
                        .deleteGiftCertificate(certificateDto.getId()))
                        .withRel(DELETE_CERTIFICATE)
                        .withType(HttpMethod.DELETE.name()),
                linkTo(methodOn(GiftCertificateController.class)
                        .createGiftCertificate(certificateDto, page, size))
                        .withRel(CREATE_CERTIFICATE)
                        .withType(HttpMethod.POST.name()),
                linkTo(methodOn(GiftCertificateController.class)
                        .updateOneFieldGiftCertificate(certificateDto.getId(),
                                certificateDto, page, size))
                        .withRel(UPDATE_ONE_FIELD_CERTIFICATE)
                        .withType(HttpMethod.PATCH.name())), HttpStatus.CREATED)).orElseGet(() ->
                new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    /**
     * Find all tags by certificate id.
     *
     * @param idCertificate id certificate
     * @param page          page
     * @param size          size
     * @return response entity
     */
    @GetMapping(value = "/{id}/tags")
    public ResponseEntity<List<TagDto>> getInformationAboutCertificatesTags(
            @Valid @PathVariable(VALUE_ID) long idCertificate,
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL_PAGE_MESSAGE) int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL_SIZE_MESSAGE) int size) {
        Optional<List<TagDto>> tagDtosOptional = giftCertificateServiceImpl.getInformationAboutCertificatesTags(idCertificate, page, size);
        if (tagDtosOptional.isPresent()) {
            tagDtosOptional.get().forEach(tag -> tag.add(linkTo(methodOn(TagController.class)
                    .findTagById(tag.getId())).withSelfRel()));
            return new ResponseEntity<>(tagDtosOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Update one field gift certificate.
     *
     * @param id                 id
     * @param giftCertificateDto gift certificate dto
     * @param page               page
     * @param size               size
     * @return the entity model
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping(value = "/{id}")
    public ResponseEntity<EntityModel<GiftCertificateDto>> updateOneFieldGiftCertificate(
            @Valid @PathVariable(VALUE_ID) long id,
            @RequestBody GiftCertificateDto giftCertificateDto,
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL_PAGE_MESSAGE) int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL_SIZE_MESSAGE) int size) {
        giftCertificateDto.setId(id);
        Optional<GiftCertificateDto> certificateDtoOptional =
                giftCertificateServiceImpl.patch(giftCertificateDto);
        return certificateDtoOptional.map(certificateDto -> new ResponseEntity<>(EntityModel.of(certificateDto,
                linkTo(methodOn(GiftCertificateController.class)
                        .updateOneFieldGiftCertificate(giftCertificateDto.getId(),
                                certificateDto, page, size)).withSelfRel(),
                linkTo(methodOn(GiftCertificateController.class)
                        .findGiftCertificateById(certificateDto.getId(), page, size))
                        .withRel(CURRENT_CERTIFICATE)
                        .withType(HttpMethod.GET.name()),
                linkTo(methodOn(GiftCertificateController.class)
                        .deleteGiftCertificate(certificateDto.getId()))
                        .withRel(DELETE_CERTIFICATE)
                        .withType(HttpMethod.DELETE.name()),
                linkTo(methodOn(GiftCertificateController.class)
                        .createGiftCertificate(certificateDto, page, size))
                        .withRel(CREATE_CERTIFICATE)
                        .withType(HttpMethod.POST.name()),
                linkTo(methodOn(GiftCertificateController.class)
                        .updateGiftCertificate(certificateDto.getId(),
                                certificateDto, page, size))
                        .withRel(UPDATE_CERTIFICATE)
                        .withType(HttpMethod.PUT.name())), HttpStatus.CREATED)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    /**
     * Delete gift certificate.
     *
     * @param id id
     * @return response entity
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deleteGiftCertificate(@Valid @PathVariable(VALUE_ID) long id) {
        if (giftCertificateServiceImpl.findCertificateById(id).isPresent()) {
            giftCertificateServiceImpl.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private void buildCertificatesLink(int page, int size,
                                       List<GiftCertificateDto> giftCertificateDtos) {
        giftCertificateDtos.forEach(giftCertificateDto ->
        {
            giftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class)
                    .findGiftCertificateById(giftCertificateDto.getId(), page, size))
                    .withRel(CURRENT_CERTIFICATE));
            giftCertificateDto.getTags()
                    .forEach(this::buildTagsLinks);
        });
    }

    private void buildTagsLinks(TagDto tagDto) {
        tagDto.add(linkTo(methodOn(TagController.class)
                .findTagById(tagDto.getId())).withRel(CURRENT_TAG)
                .withType(HttpMethod.GET.name()));
    }

}
