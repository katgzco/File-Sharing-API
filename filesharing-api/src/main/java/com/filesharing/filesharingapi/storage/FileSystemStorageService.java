package com.filesharing.filesharingapi.storage;

import com.filesharing.filesharingapi.exception.EntityNotFoundException;
import com.filesharing.filesharingapi.exception.storage.StorageException;
import com.filesharing.filesharingapi.exception.storage.StorageInitializationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import java.net.MalformedURLException;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
public class FileSystemStorageService implements IStorageService{
    private final Path rootLocation;
    private static final Set<String> VALID_CONTENT_TYPES = Set.of("image/jpeg", "image/png");
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB
    /**
     * Initializes a new {@code FileSystemStorageService} instance with the specified storage location.
     *
     * @param location the storage location path
     * @throws StorageInitializationException if the storage cannot be initialized
     */
    public FileSystemStorageService(@Value("${storage.location}") String location) throws StorageInitializationException{
        this.rootLocation = Paths.get(location);
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageInitializationException("Could not initialize storage", e);
        }
    }
        public String store(MultipartFile file, String storedFileName) {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("Failed to store empty file ");
            }
            if (!isValidFileType(file)) {
                throw new UnsupportedOperationException("File type not allowed. Only JPEG and PNG images are permitted.");
            }
            if (!isValidFileSize(file)) {
                throw new IllegalArgumentException("File exceeds the maximum allowed size of 10MB.");
            }
            String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            if (originalFilename.contains("..")) {
                throw new SecurityException(
                        "File name is not allowed " + file.getOriginalFilename());
            }
            try {
                Files.copy(file.getInputStream(), this.rootLocation.resolve(storedFileName),
                        StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File successfully uploaded: " + storedFileName);
                return storedFileName;
            } catch (IOException e) {
                throw new StorageException("We are having problems saving your file, try again " + storedFileName);
            }
        }

    @Override
    public void delete(String filename) {
        try {
            boolean deleted = Files.deleteIfExists(rootLocation.resolve(filename));
            if (!deleted) {
                log.info("File did not exist and was not deleted: {}", filename);
            }
        } catch (IOException e) {
            log.error("Failed to delete file: {}", filename, e);
            throw new StorageException("We are currently having problems deleting the file, lease try again. " + filename);
        }
    }

    @Override
    public Resource download(String filename){
        try {
            Path file = rootLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(file.toUri());
            if (!resource.exists() || !resource.isReadable()){
                //LOGGER.error("File does not exist or is not readable: {}", filename);
                throw new EntityNotFoundException("The file does not exist or cannot be read: " + filename);
            }
            return resource;
        } catch (MalformedURLException e) {
            //LOGGER.error("Malformed URL for file: {}", filename, e);
            throw new StorageException("We are currently having problems downloading the file, please try again. " + filename);
        }
    }

    private boolean isValidFileType(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && VALID_CONTENT_TYPES.contains(contentType.toLowerCase());
    }

    private boolean isValidFileSize(MultipartFile file) {
        return file.getSize() <= MAX_FILE_SIZE;
    }
}
