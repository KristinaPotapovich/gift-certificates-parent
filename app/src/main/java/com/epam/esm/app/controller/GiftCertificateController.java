package com.epam.esm.app.controller;


import com.epam.esm.app.exception.ControllerException;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.services.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


/**
 * The type Gift certificate controller.
 */
@RestController
@RequestMapping(path = "/certificates",
        produces = APPLICATION_JSON_VALUE)
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateServiceImpl;

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
    @GetMapping(value = "/setParam")
    public ResponseEntity<List<GiftCertificateDto>> findGiftCertificateByParam
            (@QueryParam("param") String param) throws ControllerException {
        try {
            return giftCertificateServiceImpl
                    .findCertificateByParam(param)
                    .map(giftCertificateDto -> new ResponseEntity<>(giftCertificateDto, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
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
    public ResponseEntity<GiftCertificateDto> findGiftCertificateById(@PathVariable("id") long id)
            throws ControllerException {
        try {
            return giftCertificateServiceImpl
                    .findCertificateById(id)
                    .map(giftCertificateDto -> new ResponseEntity<>(giftCertificateDto, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
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
    public ResponseEntity<List<GiftCertificateDto>> findAllGiftCertificates() throws ControllerException {
        try {
            return giftCertificateServiceImpl
                    .findAll()
                    .map(tagDto -> new ResponseEntity<>(tagDto, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
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
    public ResponseEntity<List<GiftCertificateDto>> findGiftCertificateByTagName
            (@QueryParam("name") String name) {
        return giftCertificateServiceImpl
                .searchAllCertificatesByTagName(name)
                .map(giftCertificateDto -> new ResponseEntity<>(giftCertificateDto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
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
    public ResponseEntity<List<GiftCertificateDto>> sortCertificateByParam
            (@QueryParam("paramForSorting") String paramForSorting,
             @QueryParam("order") String order) throws ControllerException {
        try {
            return giftCertificateServiceImpl.sortByParam(paramForSorting, order)
                    .map(giftCertificateDto -> new ResponseEntity<>(giftCertificateDto, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
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
    ResponseEntity<GiftCertificateDto> createGiftCertificate(@RequestBody GiftCertificateDto giftCertificateDto)
            throws ControllerException {
        try {
            return giftCertificateServiceImpl
                    .create(giftCertificateDto)
                    .map(giftCertificateDto1 -> new ResponseEntity<>(giftCertificateDto1, HttpStatus.CREATED))
                    .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
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
    @ResponseStatus(HttpStatus.OK)
    public void updateGiftCertificate(@PathVariable("id") long id,
                                      @RequestBody GiftCertificateDto giftCertificateDto) throws ControllerException {
        giftCertificateDto.setId(id);
        try {
            giftCertificateServiceImpl.update(giftCertificateDto);

        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    /**
     * Delete gift certificate response entity.
     *
     * @param id the id
     * @return the response entity
     * @throws ControllerException the controller exception
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Long> deleteGiftCertificate(@PathVariable("id") long id) throws ControllerException {
        try {
            if (giftCertificateServiceImpl.delete(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }
}
