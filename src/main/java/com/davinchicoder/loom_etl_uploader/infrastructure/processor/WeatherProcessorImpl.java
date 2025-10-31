package com.davinchicoder.loom_etl_uploader.infrastructure.processor;

import com.davinchicoder.loom_etl_uploader.domain.DailyWeatherSummary;
import com.davinchicoder.loom_etl_uploader.infrastructure.dto.WeatherForecastResponseDTO;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
public class WeatherProcessorImpl implements ItemProcessor<WeatherForecastResponseDTO, DailyWeatherSummary> {

    private Iterator<DailyWeatherSummary> iterator;

    @Override
    public DailyWeatherSummary process(WeatherForecastResponseDTO dto) throws Exception {
      
        if (iterator == null) {
            iterator = getDailyWeatherSummaries(dto).iterator();
        }

        if (iterator.hasNext()) {
            return iterator.next();
        }

        return null;
    }

    private List<DailyWeatherSummary> getDailyWeatherSummaries(WeatherForecastResponseDTO dto) {
        Map<LocalDate, List<Double>> tempsByDate = new HashMap<>();

        for (int i = 0; i < dto.getHourly().getTime().size(); i++) {
            LocalDate date = LocalDate.parse(dto.getHourly().getTime().get(i).substring(0, 10));
            tempsByDate.computeIfAbsent(date, _ -> new ArrayList<>())
                    .add(dto.getHourly().getTemperature_2m().get(i));
        }

        return tempsByDate.entrySet().stream()
                .map(entry -> {
                    List<Double> temps = entry.getValue();
                    double avg = temps.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                    double min = temps.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
                    double max = temps.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);

                    return DailyWeatherSummary.builder()
                            .date(entry.getKey().toString())
                            .avgTemperature(avg)
                            .minTemperature(min)
                            .maxTemperature(max)
                            .latitude(dto.getLatitude())
                            .longitude(dto.getLongitude())
                            .build();
                })
                .toList();
    }
}
