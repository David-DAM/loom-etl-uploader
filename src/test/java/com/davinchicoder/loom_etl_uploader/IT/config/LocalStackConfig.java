package com.davinchicoder.loom_etl_uploader.IT.config;

import io.awspring.cloud.testcontainers.LocalstackAwsClientFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sqs.SqsClient;

@TestConfiguration
public class LocalStackConfig {


    @Bean
    @ServiceConnection
    LocalStackContainer localStackContainer() {
        return new LocalStackContainer(
                DockerImageName.parse("localstack/localstack:4.14.0"))
                .withServices(
                        LocalStackContainer.Service.S3,
                        LocalStackContainer.Service.SQS,
                        LocalStackContainer.Service.DYNAMODB
                );
    }

    @Bean
    LocalstackAwsClientFactory localstackAwsClientFactory() {
        return new LocalstackAwsClientFactory(localStackContainer());
    }

    @Bean
    @Primary
    S3Client s3Client() {
        return localstackAwsClientFactory().create(S3Client.builder());
    }

    @Bean
    @Primary
    SqsClient sqsClient() {
        return localstackAwsClientFactory().create(SqsClient.builder());
    }

    @Bean
    @Primary
    DynamoDbClient dynamoDbClient() {
        return localstackAwsClientFactory().create(DynamoDbClient.builder());
    }


}
