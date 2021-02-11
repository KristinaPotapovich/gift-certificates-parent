package com.epam.esm.app.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Application config.
 */
@Configuration
@Component
@ComponentScan({"com.epam.esm.service", "com.epam.esm.app", "com.epam.esm.core"})
@EntityScan(basePackages = {"com.epam.esm.core.entity"})
public class ApplicationConfig {
    /**
     * Message source message source.
     *
     * @return the message source
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("message");
        source.setDefaultEncoding("UTF-8");
        source.setDefaultLocale(Locale.ENGLISH);
        return source;
    }
}
