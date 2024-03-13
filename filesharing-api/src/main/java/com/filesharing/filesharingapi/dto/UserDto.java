package com.filesharing.filesharingapi.dto;

import com.filesharing.filesharingapi.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data @Builder
public class UserDto {
    private String username;
    private String email;
    private String password;
    private Role role;
}
