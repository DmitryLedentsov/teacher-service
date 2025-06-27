package com.example.demo.controller;

import com.example.demo.dto.FileDto;
import com.example.demo.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.security.Principal;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping
    public FileDto save(Principal principal, @RequestParam MultipartFile file) {
        var fileDto = fileService.save(principal.getName(), file);
        fileDto.setLink(getFileUrl(fileDto.getLink()));
        return fileDto;
    }

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> load(Principal principal, @PathVariable String filename) {
        var file = fileService.load(principal.getName(), filename);
        return ResponseEntity.ok()
                .contentType(file.contentType())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"%s\"".formatted(filename))
                .body(file.resource());
    }

    private String getFileUrl(String filename) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/files/")
                .path(filename)
                .toUriString();
    }
}
