package com.davinchicoder.loom_etl_uploader.infrastructure.batch.config;

import com.davinchicoder.loom_etl_uploader.domain.model.DailyWeatherSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class Step1Config {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final ItemReader<DailyWeatherSummary> readerStep1;
    private final ItemStreamWriter<DailyWeatherSummary> writerStep1;


    @Bean
    public Step step1() {
        return new StepBuilder("uploadDailyWeatherSummary", jobRepository)
                .<DailyWeatherSummary, DailyWeatherSummary>chunk(10, platformTransactionManager)
                .reader(readerStep1)
                .writer(writerStep1)
                .build();
    }

}
