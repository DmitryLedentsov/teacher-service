package com.example.demo.dto;

import com.example.demo.util.ValidationUtils;
import jakarta.validation.constraints.NotBlank;

public record SubjectCreationDto(
        @NotBlank(message = ValidationUtils.NOT_BLANK)
        String name
) {
}
