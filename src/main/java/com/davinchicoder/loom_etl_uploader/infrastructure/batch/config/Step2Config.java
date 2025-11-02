package com.davinchicoder.loom_etl_uploader.infrastructure.batch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class Step2Config {


    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final Tasklet metadataTasklet;


    @Bean
    public Step step2() {
        return new StepBuilder("saveMetadata", jobRepository)
                .tasklet(metadataTasklet, platformTransactionManager)
                .build();
    }

}
