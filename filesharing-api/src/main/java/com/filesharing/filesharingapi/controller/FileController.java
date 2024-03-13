package com.filesharing.filesharingapi.controller;
import com.filesharing.filesharingapi.constants.Constants;
import com.filesharing.filesharingapi.dto.response.ResponseDto;
import com.filesharing.filesharingapi.dto.request.FileUploadRequestDto;
import com.filesharing.filesharingapi.dto.response.UserFilesOverviewDTO;
import com.filesharing.filesharingapi.enums.FileSharePermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.core.io.Resource;
import com.filesharing.filesharingapi.service.FileServiceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * Rest Controller for managing file operations including uploading, downloading,
 * sharing, and deleting files for the file-sharing API.
 * <p>
 * This controller handles operations related to file management for authenticated users,
 * allowing for file upload, download, sharing with other users, and file deletion.
 */
@Tag(name = "File Management", description = "Operations related to file management")
@RestController
@RequestMapping("/api/v1/files")
@Validated
public class FileController {

    private final FileServiceImpl fileStorageService;
    /**
     * Constructs a {@link FileController} with the necessary file storage service.
     *
     * @param fileStorageService the service responsible for handling file storage operations.
     */
    public FileController(FileServiceImpl fileStorageService) {
        this.fileStorageService = fileStorageService;
    }


    /**
     * Handles the uploading of a file by a user.
     * <p>
     * The file is saved using the provided metadata within {@link FileUploadRequestDto}. The method
     * also authenticates the user performing the operation.
     *
     * @param fileUploadRequestDto the file upload request containing file and metadata.
     * @return a {@link ResponseEntity} with the upload status message.
     */
    @Operation(summary = "Upload a file")
    @PostMapping(path="/upload")
    @PreAuthorize("hasRole('USER')")
    public ResponseDto uploadFile(@Valid @ModelAttribute FileUploadRequestDto fileUploadRequestDto) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            fileStorageService.saveFile(fileUploadRequestDto, username);
            return ResponseDto.builder()
                    .statusCode(Constants.STATUS_201)
                    .statusMessage("Archivo subido con éxito:")
                    .build();
    }

    /**
     * Allows a user to download a file by its filename.
     * <p>
     * The method retrieves the file as a {@link Resource} and prepares it for download,
     * setting the appropriate content disposition in the response header.
     *
     * @param filename the name of the file to download.
     * @return a {@link ResponseEntity} containing the file as a resource.
     */
    @GetMapping("/download/{filename:.+}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Resource> downloadFile(@PathVariable
                                                     @NotBlank(message = "File name is mandatory")
                                                     @Pattern(regexp = "^[\\w,\\s-]+\\.[A-Za-z]{3}$", message = "Invalid file name")
                                                     String filename) {
        Resource file;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        file = fileStorageService.downloadFile(filename, username);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }


    @DeleteMapping("/delete/{filename}")
    @PreAuthorize("hasRole('USER')")
    public ResponseDto deleteFile(@PathVariable
                                            @NotBlank(message = "File name is mandatory")
                                            @Pattern(regexp = "^[\\w,\\s-]+\\.[A-Za-z]{3}$", message = "Invalid file name")
                                            String filename) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            fileStorageService.deleteFile(filename, username);
                return ResponseDto.builder()
                        .statusCode(Constants.STATUS_201)
                        .statusMessage("Archivo eliminado con éxito.")
                        .build();

    }

    @PutMapping("/updateFileName")
    @PreAuthorize("hasRole('USER')")
    public ResponseDto updateFileName(@RequestParam
                                                   @Pattern(regexp = "^[\\w,\\s-]+\\.[A-Za-z]{3}$", message = "Invalid file name")
                                                     String fileName,
                                                 @RequestParam
                                                 @Pattern(regexp = "^[\\w,\\s-]+\\.[A-Za-z]{3}$", message = "Invalid file name")
                                                 String newFileName) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
             fileStorageService.updateFileName(fileName, newFileName, username);
            return ResponseDto.builder()
                .statusCode(Constants.STATUS_204)
                .statusMessage("File name updated successfully.")
                .build();
    }

    @PostMapping("/share/{fileName}/{sharedWithUsername}")
    //@PreAuthorize("hasRole('USER')")
    public ResponseDto shareFile(@PathVariable
                                           @NotBlank(message = "File name is mandatory")
                                           @Pattern(regexp = "^[\\w,\\s-]+\\.[A-Za-z]{3}$", message = "Invalid file name")
                                           String fileName,
                                       @PathVariable String sharedWithUsername,
                                       @RequestParam FileSharePermission permissions
    ) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            fileStorageService.shareFileWithUser(fileName, username, sharedWithUsername, permissions);
            return ResponseDto.builder()
                .statusCode(Constants.STATUS_201)
                .statusMessage("File shared successfully")
                .build();

    }

    @DeleteMapping("/removeAccess/{fileName}/{sharedWithUsername}")
    @PreAuthorize("hasRole('USER')")
    public ResponseDto removeFileAccess(@PathVariable
                                                  @NotBlank(message = "File name is mandatory")
                                                  @Pattern(regexp = "^[\\w,\\s-]+\\.[A-Za-z]{3}$", message = "Invalid file name")
                                                  String fileName,
                                              @PathVariable
                                              @NotBlank(message = "username is mandatory")
                                              String sharedWithUsername) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
            fileStorageService.removeFileAccess(fileName, username, sharedWithUsername);
        return ResponseDto.builder()
                .statusCode(Constants.STATUS_201)
                .statusMessage("Access removed successfully.")
                .build();

    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserFilesOverviewDTO> listAllUserFiles() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            UserFilesOverviewDTO userFilesDto = fileStorageService.listAllUserFiles(username);
            return ResponseEntity.ok(userFilesDto);
    }
}

