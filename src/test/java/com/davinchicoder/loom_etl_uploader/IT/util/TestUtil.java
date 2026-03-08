package com.davinchicoder.loom_etl_uploader.IT.util;

import com.davinchicoder.loom_etl_uploader.infrastructure.database.entity.JobMetadataEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class TestUtil {

    private final S3Client s3Client;

    private final SqsClient sqsClient;

    private final DynamoDbClient dynamoDbClient;

    private String queueUrl;

    public void prepareTest() {
        log.info("Preparing test environment");
        
        try {
            s3Client.createBucket(b -> b.bucket(TestConstants.ETL_WEATHER_BUCKET_NAME));
        } catch (Exception ignored) {
        }

        try {
            var response = sqsClient.createQueue(q -> q.queueName(TestConstants.LOOM_ETL_QUEUE_NAME));
            queueUrl = response.queueUrl();
        } catch (Exception ignored) {
        }

        DynamoDbEnhancedClient enhanced = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();

        try {
            enhanced.table(
                    TestConstants.JOB_METADATA_TABLE_NAME,
                    TableSchema.fromBean(JobMetadataEntity.class)
            ).createTable();

            DynamoDbWaiter waiter = dynamoDbClient.waiter();
            waiter.waitUntilTableExists(
                    DescribeTableRequest.builder()
                            .tableName(TestConstants.JOB_METADATA_TABLE_NAME)
                            .build()
            );
        } catch (ResourceInUseException e) {
            log.info("Table already exists: {}", TestConstants.JOB_METADATA_TABLE_NAME);
        }
        log.info("Test environment setup complete");
    }

    public void cleanUpTest() {

        log.info("Cleaning up test environment");

        s3Client.listBuckets().buckets().forEach(bucket ->
                s3Client.listObjectsV2(b -> b.bucket(bucket.name())).contents()
                        .forEach(o -> s3Client.deleteObject(d -> d.bucket(bucket.name()).key(o.key())))
        );

        while (true) {
            var messages = sqsClient.receiveMessage(r -> r.queueUrl(queueUrl).maxNumberOfMessages(10));
            if (messages.messages().isEmpty()) break;

            messages.messages().forEach(m ->
                    sqsClient.deleteMessage(d -> d.queueUrl(queueUrl).receiptHandle(m.receiptHandle()))
            );
        }

        try {
            var scan = dynamoDbClient.scan(ScanRequest.builder()
                    .tableName(TestConstants.JOB_METADATA_TABLE_NAME)
                    .build());

            scan.items().forEach(item -> {
                dynamoDbClient.deleteItem(DeleteItemRequest.builder()
                        .tableName(TestConstants.JOB_METADATA_TABLE_NAME)
                        .key(Map.of("id", item.get("id")))
                        .build());
            });
        } catch (ResourceNotFoundException e) {
            log.warn("DynamoDB table not found, skipping cleanup: {}", e.getMessage());
        }
        log.info("Test environment cleaned up");
    }

}
