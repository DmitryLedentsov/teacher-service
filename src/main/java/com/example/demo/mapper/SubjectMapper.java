package com.example.demo.mapper;

import com.example.demo.dto.SubjectCreationDto;
import com.example.demo.dto.SubjectDto;
import com.example.demo.entity.Subject;
import com.example.demo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SubjectMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "files", ignore = true)
    Subject toEntity(User user, SubjectCreationDto subjectCreationDto);

    SubjectDto toDto(Subject subject);

    List<SubjectDto> toDtoList(List<Subject> subjects);
}
