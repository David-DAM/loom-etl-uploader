package com.davinchicoder.loom_etl_uploader.infrastructure.api.client;

import com.davinchicoder.loom_etl_uploader.domain.model.WeatherForecast;
import com.davinchicoder.loom_etl_uploader.domain.port.WeatherClientPort;
import com.davinchicoder.loom_etl_uploader.infrastructure.api.dto.WeatherForecastResponseDTO;
import com.davinchicoder.loom_etl_uploader.infrastructure.batch.mapper.WeatherMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class WeatherClientImpl implements WeatherClientPort {

    private final RestTemplate restTemplate;
    private final WeatherMapper weatherMapper;

    @Override
    public WeatherForecast getForecast() {
        var response = restTemplate.getForEntity("/forecast?latitude=40.4168&longitude=-3.7038&hourly=temperature_2m", WeatherForecastResponseDTO.class);

        if (response.getStatusCode().isError()) {
            throw new RuntimeException("Unable to get weather forecast");
        }

        return weatherMapper.toWeatherForecast(response.getBody());
    }
}
