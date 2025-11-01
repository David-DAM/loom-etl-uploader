package com.davinchicoder.loom_etl_uploader.domain.model;

import lombok.Data;

import java.util.List;

@Data
public class HourlyData {

    private List<String> time;
    private List<Double> temperature_2m;
}
