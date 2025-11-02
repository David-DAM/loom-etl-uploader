package com.davinchicoder.loom_etl_uploader.infrastructure.event;

import com.davinchicoder.loom_etl_uploader.domain.port.WeatherEventPort;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WeatherEventProducer implements WeatherEventPort {

    private final SqsTemplate sqsTemplate;
    @Value("${app.sqs.weather-summary-topic}")
    private String TOPIC;

    @Override
    public void weatherSummaryCompleted() {

        WeatherSummaryCompleted event = WeatherSummaryCompleted.builder()
                .id(UUID.randomUUID().toString())
                .timestamp(Instant.now().toEpochMilli())
                .build();

        Message<WeatherSummaryCompleted> message = MessageBuilder.withPayload(event).build();

        sqsTemplate.send(TOPIC, message);
    }
}
