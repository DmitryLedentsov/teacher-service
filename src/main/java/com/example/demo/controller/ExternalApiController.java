package com.example.demo.controller;

import com.example.demo.dto.external.*;
import com.example.demo.service.ExternalApiService;
import com.example.demo.service.FileService;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class ExternalApiController {
    private final ExternalApiService externalApiService;
    private final FileService fileService;
    private final UserService userService;

    @PostMapping("/files")
    public FileProcessResponseDto saveFileAndStartProcessing(Principal principal, @RequestParam MultipartFile file) {
        var fileDto = fileService.save(principal.getName(), file);
        return externalApiService.startFileProcessing(fileDto.getUserId().toString(), fileDto.getLink());
    }

    @GetMapping("/tasks/{taskId}/status")
    public TaskStatusResponseDto getTaskStatus(@PathVariable String taskId) {
        return externalApiService.getTaskStatus(taskId);
    }

    // TODO вынести получение пользователя в JWT фильтр, а ID в Principal?
    @GetMapping("/files")
    public UserFilesResponseDto getUserFiles(
            Principal principal,
            @RequestParam(required = false) String taskId,
            @RequestParam(required = false, defaultValue = "true") boolean includeDownloadUrls
    ) {
        var userId = userService.getIdByUsername(principal.getName());
        return externalApiService.getUserFiles(userId.toString(), taskId, includeDownloadUrls);
    }

    // TODO RequestParam vs PathVariable?
    // TODO вынести получение пользователя в JWT фильтр, а ID в Principal?
    @GetMapping("/files/content")
    public FileContentResponseDto getFileContent(Principal principal, @RequestParam String objectKey) {
        var userId = userService.getIdByUsername(principal.getName());
        return externalApiService.getFileContent(userId.toString(), objectKey);
    }

    // TODO вынести получение пользователя в JWT фильтр, а ID в Principal?
    @GetMapping("/tasks/{taskId}/metadata")
    public JsonNode getTaskMetadata(Principal principal, @PathVariable String taskId) {
        var userId = userService.getIdByUsername(principal.getName());
        return externalApiService.getTaskMetadata(userId.toString(), taskId);
    }

    // TODO переделать под POST?
    // TODO вынести получение пользователя в JWT фильтр, а ID в Principal?
    @GetMapping("/search")
    public SearchResponseDto search(Principal principal, @RequestParam String query) {
        var userId = userService.getIdByUsername(principal.getName());
        return externalApiService.search(userId.toString(), query);
    }

    // TODO вынести получение пользователя в JWT фильтр, а ID в Principal?
    @DeleteMapping("/tasks/{taskId}")
    public TaskFilesDeletionResponseDto deleteTaskFiles(Principal principal, @PathVariable String taskId) {
        var userId = userService.getIdByUsername(principal.getName());
        return externalApiService.deleteTaskFiles(userId.toString(), taskId);
    }
}
