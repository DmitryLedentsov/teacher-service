package com.example.demo.service;

import com.example.demo.dto.external.*;
import com.fasterxml.jackson.databind.JsonNode;

public interface ExternalApiService {
    FileProcessResponseDto startFileProcessing(String user, String filePath);

    TaskStatusResponseDto getTaskStatus(String taskId);

    UserFilesResponseDto getUserFiles(String user, String taskId, boolean includeDownloadUrls);

    FileContentResponseDto getFileContent(String user, String objectKey);

    JsonNode getTaskMetadata(String user, String taskId);

    SearchResponseDto search(String user, String query);

    TaskFilesDeletionResponseDto deleteTaskFiles(String user, String taskId);
}
