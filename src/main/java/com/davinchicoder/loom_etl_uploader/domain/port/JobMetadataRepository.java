package com.davinchicoder.loom_etl_uploader.domain.port;

import com.davinchicoder.loom_etl_uploader.domain.model.JobMetadata;

public interface JobMetadataRepository {
    void save(JobMetadata metadata);
}
