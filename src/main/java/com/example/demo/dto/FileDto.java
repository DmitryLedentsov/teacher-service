package com.example.demo.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class FileDto {
    private Long id;
    private String name;
    private long size;
    private String link;
    private Instant uploadedAt;
}
