package com.example.demo.service.impl;

import com.example.demo.dto.FileDto;
import com.example.demo.entity.File;
import com.example.demo.entity.User;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.exception.FileStorageException;
import com.example.demo.exception.FileUploadException;
import com.example.demo.repo.FileRepository;
import com.example.demo.service.FileService;
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
import java.time.Instant;
import java.util.Set;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;
    private final UserService userService;

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
            UserService userService,
            @Value("${user-files-dir}") String baseUploadDir
    ) {
        this.fileRepository = fileRepository;
        this.userService = userService;
        this.baseUploadPath = Path.of(baseUploadDir);
    }

    @Override
    public FileDto save(String username, MultipartFile uploadFile) {
        validate(uploadFile);

        var user = userService.getByUsername(username);
        var filePath = saveToFileSystem(user, uploadFile);
        var fileEntity = saveToDatabase(user, uploadFile, filePath);

        return toDto(fileEntity);
    }

    @Override
    public FileContainer load(String username, String filename) {
        var file = fileRepository.findByNameAndUser_Username(filename, username)
                .orElseThrow(() -> new EntityNotFoundException("Файл %s не найден".formatted(filename)));

        var fileResource = loadFromFileSystem(file.getPath());
        var fileType = MediaType.parseMediaType(file.getContentType());

        return new FileContainer(fileResource, fileType);
    }

    private File saveToDatabase(User user, MultipartFile uploadFile, Path filePath) {
        var file = new File();
        file.setSize(uploadFile.getSize());
        file.setContentType(uploadFile.getContentType());
        file.setOriginalName(uploadFile.getOriginalFilename());
        file.setUploadedAt(Instant.now());
        file.setUser(user);
        file.setName(getFileName(filePath));
        file.setPath(getFileRelativePath(filePath).toString());
        return fileRepository.save(file);
    }

    private Path saveToFileSystem(User user, MultipartFile uploadFile) {
        try {
            var userDir = createAndGetUserDirPath(user);
            var fileName = uploadFile.getOriginalFilename();
            var filePath = getFilePath(userDir, fileName);
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

    private Path createAndGetUserDirPath(User user) {
        try {
            var userDirName = getUserDirName(user);
            var userDirPath = baseUploadPath.resolve(userDirName);
            Files.createDirectories(userDirPath);
            return userDirPath;
        } catch (IOException e) {
            throw new FileStorageException("Ошибка при создании директории");
        }
    }

    private String getUserDirName(User user) {
        return String.valueOf(user.getHash());
    }

    private Path getFileRelativePath(Path filePath) {
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

    private Resource loadFromFileSystem(String path) {
        var filePath = baseUploadPath.resolve(path);

        if (!Files.exists(filePath)) {
            // в БД есть, в файловой системе нет
            throw new FileStorageException("Ошибка при загрузке файла");
        }

        return new PathResource(filePath);
    }

    // TODO MapStruct и определиться, что передавать
    private FileDto toDto(File fileEntity) {
        var fileDto = new FileDto();
        fileDto.setId(fileEntity.getId());
        fileDto.setLink(fileEntity.getPath());
        fileDto.setSize(fileEntity.getSize());
        fileDto.setName(fileEntity.getName());
        fileDto.setUploadedAt(fileEntity.getUploadedAt());
        fileDto.setUserId(fileEntity.getUser().getId());
        return fileDto;
    }
}
