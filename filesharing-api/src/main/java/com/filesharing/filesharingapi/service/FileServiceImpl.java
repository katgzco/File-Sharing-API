package com.filesharing.filesharingapi.service;

import com.filesharing.filesharingapi.dto.FileDto;
import com.filesharing.filesharingapi.dto.request.FileUploadRequestDto;
import com.filesharing.filesharingapi.dto.response.FileResponseDTO;
import com.filesharing.filesharingapi.dto.FileSharePermissionDTO;
import com.filesharing.filesharingapi.dto.response.UserFilesOverviewDTO;
import com.filesharing.filesharingapi.entity.FileShare;
import com.filesharing.filesharingapi.entity.User;
import com.filesharing.filesharingapi.enums.FileSharePermission;
import com.filesharing.filesharingapi.exception.EntityAlreadyExistException;
import com.filesharing.filesharingapi.exception.EntityNotFoundException;
import com.filesharing.filesharingapi.exception.FileShareException;
import com.filesharing.filesharingapi.exception.storage.DataBaseOperationException;
import com.filesharing.filesharingapi.mapper.FileMapper;
import com.filesharing.filesharingapi.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import com.filesharing.filesharingapi.repository.FileRepository;
import com.filesharing.filesharingapi.storage.IStorageService;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.filesharing.filesharingapi.entity.File;

import java.time.LocalDateTime;

import java.util.*;
@Slf4j
@Service
public class FileServiceImpl {
    private final FileRepository fileRepository;
    private final IStorageService storageService;
    private final UserServiceImpl userService;

    private final IFileShare fileShareService;
    private final FileMapper fileMapper = FileMapper.INSTANCE;
    private final UserMapper userMapper = UserMapper.INSTANCE;


    public FileServiceImpl(FileRepository fileRepository, IStorageService storageService, UserServiceImpl userService,IFileShare fileShareService) {
        this.fileRepository = fileRepository;
        this.storageService = storageService;
        this.userService = userService;
        this.fileShareService = fileShareService;
    }

    public File saveFile(FileUploadRequestDto fileUploadDto, String userName) {
        MultipartFile multipartFile = fileUploadDto.getFile();
        if (userName == null || userName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name is required");
        }
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        var user = userService.findByUsername(userName);
        try {
            var existingFile = getFileByFileNameAndUser(fileName, user);
            String storedFileName = existingFile.getStoredFileName();
            existingFile.setUpdatedAt(LocalDateTime.now());
            storageService.store(multipartFile, storedFileName);
            return saveFile(existingFile);
        }catch (EntityNotFoundException e) {
            String storedFileName = UUID.randomUUID().toString();
            String filePath = storageService.store(multipartFile, storedFileName);
            File file = new File();
            file.setUser(user);
            file.setFileNameOriginal(fileName);
            file.setStoredFileName(storedFileName);
            file.setFilePath(filePath);
            file.setFileSize(multipartFile.getSize());
            file.setCreatedAt(LocalDateTime.now());
            return saveFile(file);
        }
    }

    public Resource downloadFile(String originalFileName, String username) {
        if (originalFileName == null || originalFileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name must be provided.");
        }
        var user = userService.findByUsername(username);
        var file = getFileNameOriginal(originalFileName);

        if (!file.getUser().equals(user)) {
            try {
                fileShareService.findSharedFile(file, user);
            } catch (EntityNotFoundException e) {
                throw new IllegalArgumentException("File not found");
            }
        }
        return storageService.download(file.getStoredFileName());
    }

    public void deleteFile(String originalFileName, String username) {
        if (originalFileName == null || originalFileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name is required");
        }
        var user = userService.findByUsername(username);
        var file = getFileByFileNameAndUser(originalFileName, user);
        storageService.delete(file.getStoredFileName());
        deleteFile(file);
    }

    public void updateFileName(String fileName, String newFileName, String username) {
        if (!StringUtils.hasText(fileName) || !StringUtils.hasText(newFileName)){
            throw new IllegalArgumentException("file's name must be provided.");
        }
        if (!StringUtils.hasText(username)) {
            throw new IllegalArgumentException("User name must be provided.");
        }
        if (fileName.equals(newFileName)) {
            throw new IllegalArgumentException("New file name is the same as the old one.");
        }
        var user = userService.findByUsername(username);
        try {
            getFileByFileNameAndUser(newFileName, user);
            throw new EntityAlreadyExistException("File with the new name already exists.");
        } catch (EntityNotFoundException e) {
            File file = getFileByFileNameAndUser(fileName, user);
            file.setFileNameOriginal(newFileName);
            file.setUpdatedAt(LocalDateTime.now());
            saveFile(file);
        }
    }

    public void shareFileWithUser(String fileName, String ownerUsername, String sharedWithUsername, FileSharePermission permissions) {
        User owner = userService.findByUsername(ownerUsername);
        User sharedWith = userService.findByUsername(sharedWithUsername);

        File file = getFileByFileNameAndUser(fileName, owner);
        try {
            fileShareService.findFileSharedFromUserWithUser(file, owner, sharedWith);
            throw new FileShareException("File is already shared with the user.");
        } catch (EntityNotFoundException e) {
            FileShare fileShare = new FileShare();
            fileShare.setFile(file);
            fileShare.setOwner(owner);
            fileShare.setSharedWithUser(sharedWith);
            fileShare.setPermissions(permissions);
            fileShareService.save(fileShare);
        }
    }

    public void removeFileAccess(String fileName, String ownerUsername, String sharedWithUsername) {
        User owner = userService.findByUsername(ownerUsername);
        User sharedWith = userService.findByUsername(sharedWithUsername);

        File file = getFileByFileNameAndUser(fileName, owner);

        FileShare fileShare = fileShareService.findFileSharedFromUserWithUser(file, owner, sharedWith);
        fileShareService.delete(fileShare);
    }


    public UserFilesOverviewDTO listAllUserFiles(String userName) {
        userService.findByUsername(userName);
        UserFilesOverviewDTO userFilesDto;
        List<FileResponseDTO> fileResponseOwned = new ArrayList<>();
        List<FileResponseDTO> fileResponseShared = new ArrayList<>();
        var filesOwned = getFileByFileNameAndUser(userName);
        if (!filesOwned.isEmpty()) {
            filesOwned.forEach(file ->
                    fileResponseOwned.add(FileResponseDTO
                            .builder()
                            .fileName(file.getFileNameOriginal())
                            .uploadDate(file.getCreatedAt())
                            .fileSize(file.getFileSize())
                            .updateDate(file.getUpdatedAt())
                            .owner(file.getUser().getUsername())
                            .build()));
        }
        var filesShared = getSharedFiles(userName);
        if (!filesShared.isEmpty()) {
            filesShared.forEach(file ->
                    fileResponseShared.add(FileResponseDTO
                            .builder()
                            .fileName(file.getFileNameOriginal())
                            .uploadDate(file.getCreatedAt())
                            .fileSize(file.getFileSize())
                            .updateDate(file.getUpdatedAt())
                            .owner(file.getUser().getUsername())
                            .build()));
        }
        userFilesDto = UserFilesOverviewDTO.builder()
                .owned(fileResponseOwned)
                .shared(fileResponseShared)
                .build();
        return userFilesDto;
    }

    public File getFileByFileNameAndUser(String fileName, User user) {
        if (!StringUtils.hasText(fileName) || user == null) {
            throw new IllegalArgumentException("File name and user must provided");
        }
        try {
            return fileRepository.findByFileNameOriginalAndUser(fileName, user)
                    .orElseThrow(() -> new EntityNotFoundException("File not found."));
        } catch (DataAccessException e) {
            throw new DataBaseOperationException("An unexpected error occurred. Please try again later.");
        }
    }

    public File getFileNameOriginal(String fileName) {
        if (!StringUtils.hasText(fileName)) {
            throw new IllegalArgumentException("File name must provided");
        }
        try {
            return fileRepository.findByFileNameOriginal(fileName)
                    .orElseThrow(() -> new EntityNotFoundException("File not found."));
        } catch (DataAccessException e) {
            throw new DataBaseOperationException("An unexpected error occurred. Please try again later.");
        }
    }


    public File saveFile(File file) {
        if (file == null) {
            throw new IllegalArgumentException("The file cannot be empty.");
        }
        try {
            File savedFile = fileRepository.save(file);
            log.info("File saved successfully: {}", savedFile.getFileId());
            return savedFile;
        } catch (DataAccessException e) {
            log.error("Error saving the file: {}", e.getMessage());
            throw new DataBaseOperationException("An unexpected error occurred. Please try again later.");
        }
    }

    public void deleteFile(File file) {
        if (file == null) {
            throw new IllegalArgumentException("The file cannot be empty.");
        }
        try {
            fileRepository.delete(file);
            log.info("Successfully deleted file: {}", file.getStoredFileName());
        } catch (DataAccessException e) {
            log.error("Error occurred while deleting file: {}", file.getStoredFileName(), e);
            throw new DataBaseOperationException("An unexpected error occurred. Please try again later.");

        }
    }

    public List<File> getFileByFileNameAndUser(String userName) {
        if (!StringUtils.hasText(userName)) {
            throw new IllegalArgumentException("File name and user must provided");
        }
        try {
            return fileRepository.findOwnedFiles(userName);
        } catch (DataAccessException e) {
            throw new DataBaseOperationException("An unexpected error occurred. Please try again later.");
        }
    }

    public List<File> getSharedFiles(String userName) {
        if (!StringUtils.hasText(userName)) {
            throw new IllegalArgumentException("File name and user must provided");
        }
        try {
            return fileRepository.findSharedFiles(userName);
        } catch (DataAccessException e) {
            throw new DataBaseOperationException("An unexpected error occurred. Please try again later.");
        }
    }
}
