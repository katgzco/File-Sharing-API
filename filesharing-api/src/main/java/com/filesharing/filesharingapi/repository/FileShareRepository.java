package com.filesharing.filesharingapi.repository;

import com.filesharing.filesharingapi.entity.File;
import com.filesharing.filesharingapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.filesharing.filesharingapi.entity.FileShare;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileShareRepository extends JpaRepository<FileShare, Long> {
    Optional<FileShare> findByFileAndSharedWithUser(File file, User user);
    Optional<FileShare> findByFileAndOwnerAndSharedWithUser(File file, User owner, User sharedWithUser);
}
