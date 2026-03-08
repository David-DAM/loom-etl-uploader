package com.davinchicoder.loom_etl_uploader.IT;

import com.davinchicoder.loom_etl_uploader.IT.config.LocalStackConfig;
import com.davinchicoder.loom_etl_uploader.IT.util.TestConstants;
import com.davinchicoder.loom_etl_uploader.IT.util.TestUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@SpringBatchTest
@Import({LocalStackConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@Slf4j
@AutoConfigureStubRunner(
        ids = "com.davinchicoder:loom_etl_uploader:+:stubs:8082",
        stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
public class JobIT {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private S3Client s3Client;

    @Autowired
    private SqsClient sqsClient;

    @Autowired
    private DynamoDbClient dynamoDbClient;

    @Autowired
    private TestUtil testUtil;

    @BeforeAll
    void beforeAll() {
        testUtil.prepareTest();
    }

    @BeforeEach
    void setUp() {
        testUtil.cleanUpTest();
    }

    @Test
    void testBatchJob() throws Exception {
        log.info("Starting test batch job");
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());

        ListObjectsV2Response response = s3Client.listObjectsV2(b -> b.bucket(TestConstants.ETL_WEATHER_BUCKET_NAME));
        assertFalse(response.contents().isEmpty());

        boolean isJobMetadataEmpty = dynamoDbClient.scanPaginator(
                builder -> builder.tableName(TestConstants.JOB_METADATA_TABLE_NAME)
        ).items().stream().toList().isEmpty();

        assertFalse(isJobMetadataEmpty);

        List<Message> messages = sqsClient.receiveMessage(
                r -> r.queueUrl(sqsClient.getQueueUrl(q -> q.queueName(TestConstants.LOOM_ETL_QUEUE_NAME)).queueUrl())
        ).messages();

        assertFalse(messages.isEmpty());

        log.info("Test batch job completed");
    }
}
