package com.mathan.nimbusvault.service;

import java.io.InputStream;

public interface StorageService {

    String uploadFile(InputStream inputStream, long size,
                      String contentType, String objectName);

    InputStream downloadFile(String objectName);

    void deleteFile(String objectName);
}