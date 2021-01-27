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
 * The type Gift certificate controller.
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
    private static final String VALIDATION_FAIL = "validation_fail";

    /**
     * Instantiates a new Gift certificate controller.
     *
     * @param giftCertificateService the gift certificate service
     */
    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateServiceImpl = giftCertificateService;
    }

    /**
     * Find gift certificate by param response entity.
     *
     * @param param the param
     * @return the response entity
     * @throws ControllerException the controller exception
     */
    @GetMapping(value = "/query")
    @ResponseStatus(HttpStatus.OK)
    public List<GiftCertificateDto> findGiftCertificateByParam
    (@Valid @QueryParam("param") String param,
     @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
     @Min(value = 1, message = VALIDATION_FAIL)
             int page,
     @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
     @Min(value = 1, message = VALIDATION_FAIL)
             int size) throws ControllerException {
        try {
            Optional<List<GiftCertificateDto>> certificateDtosOptional =
                    giftCertificateServiceImpl.findCertificateByParam(param, page, size);
            List<GiftCertificateDto> giftCertificateDtos = new ArrayList<>();
            if (certificateDtosOptional.isPresent()) {
                giftCertificateDtos = certificateDtosOptional.get();
                processExceptionForBuildCertificatesLink(page, size, giftCertificateDtos);
            }
            return giftCertificateDtos;
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
                        .forEach(tagDto -> buildTagsLinks(tagDto));
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
     * Find gift certificate by id response entity.
     *
     * @param id the id
     * @return the response entity
     * @throws ControllerException the controller exception
     */
    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<GiftCertificateDto> findGiftCertificateById(
            @Valid @PathVariable(VALUE_ID) long id,
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL)
                    int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL)
                    int size)
            throws ControllerException {
        try {
            GiftCertificateDto giftCertificateDto = giftCertificateServiceImpl.findCertificateById(id).get();
            return EntityModel.of(giftCertificateDto, linkTo(methodOn(GiftCertificateController.class)
                            .findGiftCertificateById(id, page, size)).withSelfRel(),
                    linkTo(methodOn(GiftCertificateController.class)
                            .createGiftCertificate(giftCertificateDto, page, size)).withRel(CREATE_CERTIFICATE)
                            .withType(HttpMethod.POST.name()),
                    linkTo(methodOn(GiftCertificateController.class)
                            .deleteGiftCertificate(giftCertificateDto.getId())).withRel(DELETE_CERTIFICATE)
                            .withType(HttpMethod.DELETE.name()),
                    linkTo(methodOn(GiftCertificateController.class)
                            .updateGiftCertificate(giftCertificateDto.getId(), giftCertificateDto, page, size))
                            .withRel(UPDATE_CERTIFICATE)
                            .withType(HttpMethod.PUT.name()),
                    linkTo(methodOn(GiftCertificateController.class)
                            .updateOneFieldGiftCertificate(giftCertificateDto.getId(), giftCertificateDto, page, size))
                            .withRel(UPDATE_ONE_FIELD_CERTIFICATE)
                            .withType(HttpMethod.PATCH.name()));
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    /**
     * Find all gift certificates response entity.
     *
     * @return the response entity
     * @throws ControllerException the controller exception
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GiftCertificateDto> findAllGiftCertificates(
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL)
                    int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL)
                    int size) throws ControllerException {
        try {
            List<GiftCertificateDto> giftCertificateDtos = giftCertificateServiceImpl
                    .findAll(page, size);
            processExceptionForBuildCertificatesLink(page, size, giftCertificateDtos);
            return giftCertificateDtos;
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    /**
     * Find gift certificate by tag name response entity.
     *
     * @param name the name
     * @return the response entity
     */
    @GetMapping(value = "/tagParam")
    @ResponseStatus(HttpStatus.OK)
    public List<GiftCertificateDto> findGiftCertificateByTagName
    (@Valid @QueryParam("name") String name,
     @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
     @Min(value = 1, message = VALIDATION_FAIL)
             int page,
     @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
     @Min(value = 1, message = VALIDATION_FAIL)
             int size) {
        List<GiftCertificateDto> giftCertificateDtos = giftCertificateServiceImpl
                .searchAllCertificatesByTagName(name, page, size).get();
        processExceptionForBuildCertificatesLink(page, size, giftCertificateDtos);
        return giftCertificateDtos;
    }

    /**
     * Sort certificate by param response entity.
     *
     * @param paramForSorting the param for sorting
     * @param order           the order
     * @return the response entity
     * @throws ControllerException the controller exception
     */
    @GetMapping(value = "/sortBy")
    @ResponseStatus(HttpStatus.OK)
    public List<GiftCertificateDto> sortCertificateByParam
    (@Valid @QueryParam("paramForSorting") String paramForSorting,
     @Valid @QueryParam("order") String order,
     @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
     @Min(value = 1, message = VALIDATION_FAIL)
             int page,
     @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
     @Min(value = 1, message = VALIDATION_FAIL)
             int size) throws ControllerException {
        try {
            List<GiftCertificateDto> certificateDtos =
                    giftCertificateServiceImpl.sortByParam(paramForSorting, order, page, size).get();
            processExceptionForBuildCertificatesLink(page, size, certificateDtos);
            return certificateDtos;
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    /**
     * Create gift certificate response entity.
     *
     * @param giftCertificateDto the gift certificate dto
     * @return the response entity
     * @throws ControllerException the controller exception
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    EntityModel<GiftCertificateDto> createGiftCertificate(
            @Valid @RequestBody GiftCertificateDto giftCertificateDto,
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL)
                    int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL)
                    int size
    )
            throws ControllerException {
        try {
            giftCertificateDto = giftCertificateServiceImpl.create(giftCertificateDto).get();
            return EntityModel.of(giftCertificateDto, linkTo(methodOn(GiftCertificateController.class)
                            .createGiftCertificate(giftCertificateDto, page, size)).withSelfRel(),
                    linkTo(methodOn(GiftCertificateController.class)
                            .findGiftCertificateById(giftCertificateDto.getId(), page, size)).withRel(CURRENT_CERTIFICATE)
                            .withType(HttpMethod.GET.name()),
                    linkTo(methodOn(GiftCertificateController.class)
                            .deleteGiftCertificate(giftCertificateDto.getId())).withRel(DELETE_CERTIFICATE)
                            .withType(HttpMethod.DELETE.name()),
                    linkTo(methodOn(GiftCertificateController.class)
                            .updateGiftCertificate(giftCertificateDto.getId(), giftCertificateDto, page, size))
                            .withRel(UPDATE_CERTIFICATE)
                            .withType(HttpMethod.PUT.name()),
                    linkTo(methodOn(GiftCertificateController.class)
                            .updateOneFieldGiftCertificate(giftCertificateDto.getId(), giftCertificateDto, page, size))
                            .withRel(UPDATE_ONE_FIELD_CERTIFICATE)
                            .withType(HttpMethod.PATCH.name()));
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    /**
     * Update gift certificate.
     *
     * @param id                 the id
     * @param giftCertificateDto the gift certificate dto
     * @throws ControllerException the controller exception
     */
    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<GiftCertificateDto> updateGiftCertificate(
            @Valid @PathVariable(VALUE_ID) long id,
            @Valid @RequestBody GiftCertificateDto giftCertificateDto,
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL)
                    int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL)
                    int size) throws ControllerException {
        giftCertificateDto.setId(id);
        try {
            giftCertificateDto = giftCertificateServiceImpl.update(giftCertificateDto).get();
            return EntityModel.of(giftCertificateDto, linkTo(methodOn(GiftCertificateController.class)
                            .updateGiftCertificate(giftCertificateDto.getId(), giftCertificateDto, page, size)).withSelfRel(),
                    linkTo(methodOn(GiftCertificateController.class)
                            .findGiftCertificateById(giftCertificateDto.getId(), page, size))
                            .withRel(CURRENT_CERTIFICATE)
                            .withType(HttpMethod.GET.name()),
                    linkTo(methodOn(GiftCertificateController.class)
                            .deleteGiftCertificate(giftCertificateDto.getId())).withRel(DELETE_CERTIFICATE)
                            .withType(HttpMethod.DELETE.name()),
                    linkTo(methodOn(GiftCertificateController.class)
                            .createGiftCertificate(giftCertificateDto, page, size))
                            .withRel(CREATE_CERTIFICATE)
                            .withType(HttpMethod.POST.name()),
                    linkTo(methodOn(GiftCertificateController.class)
                            .updateOneFieldGiftCertificate(giftCertificateDto.getId(), giftCertificateDto, page, size))
                            .withRel(UPDATE_ONE_FIELD_CERTIFICATE)
                            .withType(HttpMethod.PATCH.name()));
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    @PatchMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<GiftCertificateDto> updateOneFieldGiftCertificate(
            @Valid @PathVariable(VALUE_ID) long id,
            @Valid @RequestBody GiftCertificateDto giftCertificateDto,
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL)
                    int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL)
                    int size) throws ControllerException {
        giftCertificateDto.setId(id);
        try {
            giftCertificateDto = giftCertificateServiceImpl.patch(giftCertificateDto).get();
            return EntityModel.of(giftCertificateDto, linkTo(methodOn(GiftCertificateController.class)
                            .updateOneFieldGiftCertificate(giftCertificateDto.getId(), giftCertificateDto, page, size)).withSelfRel(),
                    linkTo(methodOn(GiftCertificateController.class)
                            .findGiftCertificateById(giftCertificateDto.getId(), page, size))
                            .withRel(CURRENT_CERTIFICATE)
                            .withType(HttpMethod.GET.name()),
                    linkTo(methodOn(GiftCertificateController.class)
                            .deleteGiftCertificate(giftCertificateDto.getId())).withRel(DELETE_CERTIFICATE)
                            .withType(HttpMethod.DELETE.name()),
                    linkTo(methodOn(GiftCertificateController.class)
                            .createGiftCertificate(giftCertificateDto, page, size))
                            .withRel(CREATE_CERTIFICATE)
                            .withType(HttpMethod.POST.name()),
                    linkTo(methodOn(GiftCertificateController.class)
                            .updateGiftCertificate(giftCertificateDto.getId(), giftCertificateDto, page, size))
                            .withRel(UPDATE_CERTIFICATE)
                            .withType(HttpMethod.PUT.name()));
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    /**
     * Delete gift certificate response entity.
     *
     * @param id the id
     * @throws ControllerException the controller exception
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<HttpStatus> deleteGiftCertificate(@Valid @PathVariable(VALUE_ID) long id) throws ControllerException {
        try {
            giftCertificateServiceImpl.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    @GetMapping(value = "/findAllBySeveralTags")
    @ResponseStatus(HttpStatus.OK)
    public List<GiftCertificateDto> findAllBySeveralTags(
            @Valid @RequestParam(value = "tags", required = false) List<Long> tags,
            @Valid @RequestParam(value = VALUE_PAGE, required = false, defaultValue = DEFAULT_PAGE)
            @Min(value = 1, message = VALIDATION_FAIL)
                    int page,
            @Valid @RequestParam(value = VALUE_SIZE, required = false, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = VALIDATION_FAIL)
                    int size) throws ControllerException {
        try {
            List<GiftCertificateDto> certificateDtos =
                    giftCertificateServiceImpl
                            .findAllBySeveralTags(tags, page, size).get();
            processExceptionForBuildCertificatesLink(page, size, certificateDtos);
            return certificateDtos;
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }
}
