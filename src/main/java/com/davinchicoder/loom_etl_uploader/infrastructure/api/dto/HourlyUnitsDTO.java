package com.davinchicoder.loom_etl_uploader.infrastructure.api.dto;

import lombok.Data;

@Data
public class HourlyUnitsDTO {

    private String time;
    private String temperature_2m;

}
