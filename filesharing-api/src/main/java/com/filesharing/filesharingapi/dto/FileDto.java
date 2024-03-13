package com.filesharing.filesharingapi.dto;

import com.filesharing.filesharingapi.entity.User;
import com.filesharing.filesharingapi.enums.Role;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
public class FileDto {
    private UserDto user;
    private String storedFileName;
    private String fileNameOriginal;
    private String filePath;
    private Long fileSize;
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;
}

