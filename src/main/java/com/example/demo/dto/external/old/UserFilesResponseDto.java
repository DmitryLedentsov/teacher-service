package com.example.demo.dto.external.old;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserFilesResponseDto(
        String user,
        String taskId,
        List<UserFileDto> files,
        int total
) {
}
