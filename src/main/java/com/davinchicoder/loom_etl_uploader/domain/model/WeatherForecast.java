package com.davinchicoder.loom_etl_uploader.domain.model;

import lombok.Data;

@Data
public class WeatherForecast {

    private double latitude;
    private double longitude;
    private double generationtime_ms;
    private int utc_offset_seconds;
    private String timezone;
    private String timezone_abbreviation;
    private double elevation;
    private HourlyUnits hourly_units;
    private HourlyData hourly;

}
