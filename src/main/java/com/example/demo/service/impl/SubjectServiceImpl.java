package com.example.demo.service.impl;

import com.example.demo.dto.SubjectCreationDto;
import com.example.demo.dto.SubjectDto;
import com.example.demo.entity.Subject;
import com.example.demo.entity.User;
import com.example.demo.exception.EntityAlreadyExistsException;
import com.example.demo.exception.EntityNotFoundException;
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
    private final UserService userService;

    @Override
    public SubjectDto create(String username, SubjectCreationDto subjectCreationDto) {
        var user = userService.getByUsername(username);

        validateSubject(subjectCreationDto, user);

        var subject = toEntity(user, subjectCreationDto);
        subjectRepository.save(subject);
        return toDto(subject);
    }

    @Override
    public List<SubjectDto> getAll(String username) {
        var subjects = subjectRepository.findAllByUser_Username(username);
        return toDtoList(subjects);
    }

    @Override
    public SubjectDto getById(String username, Long id) {
        return subjectRepository
                .findByIdAndUser_Username(id, username)
                .map(this::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Предмет не найден"));
    }

    private void validateSubject(SubjectCreationDto subjectCreationDto, User user) {
        if (subjectRepository.existsByNameAndUser_Username(subjectCreationDto.name(), user.getUsername())) {
            throw new EntityAlreadyExistsException("Предмет с указанным названием уже существует");
        }
    }

    private Subject toEntity(User user, SubjectCreationDto subjectCreationDto) {
        var subject = new Subject();
        subject.setUser(user);
        subject.setName(subjectCreationDto.name());
        return subject;
    }

    private SubjectDto toDto(Subject subject) {
        return new SubjectDto(subject.getId(), subject.getName());
    }

    private List<SubjectDto> toDtoList(List<Subject> subjects) {
        return subjects.stream().map(this::toDto).toList();
    }
}
