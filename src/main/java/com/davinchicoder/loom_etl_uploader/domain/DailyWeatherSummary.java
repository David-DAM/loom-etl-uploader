package com.davinchicoder.loom_etl_uploader.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DailyWeatherSummary {
    private String date;
    private double avgTemperature;
    private double minTemperature;
    private double maxTemperature;
    private double latitude;
    private double longitude;
}
