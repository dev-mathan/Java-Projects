
package com.mathan.nimbusvault.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mathan.nimbusvault.entity.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity, UUID> {

    Page<FileEntity> findByUser_Id(UUID userId, Pageable pageable);

    Optional<FileEntity> findByIdAndUser_Id(UUID fileId, UUID userId);

    Page<FileEntity> findByUser_IdAndFileNameContainingIgnoreCase(
            UUID userId,
            String fileName,
            Pageable pageable
    );
}
