package com.davinchicoder.loom_etl_uploader.infrastructure.api.dto;

import lombok.Data;

@Data
public class WeatherForecastResponseDTO {

    private double latitude;
    private double longitude;
    private double generationtime_ms;
    private int utc_offset_seconds;
    private String timezone;
    private String timezone_abbreviation;
    private double elevation;
    private HourlyUnitsDTO hourly_units;
    private HourlyDataDTO hourly;

}
