package com.example.demo.dto.external;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TaskResult(
        int notesSaved,
        List<String> noteFiles,
        String minioPath,
        String message
) {
}
