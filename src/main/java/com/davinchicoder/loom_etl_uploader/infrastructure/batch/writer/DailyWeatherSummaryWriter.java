package com.davinchicoder.loom_etl_uploader.infrastructure.batch.writer;

import com.davinchicoder.loom_etl_uploader.domain.model.DailyWeatherSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Component
public class DailyWeatherSummaryWriter implements ItemStreamWriter<DailyWeatherSummary> {

    private final WritableResource writableResource;
    private final DelimitedLineAggregator<DailyWeatherSummary> lineAggregator;
    private OutputStream outputStream;

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        try {
            this.outputStream = writableResource.getOutputStream();

            outputStream.write("date,avgTemperature,minTemperature,maxTemperature,latitude,longitude\n".getBytes(StandardCharsets.UTF_8));

        } catch (IOException e) {
            throw new ItemStreamException("Cannot open S3 output stream", e);
        }
    }

    @Override
    public void write(Chunk<? extends DailyWeatherSummary> chunk) throws Exception {
        for (DailyWeatherSummary item : chunk) {
            String line = lineAggregator.aggregate(item);
            outputStream.write((line + "\n").getBytes(StandardCharsets.UTF_8));
        }
        outputStream.flush();
    }

    @Override
    public void close() throws ItemStreamException {
        try {
            if (outputStream != null) outputStream.close();
        } catch (IOException e) {
            throw new ItemStreamException("Cannot close S3 output stream", e);
        }
    }
}
