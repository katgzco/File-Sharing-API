package com.filesharing.filesharingapi.security;

import com.filesharing.filesharingapi.constants.Constants;

import com.filesharing.filesharingapi.dto.request.SignInRequest;
import com.filesharing.filesharingapi.dto.request.SignUpRequest;

import com.filesharing.filesharingapi.exception.EntityAlreadyExistException;
import com.filesharing.filesharingapi.exception.AuthenticationException;
import com.filesharing.filesharingapi.exception.storage.DataBaseOperationException;

import com.filesharing.filesharingapi.enums.Role;
import com.filesharing.filesharingapi.entity.User;
import com.filesharing.filesharingapi.service.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String signup(SignUpRequest request) {
         if (userService.existByUsername(request.getUsername())){
                throw new EntityAlreadyExistException(Constants.USER_ALREADY_EXISTS);
         }
         if (userService.existByEmail(request.getEmail())) {
             throw new EntityAlreadyExistException(Constants.EMAIL_ALREADY_EXISTS);
         }

        var user = User
                .builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        try {
                userService.save(user);
        } catch (DataBaseOperationException e) {
            throw new DataBaseOperationException("An unexpected error occurred. Please try again later.");
        }
        return "Usuario " + request.getUsername() + " creado con éxito";
    }

    public String signin(SignInRequest request) {
        var user = userService.findByUsername(request.getUserName());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
        } catch (Exception ex) {
            throw new AuthenticationException(Constants.USER_AUTHENTICATION_FAILED + request.getUserName());
        }
        return jwtService.generateToken(user);
    }

}
