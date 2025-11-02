package com.davinchicoder.loom_etl_uploader.infrastructure.batch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JobConfig {

    private final JobRepository jobRepository;
    private final Step step1;
    private final Step step2;
    private final Step step3;

    @Bean
    public Job runJob() {
        return new JobBuilder("summarizeWeather", jobRepository)
                .start(step1)
//                .next(step2)
//                .next(step3)
                .build();
    }

}
