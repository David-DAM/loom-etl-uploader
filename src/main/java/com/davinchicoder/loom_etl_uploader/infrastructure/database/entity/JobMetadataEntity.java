package com.davinchicoder.loom_etl_uploader.infrastructure.database.entity;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@DynamoDbBean
public class JobMetadataEntity {

    private UUID id;
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

    @DynamoDbPartitionKey
    public UUID getId() {
        return id;
    }

}
