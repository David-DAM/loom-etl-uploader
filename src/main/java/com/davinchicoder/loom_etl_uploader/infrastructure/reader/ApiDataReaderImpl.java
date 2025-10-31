package com.davinchicoder.loom_etl_uploader.infrastructure.reader;

import com.davinchicoder.loom_etl_uploader.infrastructure.dto.WeatherForecastResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class ApiDataReaderImpl implements ItemReader<WeatherForecastResponseDTO> {

    private final RestTemplate restTemplate;

    @Override
    public WeatherForecastResponseDTO read() throws UnexpectedInputException, ParseException, NonTransientResourceException {

        return restTemplate.getForEntity("/forecast?latitude=40.4168&longitude=-3.7038&hourly=temperature_2m", WeatherForecastResponseDTO.class).getBody();
    }
}
