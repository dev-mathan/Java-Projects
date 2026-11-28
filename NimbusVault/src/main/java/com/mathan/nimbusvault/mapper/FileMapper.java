package com.mathan.nimbusvault.mapper;

import org.springframework.stereotype.Component;

import com.mathan.nimbusvault.dto.response.FileResponse;
import com.mathan.nimbusvault.entity.FileEntity;

@Component
public class FileMapper {

    public FileResponse toResponse(FileEntity file) {

        return new FileResponse(
                file.getId(),
                file.getFileName(),
                file.getContentType(),
                file.getSize(),
                file.getCreatedAt()
        );
    }
}
