package com.davinchicoder.loom_etl_uploader.infrastructure.database.mapper;

import com.davinchicoder.loom_etl_uploader.domain.model.JobMetadata;
import com.davinchicoder.loom_etl_uploader.infrastructure.database.entity.JobMetadataEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface JobMetadataMapper {

    JobMetadataEntity toJobMetadataEntity(JobMetadata metadata);

}
