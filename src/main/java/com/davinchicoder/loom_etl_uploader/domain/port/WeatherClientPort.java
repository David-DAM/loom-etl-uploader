package com.davinchicoder.loom_etl_uploader.domain.port;

import com.davinchicoder.loom_etl_uploader.domain.model.WeatherForecast;

public interface WeatherClientPort {
    WeatherForecast getForecast();
}
