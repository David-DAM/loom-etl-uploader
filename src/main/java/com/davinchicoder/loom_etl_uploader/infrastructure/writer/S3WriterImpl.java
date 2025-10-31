package com.davinchicoder.loom_etl_uploader.infrastructure.writer;

import com.davinchicoder.loom_etl_uploader.domain.DailyWeatherSummary;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@RequiredArgsConstructor
@Component
public class S3WriterImpl implements ItemStreamWriter<DailyWeatherSummary> {

    private final WritableResource writableResource;
    private final ObjectMapper objectMapper;

    private OutputStream outputStream;

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        try {
            this.outputStream = writableResource.getOutputStream();
        } catch (IOException e) {
            throw new ItemStreamException("Unable to get output stream", e);
        }
    }

    @Override
    public void write(Chunk<? extends DailyWeatherSummary> item) throws Exception {

        outputStream.write(objectMapper.writeValueAsBytes(item));

    }

    @Override
    public void close() throws ItemStreamException {
        try {
            outputStream.close();
        } catch (IOException e) {
            throw new ItemStreamException("Unable to close output stream", e);
        }
    }
}
