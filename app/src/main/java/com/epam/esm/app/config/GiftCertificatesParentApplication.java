package com.epam.esm.app.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;


/**
 * Gift certificates parent application.
 */
@SpringBootApplication
@ComponentScan({"com.epam.esm.service", "com.epam.esm.app", "com.epam.esm.core"})
@EntityScan(basePackages = {"com.epam.esm.core.entity"})
public class GiftCertificatesParentApplication {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(GiftCertificatesParentApplication.class, args);

    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("message");
        source.setDefaultEncoding("UTF-8");
        source.setDefaultLocale(Locale.ENGLISH);
        return source;
    }
}
