package com.davinchicoder.loom_etl_uploader.infrastructure.storage;

import com.davinchicoder.loom_etl_uploader.domain.model.DailyWeatherSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;

@Configuration
@RequiredArgsConstructor
public class S3Config {

    @Value("${app.s3.output-path}")
    private Resource s3Resource;

    @Bean
    public WritableResource writableResource() {
        return (WritableResource) s3Resource;
    }

    @Bean
    public DelimitedLineAggregator<DailyWeatherSummary> lineAggregator() {
        var extractor = new BeanWrapperFieldExtractor<DailyWeatherSummary>();
        extractor.setNames(new String[]{"date", "avgTemperature", "minTemperature", "maxTemperature", "latitude", "longitude"});

        var aggregator = new DelimitedLineAggregator<DailyWeatherSummary>();
        aggregator.setDelimiter(",");
        aggregator.setFieldExtractor(extractor);

        return aggregator;
    }

}