package com.davinchicoder.loom_etl_uploader.infrastructure.dto;

import lombok.Data;

import java.util.List;

@Data
public class HourlyDataDTO {

    private List<String> time;
    private List<Double> temperature_2m;
}
