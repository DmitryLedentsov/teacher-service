package com.example.demo.service;

import com.example.demo.dto.external.ProcessResponseDto;
import com.example.demo.dto.external.SearchResponseDto;
import com.example.demo.dto.external.TaskStatusResponseDto;

public interface ExternalApiService {
    ProcessResponseDto startProcessing(String user, String subject, String folderPath);

    TaskStatusResponseDto getTaskStatus(String taskId);

    SearchResponseDto search(String user, String subject, String query);

    /*
    UserFilesResponseDto getUserFiles(String user, String taskId, boolean includeDownloadUrls);

    FileContentResponseDto getFileContent(String user, String objectKey);

    JsonNode getTaskMetadata(String user, String taskId);

    TaskFilesDeletionResponseDto deleteTaskFiles(String user, String taskId);
     */
}
