package com.prgrms.p2p.common.localStack;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class LocalStackS3Config {
  DockerImageName localstackImage = DockerImageName.parse("localstack/localstack");

  @Bean(initMethod = "start", destroyMethod = "stop")
  public LocalStackContainer localStackContainer() {
    return new LocalStackContainer(localstackImage)
        .withServices(S3);
  }

  @Bean
  public AmazonS3 amazonS3(LocalStackContainer localStackContainer) {
    return AmazonS3ClientBuilder.standard()
        .withEndpointConfiguration(localStackContainer.getEndpointConfiguration(S3))
        .withCredentials(localStackContainer.getDefaultCredentialsProvider())
        .build();
  }
}