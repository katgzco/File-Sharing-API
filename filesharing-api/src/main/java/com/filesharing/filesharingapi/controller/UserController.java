package com.filesharing.filesharingapi.controller;

import com.filesharing.filesharingapi.constants.Constants;
import com.filesharing.filesharingapi.dto.response.ResponseDto;
import com.filesharing.filesharingapi.security.AuthenticationService;
import com.filesharing.filesharingapi.dto.response.JwtAuthenticationResponse;
import com.filesharing.filesharingapi.dto.request.SignUpRequest;
import com.filesharing.filesharingapi.dto.request.SignInRequest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

@RestController
@RequestMapping(path = "/api/v1", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class UserController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseDto signup(@RequestBody SignUpRequest request) {
          var message = authenticationService.signup(request);
        return ResponseDto.builder()
                .statusCode(Constants.STATUS_201)
                .statusMessage(message)
                .build();
    }

    @PostMapping("/signin")
    public JwtAuthenticationResponse signin(@RequestBody SignInRequest request) {
        String token = authenticationService.signin(request);
        return JwtAuthenticationResponse.builder()
                .token(token)
                .build();
    }
}
