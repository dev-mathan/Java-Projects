
package com.mathan.nimbusvault.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;

@Configuration
public class MinioBucketConfig {

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Bean
    public CommandLineRunner createBucket(MinioClient minioClient) {

        return args -> {

            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(bucketName)
                            .build());

            if (!exists) {

                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(bucketName)
                                .build());

                System.out.println("MinIO bucket created: " + bucketName);

            } else {

                System.out.println("MinIO bucket already exists: " + bucketName);
            }
        };
    }
}
