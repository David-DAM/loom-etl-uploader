package com.davinchicoder.loom_etl_uploader.infrastructure.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeatherSummaryCompleted {

    private String id;
    private Long timestamp;

}
