package com.filesharing.filesharingapi.repository;

import com.filesharing.filesharingapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.filesharing.filesharingapi.entity.File;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    Optional<File> findByFileNameOriginal(String originalFileName);
    Optional<File> findByFileNameOriginalAndUser(String originalFileName, User user);

    @Query("SELECT f FROM File f WHERE f.user.username = :userName")
    List<File> findOwnedFiles(@Param("userName") String userName);

    @Query("SELECT fs.file FROM FileShare fs WHERE fs.sharedWithUser.username = :userName")
    List<File> findSharedFiles(@Param("userName") String userName);
}
