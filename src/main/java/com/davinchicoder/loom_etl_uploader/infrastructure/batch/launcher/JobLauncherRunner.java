package com.davinchicoder.loom_etl_uploader.infrastructure.batch.launcher;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobLauncherRunner implements ApplicationRunner {

    private final JobLauncher jobLauncher;
    private final Job job;
    private final ConfigurableApplicationContext context;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        JobParameters params = new JobParametersBuilder()
                .addLong("startTime", System.currentTimeMillis())
                .toJobParameters();

        JobExecution execution = jobLauncher.run(job, params);
        SpringApplication.exit(context, () -> ExitStatus.COMPLETED.equals(execution.getExitStatus()) ? 0 : 1);
    }
}
