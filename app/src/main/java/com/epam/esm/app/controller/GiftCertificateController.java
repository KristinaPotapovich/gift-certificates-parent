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


@RestController
@RequestMapping(path = "/certificates",
        produces = APPLICATION_JSON_VALUE)
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateServiceImpl;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateServiceImpl = giftCertificateService;
    }

    @GetMapping(value = "/query")
    public ResponseEntity<List<GiftCertificateDto>> findGiftCertificateByParam
            (@QueryParam("param") String param) throws ControllerException {
        try {
            return giftCertificateServiceImpl
                    .findCertificateByParam(param)
                    .map(giftCertificateDto -> new ResponseEntity<>(giftCertificateDto, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (ServiceException e) {
            throw new ControllerException("Gift certificate not found");
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<GiftCertificateDto> findGiftCertificateById(@PathVariable("id") long id)
            throws ControllerException {
        try {
            return giftCertificateServiceImpl
                    .findCertificateById(id)
                    .map(giftCertificateDto -> new ResponseEntity<>(giftCertificateDto, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (ServiceException e) {
            throw new ControllerException("Gift certificate not found");
        }
    }

    @GetMapping
    public ResponseEntity<List<GiftCertificateDto>> findAllGiftCertificates() throws ControllerException {
        try {
            return giftCertificateServiceImpl
                    .findAll()
                    .map(tagDto -> new ResponseEntity<>(tagDto, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (ServiceException e) {
            throw new ControllerException("Gift certificates not found");
        }
    }

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
            throw new ControllerException("Gift certificate creation failed");
        }
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateGiftCertificate(@PathVariable("id") long id,
                                      @RequestBody GiftCertificateDto giftCertificateDto) throws ControllerException {
        giftCertificateDto.setId(id);
        try {
            giftCertificateServiceImpl.update(giftCertificateDto);

        } catch (ServiceException e) {
            throw new ControllerException("Gift certificate update failed");
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Long> deleteGiftCertificate(@PathVariable("id") long id) throws ControllerException {
        try {
            if (giftCertificateServiceImpl.delete(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ServiceException e) {
            throw new ControllerException("Gift certificate delete failed");
        }
    }
}
