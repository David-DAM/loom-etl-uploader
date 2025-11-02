package com.davinchicoder.loom_etl_uploader.infrastructure.batch.reader;

import com.davinchicoder.loom_etl_uploader.domain.model.DailyWeatherSummary;
import com.davinchicoder.loom_etl_uploader.domain.model.WeatherForecast;
import com.davinchicoder.loom_etl_uploader.infrastructure.api.client.WeatherClientImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
@RequiredArgsConstructor
public class WeatherReader implements ItemReader<DailyWeatherSummary> {

    private final WeatherClientImpl weatherClient;
    private Iterator<DailyWeatherSummary> iterator;

    @Override
    public DailyWeatherSummary read() throws UnexpectedInputException, ParseException, NonTransientResourceException {

        if (iterator == null) {
            WeatherForecast forecast = weatherClient.getForecast();
            List<DailyWeatherSummary> summaries = getDailyWeatherSummaries(forecast);
            iterator = summaries.iterator();
        }

        return iterator.hasNext() ? iterator.next() : null;
    }

    private List<DailyWeatherSummary> getDailyWeatherSummaries(WeatherForecast forecast) {
        Map<LocalDate, List<Double>> tempsByDate = new HashMap<>();

        for (int i = 0; i < forecast.getHourly().getTime().size(); i++) {
            LocalDate date = LocalDate.parse(forecast.getHourly().getTime().get(i).substring(0, 10));
            tempsByDate.computeIfAbsent(date, _ -> new ArrayList<>()).add(forecast.getHourly().getTemperature_2m().get(i));
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
                            .latitude(forecast.getLatitude())
                            .longitude(forecast.getLongitude())
                            .build();
                })
                .toList();
    }
}
