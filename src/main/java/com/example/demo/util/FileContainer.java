package com.example.demo.util;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

public record FileContainer(
        Resource resource,
        MediaType contentType
) {
}
