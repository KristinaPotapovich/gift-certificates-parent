package com.epam.esm.core.repository.impl.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@ComponentScan("com.epam.esm.core")
@EntityScan(basePackages = {"com.epam.esm.core.entity"})
public class TestConfig implements WebMvcConfigurer {
    public static void main(String[] args) {
        SpringApplication.run(TestConfig.class, args);
    }
}
