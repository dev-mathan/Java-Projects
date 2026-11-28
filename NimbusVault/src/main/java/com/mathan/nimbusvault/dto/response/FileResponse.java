
package com.mathan.nimbusvault.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record FileResponse(UUID id,String fileName,String contentType,Long size,LocalDateTime createdAt) {
}
