package com.example.demo.controller;

import com.example.demo.dto.external.TaskStatusResponseDto;
import com.example.demo.service.ExternalApiService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
@SecurityRequirement(name = "bearer-token")
@RequiredArgsConstructor
public class TaskController {
    private final ExternalApiService externalApiService;

    @GetMapping("/{id}/status")
    public TaskStatusResponseDto getTaskStatus(@PathVariable("id") String taskId) {
        return externalApiService.getTaskStatus(taskId);
    }
}
