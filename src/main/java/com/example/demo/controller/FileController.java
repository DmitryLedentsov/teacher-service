package com.example.demo.controller;

import com.example.demo.dto.FileDto;
import com.example.demo.dto.external.ProcessResponseDto;
import com.example.demo.service.ExternalApiService;
import com.example.demo.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/subject")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;
    private final ExternalApiService externalApiService;

    @PostMapping("/{id}/files")
    public ProcessResponseDto uploadFileAndStartProcessing(
            Principal principal,
            @PathVariable("id") Long subjectId,
            @RequestParam("file") MultipartFile uploadFile
    ) {
        var file = fileService.uploadFile(principal.getName(), subjectId, uploadFile);
        return externalApiService.startProcessing(
                file.getUserId().toString(),
                file.getSubject().name(),
                file.getFolderPath()
        );
    }

    @GetMapping("/{id}/files")
    public List<FileDto> getAllFilesForSubject(
            Principal principal,
            @PathVariable("id") Long id
    ) {
        var files = fileService.getAllForUserSubject(principal.getName(), id);
        files.forEach(file -> file.setUrl(createFileURL(file.getSubject().id(), file.getName())));
        return files;
    }

    @GetMapping("/{id}/files/{name}")
    public ResponseEntity<Resource> downloadFile(
            Principal principal,
            @PathVariable("id") Long subjectId,
            @PathVariable("name") String filename
    ) {
        var file = fileService.downloadFile(principal.getName(), subjectId, filename);
        return ResponseEntity.ok()
                .contentType(file.contentType())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"%s\"".formatted(filename))
                .body(file.resource());
    }

    // TODO вынести в другое место?
    private String createFileURL(Long subjectId, String filename) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/subject/")
                .path(subjectId.toString())
                .path("/files/")
                .path(filename)
                .toUriString();
    }
}
