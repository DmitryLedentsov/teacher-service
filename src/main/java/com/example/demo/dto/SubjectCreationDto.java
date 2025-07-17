package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record SubjectCreationDto(
        @NotBlank
        String name
) {
}
