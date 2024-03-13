package com.filesharing.filesharingapi.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotNull;

@Data
public class FileUploadRequestDto {
    @NotNull(message = "File must not be null")
    private MultipartFile file;
}
