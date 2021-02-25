package com.epam.esm.app.config;

import com.epam.esm.service.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String AUTH_LOGIN = "/auth/login";
    private static final String USERS_REGISTRATION = "/users";
    private static final String CERTIFICATES = "/certificates";
    private static final String FIND_CERTIFICATE_BY_ID = "/certificates/{id}";
    private static final String FIND_ALL_TAGS = "/tags";
    private static final String FIND_POPULAR_TAGS = "/tags/popular-tag";
    private static final String FIND_INFORMATION_ABOUT_CERTIFICATES_TAGS = "/certificates/{id}/tags";

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, AUTH_LOGIN).anonymous()
                .antMatchers(HttpMethod.POST, USERS_REGISTRATION).anonymous()
                .antMatchers(HttpMethod.GET, CERTIFICATES).permitAll()
                .antMatchers(HttpMethod.GET, FIND_CERTIFICATE_BY_ID).permitAll()
                .antMatchers(HttpMethod.GET,FIND_ALL_TAGS).permitAll()
                .antMatchers(HttpMethod.GET,FIND_POPULAR_TAGS).permitAll()
                .antMatchers(HttpMethod.GET,FIND_INFORMATION_ABOUT_CERTIFICATES_TAGS).permitAll()
                .anyRequest().authenticated()
                .and()
                .logout()
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl(CERTIFICATES)
                .and()
                .apply(new JwtConfig(jwtTokenProvider))
                .and().cors();
    }
}
