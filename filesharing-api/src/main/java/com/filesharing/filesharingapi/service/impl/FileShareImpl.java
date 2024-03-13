package com.filesharing.filesharingapi.service.impl;

import com.filesharing.filesharingapi.entity.File;
import com.filesharing.filesharingapi.entity.FileShare;
import com.filesharing.filesharingapi.entity.User;
import com.filesharing.filesharingapi.exception.EntityNotFoundException;
import com.filesharing.filesharingapi.exception.storage.DataBaseOperationException;
import com.filesharing.filesharingapi.repository.FileShareRepository;
import com.filesharing.filesharingapi.service.IFileShare;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FileShareImpl implements IFileShare {

    private final FileShareRepository fileShareRepository;

    public FileShareImpl(FileShareRepository fileShareRepository) {
        this.fileShareRepository = fileShareRepository;
    }

    public FileShare findSharedFile(File file, User user) {
        if (file == null || user == null) {
            throw new IllegalArgumentException("File name or user name must be provided.");
        }
        try {
            return fileShareRepository.findByFileAndSharedWithUser(file, user)
                    .orElseThrow(() -> new EntityNotFoundException("No shared file was found with user: " + user.getUsername()));
        } catch (DataAccessException e) {
           log.error("An error occurred while accessing the database: {}", e.getMessage(), e);
            throw new DataBaseOperationException("An unexpected error occurred. Please try again later.");
        }
    }

    public FileShare findFileSharedFromUserWithUser(File file, User owner, User sharedWithUser) {
        if (file == null || owner == null || sharedWithUser == null) {
            throw new IllegalArgumentException("File, Owner, and SharedWithUser must be provided.");
        }
        try {
            return fileShareRepository.findByFileAndOwnerAndSharedWithUser(file, owner, sharedWithUser)
                    .orElseThrow(() -> new EntityNotFoundException("No shared file was found to user: " + sharedWithUser.getUsername()));
        } catch (DataAccessException e) {
            //logger.error("An error occurred while accessing the database: {}", e.getMessage(), e);
            throw new DataBaseOperationException("An unexpected error occurred. Please try again later.");
        }
    }

    public void save(FileShare fileShare) {
        if (fileShare == null) {
            throw new IllegalArgumentException("fileShare must be provided");
        }
        try {
             fileShareRepository.save(fileShare);
        } catch (DataAccessException e) {
            log.error("An error occurred while saving user in the database", e);
            throw new DataBaseOperationException("An unexpected error occurred. Please try again later.");
        }
    }

    public void delete(FileShare fileShare) {
        if (fileShare == null) {
            throw new IllegalArgumentException("fileShare must be provided");
        }
        try {
            fileShareRepository.delete(fileShare);
        } catch (DataAccessException e) {
            log.error("An error occurred while saving user in the database", e);
            throw new DataBaseOperationException("An unexpected error occurred. Please try again later.");
        }
    }


}
