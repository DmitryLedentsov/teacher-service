package com.example.demo.dto;

import com.example.demo.util.ValidationUtils;
import jakarta.validation.constraints.NotBlank;

// TODO проверять на символы
public record AuthDto(
        @NotBlank(message = ValidationUtils.NOT_BLANK)
        String username,

        @NotBlank(message = ValidationUtils.NOT_BLANK)
        String password
) {
}
