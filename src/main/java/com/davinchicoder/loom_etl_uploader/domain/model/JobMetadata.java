package com.davinchicoder.loom_etl_uploader.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class JobMetadata {
    private String id;
    private String jobId;
    private String jobName;
    private String stepName;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long duration;
    private long itemsRead;
    private long itemsProcessed;
    private long itemsWritten;
    private long itemsFailed;
    private String errorMessage;
}
