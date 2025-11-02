package com.davinchicoder.loom_etl_uploader.infrastructure.batch.tasklet;

import com.davinchicoder.loom_etl_uploader.domain.port.WeatherEventPort;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PublishEventTasklet implements Tasklet {

    private final WeatherEventPort eventPort;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        eventPort.weatherSummaryCompleted();

        return RepeatStatus.FINISHED;
    }
}
