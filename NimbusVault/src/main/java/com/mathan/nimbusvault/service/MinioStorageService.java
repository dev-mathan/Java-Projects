
package com.mathan.nimbusvault.service;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mathan.nimbusvault.exception.StorageException;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;

@Service
public class MinioStorageService implements StorageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public MinioStorageService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public String uploadFile(
            InputStream inputStream,
            long size,
            String contentType,
            String objectName) {

        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, size, -1)
                            .contentType(contentType)
                            .build()
            );

            return objectName;

        } catch (Exception e) {
            throw new StorageException(
                    "Failed to upload file to MinIO", e);
        }
    }

    @Override
    public InputStream downloadFile(String objectName) {

        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );

        } catch (Exception e) {
            throw new StorageException(
                    "Failed to download file from MinIO", e);
        }
    }

    @Override
    public void deleteFile(String objectName) {

        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );

        } catch (Exception e) {
            throw new StorageException(
                    "Failed to delete file from MinIO", e);
        }
    }
}
