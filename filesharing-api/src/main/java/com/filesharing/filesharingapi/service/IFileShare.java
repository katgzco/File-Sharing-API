package com.filesharing.filesharingapi.service;

import com.filesharing.filesharingapi.entity.File;
import com.filesharing.filesharingapi.entity.FileShare;
import com.filesharing.filesharingapi.entity.User;

public interface IFileShare {
    /**
     * Finds a specific shared file for a user.
     *
     * @param file The file to find.
     * @param user The user with whom the file was shared.
     * @return The found shared file, or throws an exception if not found.
     */
    FileShare findSharedFile(File file, User user);

    /**
     * Finds a shared file from one user to another.
     *
     * @param file The file to find.
     * @param owner The owner of the file.
     * @param sharedWithUser The user with whom the file was shared.
     * @return The found shared file, or throws an exception if not found.
     */
    FileShare findFileSharedFromUserWithUser(File file, User owner, User sharedWithUser);

    /**
     * Saves the details of a shared file.
     *
     * @param fileShare The FileShare instance to save.
     */
    void save(FileShare fileShare);

    /**
     * Deletes the details of a shared file.
     *
     * @param fileShare The FileShare instance to delete.
     */
    void delete(FileShare fileShare);
}
