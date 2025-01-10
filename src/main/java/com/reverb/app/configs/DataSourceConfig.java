package com.reverb.app.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.jdbc.DataSourceBuilder;
import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:3000/ReverbDB")
                .username("postgres")
                .password("zaq1@WSX")
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}