package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.Instant;

@Data
public class FileDto {
    private Long id;
    private String name;
    private long size;
    private Instant uploadedAt;
    private String url;

    // TODO сделать получше? эти поля нужны для стороннего сервиса
    @JsonIgnore
    private String folderPath;
    @JsonIgnore
    private Long userId;
    @JsonIgnore
    private SubjectDto subject;
}
