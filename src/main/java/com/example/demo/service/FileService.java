package com.example.demo.service;

import com.example.demo.dto.FileDto;
import com.example.demo.util.FileContainer;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    FileDto save(String username, MultipartFile file);

    FileContainer load(String username, String filename);
}
