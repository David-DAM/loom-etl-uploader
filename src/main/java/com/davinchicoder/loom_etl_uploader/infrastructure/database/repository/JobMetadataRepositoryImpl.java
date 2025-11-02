package com.davinchicoder.loom_etl_uploader.infrastructure.database.repository;

import com.davinchicoder.loom_etl_uploader.domain.model.JobMetadata;
import com.davinchicoder.loom_etl_uploader.domain.port.JobMetadataRepository;
import com.davinchicoder.loom_etl_uploader.infrastructure.database.entity.JobMetadataEntity;
import com.davinchicoder.loom_etl_uploader.infrastructure.database.mapper.JobMetadataMapper;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobMetadataRepositoryImpl implements JobMetadataRepository {

    private final DynamoDbTemplate dynamoDbTemplate;
    private final JobMetadataMapper mapper;

    @Override
    public void save(JobMetadata metadata) {
        JobMetadataEntity jobMetadataEntity = mapper.toJobMetadataEntity(metadata);
        dynamoDbTemplate.save(jobMetadataEntity);
    }
}
