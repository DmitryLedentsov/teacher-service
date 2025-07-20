package com.example.demo.service;

import com.example.demo.dto.FileDto;
import com.example.demo.util.FileContainer;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    FileDto uploadFile(String username, Long subjectId, MultipartFile file);

    FileContainer downloadFile(String username, Long subjectId, String filename);

    List<FileDto> getAllForUserSubject(String username, Long subjectId);
}
