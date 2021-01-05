package com.epam.esm.controller;


import com.epam.esm.config.RootConfig;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping(path = "/certificate",
        produces = APPLICATION_JSON_VALUE)
public class GiftCertificateController {
    ApplicationContext javaConfigContext =
            new AnnotationConfigApplicationContext(RootConfig.class);
    @GetMapping(value = "/{id}")
    public GiftCertificate findGiftCertificate(@PathVariable("id") long id) {
        return javaConfigContext.getBean(GiftCertificate.class);
    }

    @GetMapping
    public ResponseEntity<GiftCertificate> findAllGiftCertificates() {
        GiftCertificate giftCertificate = javaConfigContext.getBean(GiftCertificate.class);
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
