package com.example.demo.service;

import com.example.demo.dto.external.ProcessResponseDto;
import com.example.demo.dto.external.SearchResponseDto;
import com.example.demo.dto.external.TaskStatusResponseDto;

public interface ExternalApiService {
    ProcessResponseDto startProcessing(String user, String subject, String folderPath);

    TaskStatusResponseDto getTaskStatus(String taskId);

    SearchResponseDto search(String user, String subject, String query);
}
