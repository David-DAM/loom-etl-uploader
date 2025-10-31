package com.davinchicoder.loom_etl_uploader.infrastructure.config;

import com.davinchicoder.loom_etl_uploader.domain.DailyWeatherSummary;
import com.davinchicoder.loom_etl_uploader.infrastructure.dto.WeatherForecastResponseDTO;
import com.davinchicoder.loom_etl_uploader.infrastructure.processor.WeatherProcessorImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.function.SupplierItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.WritableResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class Step1Config {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final WritableResource writableResource;
    private final RestTemplate restTemplate;
    private final WeatherProcessorImpl processorStep1;
//    private final S3WriterImpl writerStep1;
//    private final ApiDataReaderImpl readerStep1;


    @Bean
    public SupplierItemReader<WeatherForecastResponseDTO> readerStep1() {
        return new SupplierItemReader<>(() ->
                restTemplate.getForObject(
                        "/forecast?latitude=40.4168&longitude=-3.7038&hourly=temperature_2m",
                        WeatherForecastResponseDTO.class
                )
        );
    }

    @Bean
    public FlatFileItemWriter<DailyWeatherSummary> writerStep1() {

        FlatFileItemWriter<DailyWeatherSummary> writer = new FlatFileItemWriter<>();
        writer.setEncoding("UTF-8");
        writer.setResource(writableResource);
        writer.setShouldDeleteIfExists(true);
        writer.setHeaderCallback(writer1 -> writer1.write("date,avgTemperature,minTemperature,maxTemperature,latitude,longitude"));

        BeanWrapperFieldExtractor<DailyWeatherSummary> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"date", "avgTemperature", "minTemperature", "maxTemperature", "latitude", "longitude"});

        DelimitedLineAggregator<DailyWeatherSummary> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor);

        writer.setLineAggregator(lineAggregator);

        return writer;
    }

    @Bean
    public Step step1() {
        return new StepBuilder("uploadWeatherSummarize", jobRepository)
                .<WeatherForecastResponseDTO, DailyWeatherSummary>chunk(10, platformTransactionManager)
                .reader(readerStep1())
                .processor(processorStep1)
                .writer(writerStep1())
                .build();
    }

}
