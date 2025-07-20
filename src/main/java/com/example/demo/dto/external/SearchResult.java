package com.example.demo.dto.external;

public record SearchResult(
        String source,
        String section,
        String content,
        double score
) {
}
