package com.epam.esm.controller;


import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.impl.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping(path = "/certificate",
        produces = APPLICATION_JSON_VALUE)
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<GiftCertificateDto> findGiftCertificate(@PathVariable("id") long id) {
        return giftCertificateService.findCertificate(id).map(giftCertificateDto -> new ResponseEntity<>(giftCertificateDto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<GiftCertificate> findAllGiftCertificates() {
        GiftCertificate giftCertificate = new GiftCertificate();
        return ResponseEntity.ok(giftCertificate);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    GiftCertificate createGiftCertificated(@RequestBody GiftCertificate giftCertificate) {
        return giftCertificate;
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateGiftCertificate(@PathVariable("id") long id) {
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGiftCertificate(@PathVariable("id") long id) {
    }
}
