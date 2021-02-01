package com.epam.esm.app.controller;


import com.epam.esm.app.exception.ControllerException;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.ServiceException;
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
     * @throws ControllerException
     */
    @GetMapping(value = "/byParam")
    public ResponseEntity<List<GiftCertificateDto>> findGiftCertificateByParam
    (@Valid @QueryParam("param") String param,
     @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
     @Min(value = 1, message = VALIDATION_FAIL_MESSAGE)
             int page,
     @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
     @Min(value = 1, message = VALIDATION_FAIL_MESSAGE)
             int size) throws ControllerException {
        ResponseEntity<List<GiftCertificateDto>> responseEntity;
        try {
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
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    private void processExceptionForBuildCertificatesLink(int page, int size,
                                                          List<GiftCertificateDto> giftCertificateDtos) {
        giftCertificateDtos.forEach(giftCertificateDto ->
        {
            try {
                giftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class)
                        .findGiftCertificateById(giftCertificateDto.getId(), page, size))
                        .withRel(CURRENT_CERTIFICATE));
                giftCertificateDto.getTags()
                        .forEach(this::buildTagsLinks);
            } catch (ControllerException e) {
                throw new RuntimeException(e.getMessage());
            }
        });
    }

    private void buildTagsLinks(TagDto tagDto) {
        try {
            tagDto.add(linkTo(methodOn(TagController.class)
                    .findTagById(tagDto.getId())).withRel(CURRENT_TAG)
                    .withType(HttpMethod.GET.name()));
        } catch (ControllerException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Find gift certificate by id.
     *
     * @param id   id gift certificate
     * @param page number of page
     * @param size count certificate on page
     * @return response entity
     * @throws ControllerException the controller exception
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<EntityModel<GiftCertificateDto>> findGiftCertificateById(
            @Valid @PathVariable(VALUE_ID) long id,
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL_MESSAGE)
                    int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL_MESSAGE)
                    int size)
            throws ControllerException {
        try {
            Optional<GiftCertificateDto> certificateDtoOptional =
                    giftCertificateServiceImpl.findCertificateById(id);
            if (certificateDtoOptional.isPresent()) {
                return new ResponseEntity<>(EntityModel.of(certificateDtoOptional.get(),
                        linkTo(methodOn(GiftCertificateController.class)
                                .findGiftCertificateById(id, page, size)).withSelfRel(),
                        linkTo(methodOn(GiftCertificateController.class)
                                .createGiftCertificate(certificateDtoOptional.get(), page, size))
                                .withRel(CREATE_CERTIFICATE)
                                .withType(HttpMethod.POST.name()),
                        linkTo(methodOn(GiftCertificateController.class)
                                .deleteGiftCertificate(certificateDtoOptional.get().getId()))
                                .withRel(DELETE_CERTIFICATE)
                                .withType(HttpMethod.DELETE.name()),
                        linkTo(methodOn(GiftCertificateController.class)
                                .updateGiftCertificate(certificateDtoOptional.get().getId(),
                                        certificateDtoOptional.get(), page, size))
                                .withRel(UPDATE_CERTIFICATE)
                                .withType(HttpMethod.PUT.name()),
                        linkTo(methodOn(GiftCertificateController.class)
                                .updateOneFieldGiftCertificate(certificateDtoOptional.get().getId(),
                                        certificateDtoOptional.get(), page, size))
                                .withRel(UPDATE_ONE_FIELD_CERTIFICATE)
                                .withType(HttpMethod.PATCH.name())), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    /**
     * Find all gift certificates.
     *
     * @param page number of page
     * @param size count certificate on page
     * @return the response entity
     * @throws ControllerException the controller exception
     */
    @GetMapping
    public ResponseEntity<List<GiftCertificateDto>> findAllGiftCertificates(
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL_MESSAGE)
                    int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL_MESSAGE)
                    int size) throws ControllerException {
        try {
            List<GiftCertificateDto> giftCertificateDtos = giftCertificateServiceImpl
                    .findAll(page, size);
            if (!giftCertificateDtos.isEmpty()) {
                processExceptionForBuildCertificatesLink(page, size, giftCertificateDtos);
                return new ResponseEntity<>(giftCertificateDtos, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    /**
     * Find gift certificate by tag name.
     *
     * @param name name of gift certificate
     * @param page number of page
     * @param size count certificate on page
     * @return the response entity
     */
    @GetMapping(value = "/byTag")
    public ResponseEntity<List<GiftCertificateDto>> findGiftCertificateByTagName
    (@Valid @QueryParam("name") String name,
     @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
     @Min(value = 1, message = VALIDATION_FAIL_MESSAGE)
             int page,
     @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
     @Min(value = 1, message = VALIDATION_FAIL_MESSAGE)
             int size) {
        Optional<List<GiftCertificateDto>> giftCertificateDtosOpt = giftCertificateServiceImpl
                .searchAllCertificatesByTagName(name, page, size);
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
     * @throws ControllerException the controller exception
     */
    @GetMapping(value = "/sortBy")
    public ResponseEntity<List<GiftCertificateDto>> sortCertificateByParam
    (@Valid @QueryParam("paramForSorting") String paramForSorting,
     @Valid @QueryParam("order") String order,
     @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
     @Min(value = 1, message = VALIDATION_FAIL_MESSAGE)
             int page,
     @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
     @Min(value = 1, message = VALIDATION_FAIL_MESSAGE)
             int size) throws ControllerException {
        try {
            Optional<List<GiftCertificateDto>> certificatesOpt =
                    giftCertificateServiceImpl.sortByParam(paramForSorting, order, page, size);
            if (certificatesOpt.isPresent()) {
                processExceptionForBuildCertificatesLink(page, size, certificatesOpt.get());
                return new ResponseEntity<>(certificatesOpt.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    /**
     * Create gift certificate.
     *
     * @param giftCertificateDto gift certificate dto
     * @param page               page
     * @param size               size
     * @return the response entity
     * @throws ControllerException the controller exception
     */
    @PostMapping
    public ResponseEntity<EntityModel<GiftCertificateDto>> createGiftCertificate(
            @Valid @RequestBody GiftCertificateDto giftCertificateDto,
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL_MESSAGE)
                    int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL_MESSAGE)
                    int size
    )
            throws ControllerException {
        try {
            Optional<GiftCertificateDto> giftCertificateDtoOpt = giftCertificateServiceImpl.create(giftCertificateDto);
            if (giftCertificateDtoOpt.isPresent()) {
                return new ResponseEntity<>(EntityModel.of(giftCertificateDtoOpt.get(),
                        linkTo(methodOn(GiftCertificateController.class)
                                .createGiftCertificate(giftCertificateDto, page, size)).withSelfRel(),
                        linkTo(methodOn(GiftCertificateController.class)
                                .findGiftCertificateById(giftCertificateDtoOpt.get().getId(), page, size))
                                .withRel(CURRENT_CERTIFICATE)
                                .withType(HttpMethod.GET.name()),
                        linkTo(methodOn(GiftCertificateController.class)
                                .deleteGiftCertificate(giftCertificateDtoOpt.get().getId()))
                                .withRel(DELETE_CERTIFICATE)
                                .withType(HttpMethod.DELETE.name()),
                        linkTo(methodOn(GiftCertificateController.class)
                                .updateGiftCertificate(giftCertificateDtoOpt.get().getId(),
                                        giftCertificateDtoOpt.get(), page, size))
                                .withRel(UPDATE_CERTIFICATE)
                                .withType(HttpMethod.PUT.name()),
                        linkTo(methodOn(GiftCertificateController.class)
                                .updateOneFieldGiftCertificate(giftCertificateDtoOpt.get().getId(),
                                        giftCertificateDtoOpt.get(), page, size))
                                .withRel(UPDATE_ONE_FIELD_CERTIFICATE)
                                .withType(HttpMethod.PATCH.name())), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    /**
     * Update gift certificate.
     *
     * @param id                 id
     * @param giftCertificateDto gift certificate dto
     * @param page               page
     * @param size               size
     * @return response entity
     * @throws ControllerException the controller exception
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<EntityModel<GiftCertificateDto>> updateGiftCertificate(
            @Valid @PathVariable(VALUE_ID) long id,
            @Valid @RequestBody GiftCertificateDto giftCertificateDto,
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL_MESSAGE)
                    int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL_MESSAGE)
                    int size) throws ControllerException {
        giftCertificateDto.setId(id);
        try {
            Optional<GiftCertificateDto> certificateDtoOptional = giftCertificateServiceImpl
                    .update(giftCertificateDto);
            if (certificateDtoOptional.isPresent()) {
                return new ResponseEntity<>(EntityModel.of(certificateDtoOptional.get(),
                        linkTo(methodOn(GiftCertificateController.class)
                                .updateGiftCertificate(giftCertificateDto.getId(),
                                        certificateDtoOptional.get(), page, size)).withSelfRel(),
                        linkTo(methodOn(GiftCertificateController.class)
                                .findGiftCertificateById(certificateDtoOptional.get().getId(), page, size))
                                .withRel(CURRENT_CERTIFICATE)
                                .withType(HttpMethod.GET.name()),
                        linkTo(methodOn(GiftCertificateController.class)
                                .deleteGiftCertificate(certificateDtoOptional.get().getId()))
                                .withRel(DELETE_CERTIFICATE)
                                .withType(HttpMethod.DELETE.name()),
                        linkTo(methodOn(GiftCertificateController.class)
                                .createGiftCertificate(certificateDtoOptional.get(), page, size))
                                .withRel(CREATE_CERTIFICATE)
                                .withType(HttpMethod.POST.name()),
                        linkTo(methodOn(GiftCertificateController.class)
                                .updateOneFieldGiftCertificate(certificateDtoOptional.get().getId(),
                                        certificateDtoOptional.get(), page, size))
                                .withRel(UPDATE_ONE_FIELD_CERTIFICATE)
                                .withType(HttpMethod.PATCH.name())), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
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
     * @throws ControllerException the controller exception
     */
    @PatchMapping(value = "/{id}")
    public ResponseEntity<EntityModel<GiftCertificateDto>> updateOneFieldGiftCertificate(
            @Valid @PathVariable(VALUE_ID) long id,
            @RequestBody GiftCertificateDto giftCertificateDto,
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL_MESSAGE)
                    int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL_MESSAGE)
                    int size) throws ControllerException {
        giftCertificateDto.setId(id);
        try {
            Optional<GiftCertificateDto> certificateDtoOptional =
                    giftCertificateServiceImpl.patch(giftCertificateDto);
            if (certificateDtoOptional.isPresent()) {
                return new ResponseEntity<>(EntityModel.of(certificateDtoOptional.get(),
                        linkTo(methodOn(GiftCertificateController.class)
                                .updateOneFieldGiftCertificate(giftCertificateDto.getId(),
                                        certificateDtoOptional.get(), page, size)).withSelfRel(),
                        linkTo(methodOn(GiftCertificateController.class)
                                .findGiftCertificateById(certificateDtoOptional.get().getId(), page, size))
                                .withRel(CURRENT_CERTIFICATE)
                                .withType(HttpMethod.GET.name()),
                        linkTo(methodOn(GiftCertificateController.class)
                                .deleteGiftCertificate(certificateDtoOptional.get().getId()))
                                .withRel(DELETE_CERTIFICATE)
                                .withType(HttpMethod.DELETE.name()),
                        linkTo(methodOn(GiftCertificateController.class)
                                .createGiftCertificate(certificateDtoOptional.get(), page, size))
                                .withRel(CREATE_CERTIFICATE)
                                .withType(HttpMethod.POST.name()),
                        linkTo(methodOn(GiftCertificateController.class)
                                .updateGiftCertificate(certificateDtoOptional.get().getId(),
                                        certificateDtoOptional.get(), page, size))
                                .withRel(UPDATE_CERTIFICATE)
                                .withType(HttpMethod.PUT.name())), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    /**
     * Delete gift certificate.
     *
     * @param id id
     * @return response entity
     * @throws ControllerException controller exception
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deleteGiftCertificate(@Valid @PathVariable(VALUE_ID) long id) throws ControllerException {
        try {
            if (giftCertificateServiceImpl.findCertificateById(id).isPresent()) {
                giftCertificateServiceImpl.delete(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    /**
     * Find all by several tags list.
     *
     * @param tags tags
     * @param page page
     * @param size size
     * @return list
     * @throws ControllerException the controller exception
     */
    @GetMapping(value = "/byTags")
    public ResponseEntity<List<GiftCertificateDto>> findAllBySeveralTags(
            @Valid @RequestParam(value = "tags", required = false) List<Long> tags,
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL_MESSAGE)
                    int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL_MESSAGE)
                    int size) throws ControllerException {
        try {
            Optional<List<GiftCertificateDto>> certificateDtos =
                    giftCertificateServiceImpl
                            .findAllBySeveralTags(tags, page, size);
            if (certificateDtos.isPresent()) {
                processExceptionForBuildCertificatesLink(page, size, certificateDtos.get());
                return new ResponseEntity<>(certificateDtos.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }
}
