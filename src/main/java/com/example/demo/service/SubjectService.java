package com.example.demo.service;

import com.example.demo.dto.SubjectCreationDto;
import com.example.demo.dto.SubjectDto;
import com.example.demo.entity.Subject;

import java.util.List;

public interface SubjectService {
    SubjectDto create(String username, SubjectCreationDto subjectCreationDto);

    List<SubjectDto> getAllByUsername(String username);

    SubjectDto getByUsernameAndId(String username, Long id);

    Subject findByUsernameAndId(String username, Long id);
}
