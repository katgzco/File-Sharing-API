package com.filesharing.filesharingapi.service;

import com.filesharing.filesharingapi.constants.Constants;
import com.filesharing.filesharingapi.entity.User;

import com.filesharing.filesharingapi.exception.EntityAlreadyExistException;
import com.filesharing.filesharingapi.exception.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.filesharing.filesharingapi.exception.storage.DataBaseOperationException;

import com.filesharing.filesharingapi.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl {

    private UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserDetailsService userDetailsService() {
        return this::findByUsername;
    }

    public User save(User newUser) {
        // User user = UserMapper.mapToUser(newUser, new User());
        if (newUser == null) {
            throw new IllegalArgumentException("User must be provided");
        }
        try {
            return userRepository.save(newUser);
        }catch (DataIntegrityViolationException e) {
            logger.error("An error occurred while saving user in the database", e);
            throw new EntityAlreadyExistException("User or email already exist");
        } catch (DataAccessException e) {
            logger.error("An error occurred while saving user in the database", e);
            throw new DataBaseOperationException("An unexpected error occurred. Please try again later.");
        }
    }

    public Boolean existByUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("User name must be provided");
        }
        try {
            return userRepository.findByUsername(username).isPresent();
        } catch (DataAccessException e) {
            logger.error("An error occurred while accessing the database to check username existence", e);
            throw new DataBaseOperationException("An unexpected error occurred. Please try again later.");
        }
    }

    public Boolean existByEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("email must be provided");
        }
        try {
            return userRepository.findByEmail(email).isPresent();
        } catch (DataAccessException e) {
            logger.error("An error occurred while accessing the database to check email existence", e);
            throw new DataBaseOperationException("An unexpected error occurred. Please try again later.");
        }
    }

    public User findByUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("username must be provided.");
        }
        try {
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new EntityNotFoundException(Constants.USER_NOT_FOUND));
        } catch (DataAccessException e) {
            logger.error("An error occurred while accessing the database to check username existence", e);
            throw new DataBaseOperationException("An unexpected error occurred. Please try again later.");
        }
    }
}

