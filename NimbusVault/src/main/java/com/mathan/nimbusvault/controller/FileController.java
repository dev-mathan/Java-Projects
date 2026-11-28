
package com.mathan.nimbusvault.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.mathan.nimbusvault.dto.response.FileResponse;
import com.mathan.nimbusvault.entity.User;
import com.mathan.nimbusvault.repository.UserRepository;
import com.mathan.nimbusvault.service.FileService;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    private final FileService fileService;
    private final UserRepository userRepository;

    public FileController(
            FileService fileService,
            UserRepository userRepository) {

        this.fileService = fileService;
        this.userRepository = userRepository;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public FileResponse uploadFile(
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElseThrow(() ->new IllegalArgumentException(
        		"Authenticated user not found"));

        UUID userId = user.getId();

        return fileService.uploadFile(file, userId);
    }
    @GetMapping
    public Page<FileResponse> getUserFiles(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Authenticated user not found"));

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        return fileService.getUserFiles(user.getId(), pageable);
    }

@GetMapping("/{fileId}")
public FileResponse getFileDetails(@PathVariable UUID fileId,Authentication authentication) {

    String email = authentication.getName();

    User user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new IllegalArgumentException("Authenticated user not found"));

    return fileService.getFileDetails(fileId, user.getId());
}

}