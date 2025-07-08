package com.example.demo.service.impl;

import com.example.demo.dto.external.*;
import com.example.demo.service.ExternalApiService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExternalApiServiceImpl implements ExternalApiService {
    private final RestClient restClient;

    @Override
    public FileProcessResponseDto startFileProcessing(String user, String filePath) {
        log.info("Запуск обработки файла {} пользователя {}", filePath, user);
        return restClient
                .post()
                .uri("/process")
                .body(new FileProcessRequestDto(filePath, "structure", user))
                .retrieve()
                .body(FileProcessResponseDto.class);
    }

    @Override
    public TaskStatusResponseDto getTaskStatus(String taskId) {
        log.info("Получение статуса задачи {}", taskId);
        return restClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/status/{task_id}").build(taskId))
                .retrieve()
                .body(TaskStatusResponseDto.class);
    }

    @Override
    public UserFilesResponseDto getUserFiles(String user, String taskId, boolean includeDownloadUrls) {
        log.info("Получение списка файлов пользователя {} (taskId: {}, includeDownloadUrls: {})",
                user, taskId, includeDownloadUrls
        );
        return restClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/storage/files/{user}")
                        .queryParamIfPresent("task_id", Optional.ofNullable(taskId))
                        .queryParam("include_download_urls", includeDownloadUrls)
                        .build(user)
                )
                .retrieve()
                .body(UserFilesResponseDto.class);
    }

    @Override
    public FileContentResponseDto getFileContent(String user, String objectKey) {
        log.info("Получение содержимого файла {} пользователя {}", objectKey, user);
        return restClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/storage/files/{user}/content")
                        .queryParam("object_key", objectKey)
                        .build(user)
                )
                .retrieve()
                .body(FileContentResponseDto.class);
    }

    @Override
    public JsonNode getTaskMetadata(String user, String taskId) {
        log.info("Получение метаданных задачи {} пользователя {}", taskId, user);
        return restClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/storage/tasks/{user}/{task_id}/metadata")
                        .build(user, taskId)
                )
                .retrieve()
                .body(JsonNode.class);
    }

    @Override
    public SearchResponseDto search(String user, String query) {
        log.info("Семантический поиск по запросу {} (пользователь: {})", query, user);
        return restClient
                .post()
                .uri("/rag/query")
                .body(new SearchRequestDto(user, query))
                .retrieve()
                .body(SearchResponseDto.class);
    }

    @Override
    public TaskFilesDeletionResponseDto deleteTaskFiles(String user, String taskId) {
        log.info("Удаление файлов задачи {} пользователя {}", taskId, user);
        return restClient
                .delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/minio/tasks/{user}/{task_id}")
                        .build(user, taskId)
                )
                .retrieve()
                .body(TaskFilesDeletionResponseDto.class);
    }
}
