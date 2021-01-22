package com.epam.esm.core.config;

import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "com.epam.esm",excludeFilters = {
//@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,classes = JdbcConfig.class)
})
public class TestConfig {

@Bean
public DataSource dataSource(){
return new EmbeddedDatabaseBuilder().setName("testDB;MODE=MySql")
        .setType(EmbeddedDatabaseType.H2)
        .generateUniqueName(true)
        .addScripts("gift-certificates-parent.sql", "init-data_test.sql")
        .build();
    }
    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
}
