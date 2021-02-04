package com.epam.esm.app.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Gift certificates parent application.
 */
@SpringBootApplication
public class GiftCertificatesParentApplication {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(GiftCertificatesParentApplication.class, args);

    }
}
