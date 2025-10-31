package com.davinchicoder.loom_etl_uploader.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;

@Configuration
@RequiredArgsConstructor
public class S3Config {

    @Value("${app.s3.output-path:s3://etl-weather/output/default.csv}")
    private Resource s3Resource;

    @Bean
    public WritableResource writableResource() {

        return (WritableResource) s3Resource;
    }

}