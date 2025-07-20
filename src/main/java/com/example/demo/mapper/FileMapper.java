package com.example.demo.mapper;

import com.example.demo.dto.FileDto;
import com.example.demo.entity.File;
import com.example.demo.entity.Subject;
import com.example.demo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = SubjectMapper.class)
public interface FileMapper {
    @Mapping(target = "url", ignore = true)
    @Mapping(target = "userId", source = "file.user.id")
    FileDto toDto(File file);

    List<FileDto> toDtoList(List<File> files);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "folderPath", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "subject", source = "subject")
    @Mapping(target = "size", source = "uploadFile.size")
    @Mapping(target = "contentType", source = "uploadFile.contentType")
    @Mapping(target = "originalName", source = "uploadFile.originalFilename")
    @Mapping(target = "uploadedAt", expression = "java(java.time.Instant.now())")
    File toEntity(User user, Subject subject, MultipartFile uploadFile);
}
