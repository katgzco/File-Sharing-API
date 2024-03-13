package com.filesharing.filesharingapi.dto;

import com.filesharing.filesharingapi.entity.File;
import com.filesharing.filesharingapi.entity.User;
import com.filesharing.filesharingapi.enums.FileSharePermission;
import com.filesharing.filesharingapi.enums.Role;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class FileShareDto {
    private File file;
    private User owner;
    private User sharedWithUser;
    private FileSharePermission permissions;
}
