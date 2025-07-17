package com.example.demo.service.impl;

import com.example.demo.dto.external.ProcessRequestDto;
import com.example.demo.dto.external.ProcessResponseDto;
import com.example.demo.dto.external.SearchRequestDto;
import com.example.demo.dto.external.SearchResponseDto;
import com.example.demo.dto.external.TaskStatusResponseDto;
import com.example.demo.service.ExternalApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExternalApiServiceImpl implements ExternalApiService {
    private final RestClient restClient;

    @Override
    public ProcessResponseDto startProcessing(String user, String subject, String folderPath) {
        log.info("Запуск обработки папки {} пользователя {}", folderPath, user);
        return restClient
                .post()
                .uri("/process-folder")
                .body(new ProcessRequestDto(user, subject, folderPath))
                .retrieve()
                .body(ProcessResponseDto.class);
    }

    @Override
    public TaskStatusResponseDto getTaskStatus(String taskId) {
        log.info("Получение статуса задачи {}", taskId);
        return restClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/task-status/{task_id}").build(taskId))
                .retrieve()
                .body(TaskStatusResponseDto.class);
    }

    @Override
    public SearchResponseDto search(String user, String subject, String query) {
        log.info("Поиск по материалам по запросу {} (пользователь: {})", query, user);
        return restClient
                .post()
                .uri("/search")
                .body(new SearchRequestDto(user, subject, query))
                .retrieve()
                .body(SearchResponseDto.class);
    }

    /*
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
     */
}
