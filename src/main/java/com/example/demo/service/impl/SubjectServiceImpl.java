package com.example.demo.service.impl;

import com.example.demo.dto.SubjectCreationDto;
import com.example.demo.dto.SubjectDto;
import com.example.demo.entity.Subject;
import com.example.demo.entity.User;
import com.example.demo.exception.EntityAlreadyExistsException;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.SubjectMapper;
import com.example.demo.repo.SubjectRepository;
import com.example.demo.service.SubjectService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;
    private final UserService userService;

    @Override
    public SubjectDto create(String username, SubjectCreationDto subjectCreationDto) {
        var user = userService.findByUsername(username);
        validate(subjectCreationDto, user);
        var subject = subjectMapper.toEntity(user, subjectCreationDto);
        subjectRepository.save(subject);
        return subjectMapper.toDto(subject);
    }

    @Override
    public List<SubjectDto> getAllByUsername(String username) {
        var subjects = subjectRepository.findAllByUser_Username(username);
        return subjectMapper.toDtoList(subjects);
    }

    @Override
    public SubjectDto getByUsernameAndId(String username, Long id) {
        var subject = findByUsernameAndId(username, id);
        return subjectMapper.toDto(subject);
    }

    @Override
    public Subject findByUsernameAndId(String username, Long id) {
        return subjectRepository
                .findByIdAndUser_Username(id, username)
                .orElseThrow(() -> new EntityNotFoundException("Предмет не найден"));
    }

    private void validate(SubjectCreationDto subjectCreationDto, User user) {
        if (subjectRepository.existsByNameAndUser_Username(subjectCreationDto.name(), user.getUsername())) {
            throw new EntityAlreadyExistsException("Предмет с указанным названием уже существует");
        }
    }
}
