package com.epam.esm.config;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RootConfig implements WebMvcConfigurer {
    @Bean
    public GiftCertificate createGiftCertificate(){
        return new GiftCertificate();
    }
    @Bean
    public Tag createTag(){
        return new Tag(12,"celebrating");
    }
}
