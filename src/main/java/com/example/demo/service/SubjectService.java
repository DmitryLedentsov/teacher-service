package com.example.demo.service;

import com.example.demo.dto.SubjectCreationDto;
import com.example.demo.dto.SubjectDto;

import java.util.List;

public interface SubjectService {
    SubjectDto create(String username, SubjectCreationDto subjectCreationDto);

    List<SubjectDto> getAll(String username);

    SubjectDto getById(String username, Long id);
}
