package com.davinchicoder.loom_etl_uploader.infrastructure.batch.tasklet;

import com.davinchicoder.loom_etl_uploader.domain.model.JobMetadata;
import com.davinchicoder.loom_etl_uploader.domain.port.JobMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class MetadataTasklet implements Tasklet {

    private final JobMetadataRepository repository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        JobMetadata metadata = JobMetadata.builder()
                .id(UUID.randomUUID().toString())
                .jobId(contribution.getStepExecution().getJobExecutionId().toString())
                .jobName(contribution.getStepExecution().getJobExecution().getJobInstance().getJobName())
                .stepName(contribution.getStepExecution().getStepName())
                .startTime(contribution.getStepExecution().getStartTime())
                .endTime(contribution.getStepExecution().getEndTime())
                .duration(contribution.getStepExecution().getEndTime().toEpochSecond(ZoneOffset.UTC) - contribution.getStepExecution().getStartTime().toEpochSecond(ZoneOffset.UTC))
                .status(contribution.getStepExecution().getExitStatus().getExitCode())
                .errorMessage(contribution.getStepExecution().getFailureExceptions().isEmpty() ? null : contribution.getStepExecution().getFailureExceptions().get(0).getMessage())
                .itemsRead(contribution.getStepExecution().getReadCount())
                .itemsProcessed(contribution.getStepExecution().getFilterCount())
                .itemsWritten(contribution.getStepExecution().getWriteCount())
                .itemsFailed(contribution.getStepExecution().getSkipCount())
                .build();

        repository.save(metadata);

        return RepeatStatus.FINISHED;
    }
}
