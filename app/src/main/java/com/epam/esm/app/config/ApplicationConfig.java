package com.epam.esm.app.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import org.springframework.stereotype.Component;

import javax.ws.rs.HttpMethod;
import java.util.Locale;

/**
 * Application config.
 */
@Configuration
@Component
@ComponentScan({"com.epam.esm.service", "com.epam.esm.app", "com.epam.esm.core"})
@EntityScan(basePackages = {"com.epam.esm.core.entity"})
//@EnableWebSecurity
public class ApplicationConfig {

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .httpBasic().disable()
//                .csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                .antMatchers(HttpMethod.POST,"").anonymous()
//                .antMatchers(HttpMethod.POST, SIGN_UP_ENDPOINT).anonymous()
//                .antMatchers(HttpMethod.GET, FIND_CERTIFICATES_ENDPOINT).permitAll()
//                .antMatchers(HttpMethod.GET, FIND_CERTIFICATE_BY_ID).permitAll()
//                .anyRequest().fullyAuthenticated()
//                .and()
//                .and().cors();
//    }
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
