
package com.mathan.nimbusvault.service;

import java.io.InputStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.mathan.nimbusvault.dto.response.FileResponse;
import com.mathan.nimbusvault.entity.FileEntity;
import com.mathan.nimbusvault.entity.User;
import com.mathan.nimbusvault.exception.StorageException;
import com.mathan.nimbusvault.mapper.FileMapper;
import com.mathan.nimbusvault.repository.FileRepository;
import com.mathan.nimbusvault.repository.UserRepository;

@Service
public class FileService {

    private final StorageService storageService;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final FileMapper fileMapper;

    public FileService(
            StorageService storageService,
            FileRepository fileRepository,
            UserRepository userRepository,
            FileMapper fileMapper) {

        this.storageService = storageService;
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
        this.fileMapper = fileMapper;
    }

    @Transactional
    public FileResponse uploadFile(
            MultipartFile file,
            UUID userId) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        long fileSize = file.getSize();

        if (fileSize > user.getStorageLimit() - user.getStorageUsed()) {
            throw new IllegalArgumentException("Storage quota exceeded");
        }

        String originalFileName = file.getOriginalFilename();

        if (originalFileName == null || originalFileName.isBlank()) {
            throw new IllegalArgumentException("Invalid file name");
        }

        String objectName = UUID.randomUUID().toString();

        String contentType = file.getContentType();

        if (contentType == null || contentType.isBlank()) {
            contentType = "application/octet-stream";
        }

        try (InputStream inputStream = file.getInputStream()) {

            storageService.uploadFile(
                    inputStream,
                    fileSize,
                    contentType,
                    objectName);

        } catch (Exception e) {
            throw new StorageException("File upload failed", e);
        }

        FileEntity fileEntity = new FileEntity();

        fileEntity.setFileName(originalFileName);
        fileEntity.setObjectName(objectName);
        fileEntity.setContentType(contentType);
        fileEntity.setSize(fileSize);
        fileEntity.setUser(user);

        FileEntity savedFile = fileRepository.save(fileEntity);

        user.setStorageUsed(user.getStorageUsed() + fileSize);

        userRepository.save(user);

        return fileMapper.toResponse(savedFile);
    }
    @Transactional(readOnly = true)
    public Page<FileResponse> getUserFiles(
            UUID userId,
            Pageable pageable) {

        Page<FileEntity> files = fileRepository.findByUser_Id(
                userId, pageable);

        return files.map(fileMapper::toResponse);
    }

@Transactional(readOnly = true)
public FileResponse getFileDetails(UUID fileId, UUID userId) {

    FileEntity file = fileRepository.findByIdAndUser_Id(fileId, userId)
            .orElseThrow(() ->
                    new IllegalArgumentException("File not found"));

    return fileMapper.toResponse(file);
}

}
