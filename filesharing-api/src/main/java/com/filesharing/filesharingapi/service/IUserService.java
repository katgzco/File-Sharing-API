package com.filesharing.filesharingapi.service;

import com.filesharing.filesharingapi.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService {
    UserDetailsService userDetailsService();

}
