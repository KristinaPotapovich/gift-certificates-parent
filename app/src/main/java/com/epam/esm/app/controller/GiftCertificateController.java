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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.ws.rs.QueryParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


/**
 * Gift certificate rest controller.
 */
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
    private static final String VALIDATION_FAIL_MESSAGE = "validation_fail";

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
     * Find gift certificates by part name or description param.
     *
     * @param param part name or description
     * @param page  number of page
     * @param size  count certificates on page
     * @return List<GiftCertificates> certificates
     */
    @GetMapping(params = {"parameter"})
    public ResponseEntity<List<GiftCertificateDto>> findGiftCertificateByParam
    (@Valid @RequestParam(value = "parameter") String param,
     @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
     @Min(value = 1, message = VALIDATION_FAIL_MESSAGE) int page,
     @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
     @Min(value = 1, message = VALIDATION_FAIL_MESSAGE) int size) {
        ResponseEntity<List<GiftCertificateDto>> responseEntity;
        Optional<List<GiftCertificateDto>> certificateDtosOptional =
                giftCertificateServiceImpl.findCertificateByParam(param, page, size);
        List<GiftCertificateDto> giftCertificateDtos = new ArrayList<>();
        if (certificateDtosOptional.isPresent()) {
            giftCertificateDtos = certificateDtosOptional.get();
            processExceptionForBuildCertificatesLink(page, size, giftCertificateDtos);
            responseEntity = new ResponseEntity<>(giftCertificateDtos, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>(giftCertificateDtos, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    private void processExceptionForBuildCertificatesLink(int page, int size,
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
            @Min(value = 1, message = VALIDATION_FAIL_MESSAGE) int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL_MESSAGE) int size) {
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
            @Min(value = 1, message = VALIDATION_FAIL_MESSAGE) int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL_MESSAGE) int size) {
        List<GiftCertificateDto> giftCertificateDtos = giftCertificateServiceImpl
                .findAll(page, size);
        processExceptionForBuildCertificatesLink(page, size, giftCertificateDtos);
        return new ResponseEntity<>(giftCertificateDtos, HttpStatus.OK);
    }

    /**
     * Find gift certificate by tag tagName.
     *
     * @param tagName tagName of gift certificate
     * @param page number of page
     * @param size count certificate on page
     * @return the response entity
     */
    @GetMapping(params = "tagName")
    public ResponseEntity<List<GiftCertificateDto>> findGiftCertificateByTagName
    (@Valid @RequestParam("tagName") String tagName,
     @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
     @Min(value = 1, message = VALIDATION_FAIL_MESSAGE) int page,
     @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
     @Min(value = 1, message = VALIDATION_FAIL_MESSAGE) int size) {
        Optional<List<GiftCertificateDto>> giftCertificateDtosOpt = giftCertificateServiceImpl
                .searchAllCertificatesByTagName(tagName, page, size);
        if (giftCertificateDtosOpt.isPresent()) {
            processExceptionForBuildCertificatesLink(page, size, giftCertificateDtosOpt.get());
            return new ResponseEntity<>(giftCertificateDtosOpt.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Sort certificate by param name or date.
     *
     * @param paramForSorting param for sorting
     * @param order           order
     * @param page            page
     * @param size            size
     * @return the response entity
     */
    @GetMapping(params = {"sort", "order"})
    public ResponseEntity<List<GiftCertificateDto>> sortCertificateByParam
    (@Valid @RequestParam(value = "sort",defaultValue = "name") String paramForSorting,
     @Valid @RequestParam(value = "order",defaultValue = "asc") String order,
     @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
     @Min(value = 1, message = VALIDATION_FAIL_MESSAGE) int page,
     @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
     @Min(value = 1, message = VALIDATION_FAIL_MESSAGE) int size) {
        Optional<List<GiftCertificateDto>> certificatesOpt =
                giftCertificateServiceImpl.sortByParam(paramForSorting, order, page, size);
        if (certificatesOpt.isPresent()) {
            processExceptionForBuildCertificatesLink(page, size, certificatesOpt.get());
            return new ResponseEntity<>(certificatesOpt.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Create gift certificate.
     *
     * @param giftCertificateDto gift certificate dto
     * @param page               page
     * @param size               size
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<EntityModel<GiftCertificateDto>> createGiftCertificate(
            @Valid @RequestBody GiftCertificateDto giftCertificateDto,
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL_MESSAGE) int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL_MESSAGE) int size) {
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
    @PutMapping(value = "/{id}")
    public ResponseEntity<EntityModel<GiftCertificateDto>> updateGiftCertificate(
            @Valid @PathVariable(VALUE_ID) long id,
            @Valid @RequestBody GiftCertificateDto giftCertificateDto,
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL_MESSAGE) int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL_MESSAGE) int size) {
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
                        .withType(HttpMethod.PATCH.name())), HttpStatus.CREATED)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
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
    @PatchMapping(value = "/{id}")
    public ResponseEntity<EntityModel<GiftCertificateDto>> updateOneFieldGiftCertificate(
            @Valid @PathVariable(VALUE_ID) long id,
            @RequestBody GiftCertificateDto giftCertificateDto,
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL_MESSAGE) int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL_MESSAGE) int size) {
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
        @DeleteMapping(value = "/{id}")
        public ResponseEntity<HttpStatus> deleteGiftCertificate ( @Valid @PathVariable(VALUE_ID) long id) {
            if (giftCertificateServiceImpl.findCertificateById(id).isPresent()) {
                giftCertificateServiceImpl.delete(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

        /**
         * Find all by several tags list.
         *
         * @param tags tags
         * @param page page
         * @param size size
         * @return list
         */
        @GetMapping(params = "tags")
        public ResponseEntity<List<GiftCertificateDto>> findAllBySeveralTags (
                @Valid @RequestParam(value = "tags", required = false) List < Long > tags,
        @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
        @Min(value = 1, message = VALIDATION_FAIL_MESSAGE) int page,
        @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
        @Min(value = 1, message = VALIDATION_FAIL_MESSAGE) int size) {
            Optional<List<GiftCertificateDto>> certificateDtos =
                    giftCertificateServiceImpl
                            .findAllBySeveralTags(tags, page, size);
            if (certificateDtos.isPresent()) {
                processExceptionForBuildCertificatesLink(page, size, certificateDtos.get());
                return new ResponseEntity<>(certificateDtos.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
    }
