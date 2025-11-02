package com.davinchicoder.loom_etl_uploader.infrastructure.batch.mapper;

import com.davinchicoder.loom_etl_uploader.domain.model.WeatherForecast;
import com.davinchicoder.loom_etl_uploader.infrastructure.api.dto.WeatherForecastResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface WeatherMapper {

    WeatherForecast toWeatherForecast(WeatherForecastResponseDTO dto);

}
