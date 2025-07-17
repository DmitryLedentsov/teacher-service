package com.example.demo.service.impl;

import com.example.demo.dto.FileDto;
import com.example.demo.entity.File;
import com.example.demo.entity.Subject;
import com.example.demo.entity.User;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.exception.FileStorageException;
import com.example.demo.exception.FileUploadException;
import com.example.demo.mapper.FileMapper;
import com.example.demo.repo.FileRepository;
import com.example.demo.service.FileService;
import com.example.demo.service.SubjectService;
import com.example.demo.service.UserService;
import com.example.demo.util.FileContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;
    private final FileMapper fileMapper;
    private final UserService userService;
    private final SubjectService subjectService;

    private final Path baseUploadPath;

    // TODO надо получше
    @SuppressWarnings("SpellCheckingInspection")
    private final Set<String> allowedTypes = Set.of(
            "application/pdf",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "application/vnd.openxmlformats-officedocument.presentationml.presentation"
    );

    public FileServiceImpl(
            FileRepository fileRepository,
            FileMapper fileMapper,
            UserService userService,
            SubjectService subjectService,
            @Value("${user-files-dir}") String baseUploadDir
    ) {
        this.fileRepository = fileRepository;
        this.fileMapper = fileMapper;
        this.userService = userService;
        this.subjectService = subjectService;
        this.baseUploadPath = Path.of(baseUploadDir);
    }

    @Override
    public FileDto uploadFile(String username, Long subjectId, MultipartFile uploadFile) {
        validate(uploadFile);

        // TODO можно убрать лишний запрос?
        var user = userService.findByUsername(username);
        var subject = subjectService.findByUsernameAndId(username, subjectId);

        var filePath = saveToFileSystem(user, subject, uploadFile);
        var fileEntity = saveToDatabase(user, subject, uploadFile, filePath);

        return fileMapper.toDto(fileEntity);
    }

    @Override
    public FileContainer downloadFile(String username, Long subjectId, String filename) {
        var file = loadFromDatabase(username, subjectId, filename);

        var fileResource = loadFromFileSystem(file.getFolderPath(), file.getName());
        var fileType = MediaType.parseMediaType(file.getContentType());

        return new FileContainer(fileResource, fileType);
    }

    @Override
    public List<FileDto> getAllForUserSubject(String username, Long subjectId) {
        var files = fileRepository.findAllBySubject_IdAndUser_Username(subjectId, username);
        return fileMapper.toDtoList(files);
    }

    private File saveToDatabase(User user, Subject subject, MultipartFile uploadFile, Path filePath) {
        var file = fileMapper.toEntity(user, subject, uploadFile);
        file.setName(getFileName(filePath));
        file.setFolderPath(getFolderPath(filePath));
        return fileRepository.save(file);
    }

    private String getFolderPath(Path filePath) {
        return getRelativeFilePath(filePath).getParent().toString().replace("\\", "/");
    }

    private Path saveToFileSystem(User user, Subject subject, MultipartFile uploadFile) {
        try {
            var directory = createAndGetDirectory(user, subject);
            var fileName = uploadFile.getOriginalFilename();
            var filePath = getFilePath(directory, fileName);
            Files.write(filePath, uploadFile.getBytes());
            log.info("Файл сохранён: {}", filePath);
            return filePath;
        } catch (IOException e) {
            throw new FileStorageException("Ошибка при сохранении файла: " + e.getMessage());
        }
    }

    private Path getFilePath(Path userDir, String nameWithExtension) {
        var filePath = userDir.resolve(nameWithExtension);

        var extension = StringUtils.getFilenameExtension(nameWithExtension);
        var name = nameWithExtension.replace(".%s".formatted(extension), "");
        var duplicates = 1;
        while (Files.exists(filePath)) {
            filePath = userDir.resolve("%s (%d).%s".formatted(name, duplicates, extension));
            duplicates++;
        }

        return filePath;
    }

    private Path createAndGetDirectory(User user, Subject subject) {
        try {
            var userDirName = getUserDirName(user);
            var subjectDirName = getSubjectDirName(subject);
            var path = baseUploadPath.resolve(userDirName).resolve(subjectDirName);
            Files.createDirectories(path);
            return path;
        } catch (IOException e) {
            throw new FileStorageException("Ошибка при создании директории");
        }
    }

    private String getUserDirName(User user) {
        return String.valueOf(user.getHash());
    }

    private String getSubjectDirName(Subject subject) {
        return subject.getName();
    }

    private Path getRelativeFilePath(Path filePath) {
        return baseUploadPath.relativize(filePath);
    }

    private String getFileName(Path filePath) {
        return filePath.getFileName().toString();
    }

    private void validate(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileUploadException("Пустой файл");
        }

        if (file.getOriginalFilename() == null) {
            throw new FileUploadException("Безымянный файл");
        }

        if (file.getContentType() == null || !allowedTypes.contains(file.getContentType())) {
            throw new FileUploadException("Недопустимый тип файла: " + file.getContentType());
        }
    }

    private File loadFromDatabase(String username, Long subjectId, String filename) {
        return fileRepository.findByNameAndSubject_IdAndUser_Username(filename, subjectId, username)
                .orElseThrow(() -> new EntityNotFoundException("Файл %s не найден".formatted(filename)));
    }

    private Resource loadFromFileSystem(String folderPath, String fileName) {
        var filePath = baseUploadPath.resolve(folderPath).resolve(fileName);

        if (!Files.exists(filePath) || Files.isDirectory(filePath)) {
            // в БД есть, в файловой системе нет
            throw new FileStorageException("Ошибка при загрузке файла");
        }

        return new PathResource(filePath);
    }
}
